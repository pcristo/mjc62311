package replication;

import common.UdpServer;
import common.logger.LoggerClient;
import common.util.Config;
import replication.messageObjects.*;

import java.util.HashMap;

public class Sequencer extends UdpServer {
	private HashMap<Long, Integer> replicaManagers;
	private static Long sequence = (long) 0;

	public Sequencer() {
		replicaManagers = new HashMap<>();
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
			SequencerResponseMessage srs;
			long originalID = om.getSequenceID();
			
			synchronized(sequence) {
				sequence++;
				om.setSequenceID(sequence);
				srs = new SequencerResponseMessage(originalID, sequence);
			}

			// send a reply to the front end providing the correct sequence #
			replyToFrontEnd(srs);
			
			// send the order to all registered RMs
			multicastToRMs(om);
			break;
		case OrderResponseMessage:
			forwardToFrontEnd(me);
			break;
		case RegisterRmMessage:
			registerRM(me.getRegisterRmMessage().getReplicaID(),
					me.getRegisterRmMessage().getReplicaPort());
			LoggerClient.log("RM registered");
			break;
		case UnregisterRmMessage:
			unregisterRM(me.getUnregisterRmMessage().getReplicaID());
			break;
		case FailedRmMessage:	
			FailedRmMessage frm = me.getFailedRmMessage();
			
			synchronized(sequence) {
				sequence++;
				frm.setSequenceId(sequence);
			}
			
			multicastToRMs(frm);
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
	 * Multicasts an order message to all Replication Managers
	 * @param o The message to broadcast
	 */
	private void multicastToRMs(OrderMessage om) {
		MessageEnvelope me = new MessageEnvelope(om);
		
		for (Long rmID : replicaManagers.keySet()) {
			this.send(me, "localhost", replicaManagers.get(rmID));
		}	
	}	
	
	/**
	 * Multicasts a failed RM message to all Replication Managers
	 * @param o The message to broadcast
	 */
	private void multicastToRMs(FailedRmMessage frm) {
		MessageEnvelope me = new MessageEnvelope(frm);
		
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

	public static void main(String[] args) {
		Sequencer seq = new Sequencer();

	}
}
