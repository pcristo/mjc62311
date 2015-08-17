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
			LoggerClient.log("Recieved order message");
			OrderMessage om = me.getOrderMessage();
			SequencerResponseMessage srs;
			long originalID = om.getSequenceID();
			
			synchronized(sequence) {
				sequence++;
				om.setSequenceID(sequence);
				srs = new SequencerResponseMessage(originalID, sequence);
			}


			// send the order to all registered RMs
			LoggerClient.log("Sending order message to RMs");
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
		// Check if port is not already in use
		for(int p : replicaManagers.values()) {
			if (p == port) {
				LoggerClient.log("Unable to register RM, port in use");
				return;
			}
		}

		// Track RM
		replicaManagers.put(ID, port);

		LoggerClient.log("RM registered on port " + port + " at id " + ID.toString());

		// Tell RM's that a new RM has been added
		MessageEnvelope me = new MessageEnvelope(new PortMessage(replicaManagers));


		synchronized (replicaManagers) {
			for (int p : replicaManagers.values()) {
				LoggerClient.log("Updating " + p + " on tracked RMs");
				send(me, "localhost", p);
			}
		}
	}
	
	/**
	 * Unregisters a Replication Manager
	 */
	private void unregisterRM(Long ID) {
		synchronized (replicaManagers) {
			if(replicaManagers.get(ID) != null) {
				replicaManagers.remove(ID);
				LoggerClient.log("Unregistered RM at id " + ID.toString());
			} else{
				// A thread beat you in removing this RM, so we return
				LoggerClient.log("Unable to remove RM at id " + ID.toString() + " it didn't exist, probably already removed");
				return;
			}
		}
		// Start up a new RM
		new Thread(() -> {
            new ReplicaManager().start();
        }).start();


	}
	
	/**
	 * Multicasts an order message to all Replication Managers
	 * @param o The message to broadcast
	 */
	private void multicastToRMs(OrderMessage om) {
		MessageEnvelope me = new MessageEnvelope(om);
		for (Long rmID : replicaManagers.keySet()) {
			LoggerClient.log("Multicasting to rm on port: " + replicaManagers.get(rmID));
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
	 * Forwards a message to the front end
	 * @param me
	 * @return true if successful
	 */
	private boolean forwardToFrontEnd(MessageEnvelope me) {
		return this.send(me, "localhost", Config.getInstance().getAttr("FrontEndPort"));
	}

	/**
	 * Start sequencer
	 * @param args
	 */
	public static void main(String[] args) {
		Sequencer seq = new Sequencer();

	}
}
