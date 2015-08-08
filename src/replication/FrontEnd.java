package replication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.UdpServer;
import common.share.ShareOrder;
import common.util.Config;
import replication.messageObjects.*;

/**
 * Front end to the reliable replicated system
 * @author Patrick
 */
public class FrontEnd extends UdpServer {
	/**
	 * Maps request IDs to waiting client threads. The Request ID represents 
	 * the number sent to the sequencer while waiting for the sequencer to 
	 * return the sequence number. Once received, the entry should be removed 
	 * from this map and migrated to the waitingRequests map.
	 */
	private HashMap<Long, Thread> unconfirmedRequests;
	/**
	 * Maps sequence numbers to waiting client threads.
	 */
	private HashMap<Long, Thread> waitingRequests;	
	/**
	 * Tracks the results sent by RMs and notifies if an RM is bad
	 */
	private VotingTable votingTable = new VotingTable();
	/**
	 * The temporary ID assigned to orders when sending them to the sequencer
	 */
	private long requestID = 0;	
	
	/**
	 * Create a front end and launch the server right away
	 */
	public FrontEnd() {
		this.launch(Integer.parseInt(Config.getInstance().getAttr("FrontEndPort")));
	}
	
	@Override
	protected void incomingMessageHandler(MessageEnvelope me) {
		System.out.println("Message received");
		
		switch (me.getType()) {
		case OrderMessage:
			// client send a share order -- but does this need to be Java RMI/CORBA/WS?
			
			// send the request to sequencer
			// get back a sequence number *HOW TO MATCH SEQUENCE WITH REQUEST??*
			//       maybe send back the whole order with a sequence number for matching?
			// store sequence and Thread.currentThread() in a hashmap 
			// wait until notified or interrupted
			// send a response to the client
			System.out.println("ShareOrder");
			break;
		case SequencerResponseMessage:
			// update the local table to include the sequence number
			// remove the item from the unconfirmed list
			System.out.println("SequencerResponseMessage");
			break;
		case OrderResponseMessage:
			processOrderResponseMessage(me.getOrderResponseMessage());
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

	private void processOrderResponseMessage(
			OrderResponseMessage oRM) {
		
		long orderSequence = oRM.getSequence();
		
		// create a voting table object
		
		
		//    store the result
		//    check if we have at least RM/2 + 1 results that match
		//      if so, notify the waiting thread
		//    check if we have results from all RMs
		//      if so, check that they all match
		//      reset the failure counters of the matching servers and 
		//			increment the number of failures for the non-matching RM
		//      if an RM has (threshold) failures, notify the sequencer
		
	}	
}
