package replication.frontEnd;

import java.util.List;

import common.UdpServer;
import common.share.ShareOrder;

public class frontEnd extends UdpServer {
	private List<Integer> replicaManagerPorts;			// we assume all localhost, so no need to store addresses as well

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
		
		if (o instanceof Object) {
			// what to do when responses are received
			
			// workflow:
			//    a message is received
			//    check which sequence number it is for
			//    store the result
			//    check if we have at least 2 results that match
			//      if so, notify the waiting thread
			//    check if we have 3 total results
			//      if so, check that they all match
			//      reset the failure counters of the matching servers and increment the number of failures for the non-matching RM
			//      if an RM has (threshold) failures, notify the sequencer
		}
	}
	
	
	
	
}
