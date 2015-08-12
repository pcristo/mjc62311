package replication;

import common.Customer;
import common.UdpServer;
import common.share.ShareOrder;
import common.util.Config;
import replication.messageObjects.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Front end to the reliable replicated system
 * 
 * @author Patrick
 */
public class FrontEnd extends UdpServer {
	/**
	 * Maps request IDs to waiting client threads. The Request ID represents the
	 * number sent to the sequencer while waiting for the sequencer to return
	 * the sequence number. Once received, the entry should be removed from this
	 * map and migrated to the waitingRequests map.
	 */
	private HashMap<Long, Thread> unconfirmedRequests;
	/**
	 * Maps sequence numbers to waiting client threads.
	 */
	private HashMap<Long, Thread> waitingRequests = new HashMap<>();
	/**
	 * Tracks the results sent by RMs and notifies if an RM is bad
	 */
	private VotingTable votingTable = new VotingTable();
	/**
	 * The temporary ID assigned to orders when sending them to the sequencer
	 */
	private static Long tagIdCounter = (long) 0;

	/**
	 * Create a front end and launch the server right away
	 */
	public FrontEnd() {
		unconfirmedRequests = new HashMap<>();
		this.launch(Integer.parseInt(Config.getInstance().getAttr("FrontEndPort")));
	}

	public boolean sendOrderAndWaitForReply(ShareOrder so, Customer cust) {
		OrderMessage om;
		synchronized(tagIdCounter) {
			tagIdCounter++;
			om = new OrderMessage(tagIdCounter, so, cust);
			unconfirmedRequests.put(tagIdCounter, Thread.currentThread());
		}
		MessageEnvelope me = new MessageEnvelope(om);
		
		this.send(me, "localhost", Config.getInstance().getAttr("SequencerPort"));
		
	//	try {
	//		Thread.currentThread().wait();
	//	} catch (InterruptedException e) {
			// thread interrupted means the call failed.
	//		return false;
	//	}
		
		// thread continues means the call succeeded
		return true;		
	}
	
	@Override
	protected void incomingMessageHandler(MessageEnvelope me) {
		System.out.println("Message received: " + me.toString());

		switch (me.getType()) {
		case SequencerResponseMessage:
			processSequencerResponseMessage(me.getSequencerResponseMessage());
			break;
		case OrderResponseMessage:
			processOrderResponseMessage(me.getOrderResponseMessage());
			checkForFailedRms();
			break;
		case RegisterRmMessage:
			votingTable.registerRM(me.getRegisterRmMessage().getReplicaID());
			break;
		case UnregisterRmMessage:
			votingTable.unregisterRM(me.getUnregisterRmMessage().getReplicaID());
			break;
		default:
			break;
		}
	}

	/**
	 * Removes the a thread from the unconfirmed table and places it into the
	 * waiting table, where it can receive responses from the RMs.
	 * 
	 * @param sRM
	 */
	private void processSequencerResponseMessage(SequencerResponseMessage sRM) {
		Long sequenceID = sRM.getSequence();
		Long tagID = sRM.getTag();

		Thread thread = unconfirmedRequests.get(tagID);
		waitingRequests.put(sequenceID, thread);
		unconfirmedRequests.remove(tagID);
	}

	/**
	 * Processes an order response from an RM by adding its vote to the voting
	 * table and notifying the client if enough votes have been received.
	 * 
	 * @param oRM
	 */
	private void processOrderResponseMessage(OrderResponseMessage oRM) {
		Long sequenceID = oRM.getSequence();

		votingTable.castVote(sequenceID, oRM.getReplicaID(), oRM.getResult());

		Boolean result = votingTable.checkResults(sequenceID);

		if (result == null)
			return;

		// Check if there is a client waiting for this result
		if (!waitingRequests.containsKey(sequenceID))
			return;

		Thread threadToNotify = waitingRequests.get(sequenceID);

		if (result) {
			// a true result means the purchase was successful, we
			// use notify to have the client thread continue execution
			threadToNotify.notify();
		} else {
			// false result means the purchase failed, we inform the
			// waiting client thread by using an interrupt exception
			threadToNotify.interrupt();
		}

		// we don't need to keep track of this thread anymore,
		// because it has received its reply
		waitingRequests.remove(sequenceID);
	}

	/**
	 * Checks if any RMs have failed status, and sends a Failed RM message for
	 * each failed RM found.
	 */
	private void checkForFailedRms() {
		Byte threshold = Byte.parseByte(Config.getInstance().getAttr(
				"ReplicaFailureThreshold"));

		ArrayList<Long> failedRMs = votingTable.identifyProblemRMs(threshold);
			
		for (Long failedRm : failedRMs) {
			FailedRmMessage fRM = new FailedRmMessage(failedRm);
			MessageEnvelope me = new MessageEnvelope(fRM);
			
			this.send(me, "localhost", Config.getInstance().getAttr("SequencerPort"));
		}
	}


	public static void main(String[] args) {
		new FrontEnd();
	}
}
