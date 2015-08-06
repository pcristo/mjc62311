package replication;

import java.util.List;
import java.util.Map;

import common.UdpServer;
import common.share.ShareOrder;
import replication.messageObjects.*;

public class FrontEnd extends UdpServer {
	private List<Integer> replicaManagerPorts;			// we assume all localhost, so no need to store addresses as well
	private Map<Long, Thread> unconfirmedRequests;
	private Map<Long, Thread> waitingRequests;
	
	@Override
	protected void incomingMessageHandler(Object o) {
		// TODO Auto-generated method stub
		
		if (o instanceof ShareOrder) {
			// client send a share order -- but does this need to be Java RMI/CORBA/WS?
			
			// send the request to sequencer
			// get back a sequence number *HOW TO MATCH SEQUENCE WITH REQUEST??*
			//       maybe send back the whole order with a sequence number for matching?
			// store sequence and Thread.currentThread() in a hashmap 
			// wait until notified or interrupted
			// send a response to the client
		}
		
		if (o instanceof SequencerResponseMessage) {
			// update the local table to include the sequence number
			// remove the item from the unconfirmed list
		}
		
		if (o instanceof OrderResponseMessage) {
			// what to do when responses are received
			
			// workflow:
			//    a message is received
			//    check which sequence number it is for
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
}
