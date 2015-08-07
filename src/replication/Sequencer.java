package replication;

import java.util.HashMap;
import java.util.List;

import common.UdpServer;
import common.util.Config;
import replication.messageObjects.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Sequencer extends UdpServer {
	private HashMap<Long, Integer> replicaManagers;
	private long sequence = 0;

	public Sequencer() {
		this.launch(Integer.parseInt(Config.getInstance().getAttr("SequencerPort")));
	}
	
	/**
	 * Method to handle messages received by the UDP server
	 */
	@Override
	protected void incomingMessageHandler(MessageEnvelope me) {
		switch (me.getType()) {
		case OrderMessage:
			OrderMessage om = me.getOrderMessage();
			long originalID = om.getSequenceID();
			sequence++;

			// send a reply to the front end providing the correct sequence #
			SequencerResponseMessage srs = 
					new SequencerResponseMessage(originalID, sequence);
			replyToFrontEnd(srs);
			
			// send the order to all registered RMs
			om.setSequenceID(sequence);
			multicastToRMs(om);
			break;
		case OrderResponseMessage:
			forwardToFrontEnd(me);
			break;
		case RegisterRmMessage:
			registerRM(me.getRegisterRmMessage().getReplicaID(),
					me.getRegisterRmMessage().getReplicaPort());
			break;
		case UnregisterRmMessage:
			unregisterRM(me.getUnregisterRmMessage().getReplicaID());
			break;
		default:
			break; 
		}
	}
	
	/**
	 * Registers a Replication Manager 
	 */
	private void registerRM(Long ID, Integer port) {
		replicaManagers.put(ID, port);

		MessageEnvelope me = new MessageEnvelope(new RegisterRmMessage(ID, port));
		forwardToFrontEnd(me);
	}
	
	/**
	 * Unregisters a Replication Manager
	 */
	private void unregisterRM(Long ID) {
		replicaManagers.remove(ID);
		
		MessageEnvelope me = new MessageEnvelope(new UnregisterRmMessage(ID));
		forwardToFrontEnd(me);
	}
	
	/**
	 * Multicasts a message to all Replication Managers
	 * @param o The message to broadcast
	 */
	private void multicastToRMs(OrderMessage om) {
		MessageEnvelope me = new MessageEnvelope(om);
		
		for (Long rmID : replicaManagers.keySet()) {
			this.send(me, "localhost", replicaManagers.get(rmID));
		}	
	}	
	
	/**
	 * Sends a response to the front end for an order, providing the front
	 * end with the correct sequence number for this transaction
	 * @param srs
	 * @return true if successful
	 */
	private boolean replyToFrontEnd(SequencerResponseMessage srs) {
		MessageEnvelope me = new MessageEnvelope(srs);
		return forwardToFrontEnd(me);
	}
	
	/**
	 * Forwards a message to the front end
	 * @param me
	 * @return true if successful
	 */
	private boolean forwardToFrontEnd(MessageEnvelope me) {
		return this.send(me, "localhost", Config.getInstance().getAttr("FrontEndPort"));
	}
}
