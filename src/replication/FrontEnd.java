package replication;

import common.Customer;
import common.UdpServer;
import common.logger.LoggerClient;
import common.share.ShareOrder;
import common.util.Config;
import replication.messageObjects.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
	private static HashMap<Long, Thread> unconfirmedRequests = new HashMap<>();
	/**
	 * Maps sequence numbers to waiting client threads.
	 */
	private static HashMap<Long, Thread> waitingRequests = new HashMap<>();
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
		this.launch(Integer.parseInt(Config.getInstance().getAttr("FrontEndPort")));
	}

	public boolean sendOrderAndWaitForReply(ShareOrder so, Customer cust) {
		OrderMessage om;
		synchronized(tagIdCounter) {
			tagIdCounter++;
			om = new OrderMessage(tagIdCounter, so, cust, 8008);
			unconfirmedRequests.put(tagIdCounter, Thread.currentThread());
		}

		int port = 8008;
		MessageEnvelope me = new MessageEnvelope(om);
		LoggerClient.log("Sending message envelope to sequencer");

		this.send(me, "localhost", Config.getInstance().getAttr("SequencerPort"));

		// Block by starting a server, wait for response
		DatagramSocket serverSocket = null;
		ByteArrayInputStream bis;
		ObjectInput in = null;
		try{
			LoggerClient.log("Starting server on " + port);
			serverSocket = new DatagramSocket(port);

			System.out.println("binding port " + port);
			byte[] data;
			byte[] receiveData = new byte[4024];
			while (true) { //TODO: we need a way to teardown the servers
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);

				data = new byte[receivePacket.getLength()];
				System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());

				bis = new ByteArrayInputStream(data);
				in = new ObjectInputStream(bis);
				Object obj = in.readObject();
				MessageEnvelope msgEvn = (MessageEnvelope) obj;
				LoggerClient.log("RECEIVED PACKAGE: " + msgEvn.getType());
				OrderResponseMessage oRM = me.getOrderResponseMessage();
				if(oRM == null) {
					LoggerClient.log("ORM is NULL");
				}
				Long sequenceID = oRM.getSequence();

				LoggerClient.log("SequenceID: " + sequenceID);
				votingTable.castVote(sequenceID, oRM.getReplicaID(), oRM.getResult());

				Boolean result = votingTable.checkResults(sequenceID);


				serverSocket.close();
				return result;
			}
		} catch(IOException ioe) {
			LoggerClient.log("IOE exception in FE: " + ioe.getStackTrace());
			ioe.printStackTrace();
			return false;
		} catch(ClassNotFoundException cne){
			LoggerClient.log("CNE exception in FE");
			cne.printStackTrace();
			return false;
		} catch(ClassCastException cce) {
			LoggerClient.log("CCE exception in FE");
			cce.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void incomingMessageHandler(MessageEnvelope me) {
		LoggerClient.log("Message received: " + me.toString());
		LoggerClient.log("Message type: " + me.getType());

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

		LoggerClient.log("SequenceID: " + sequenceID);
		votingTable.castVote(sequenceID, oRM.getReplicaID(), oRM.getResult());

		Boolean result = votingTable.checkResults(sequenceID);

		if (result == null) {
			LoggerClient.log("Voting table failed?");
			return;
		}
		// Check if there is a client waiting for this result
		if (!waitingRequests.containsKey(sequenceID)) {
			LoggerClient.log("Could not find waiting thread");
			return;
		}

		Thread threadToNotify = waitingRequests.get(sequenceID);
		LoggerClient.log("Thread to notify = " + threadToNotify);
		LoggerClient.log("Result from voting table is " + result);
		if (result) {
			// a true result means the purchase was successful, we
			// use notify to have the client thread continue execution
			LoggerClient.log("Notifying");
			threadToNotify.notify();
		} else {
			// false result means the purchase failed, we inform the
			// waiting client thread by using an interrupt exception
			LoggerClient.log("interrupting");
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
