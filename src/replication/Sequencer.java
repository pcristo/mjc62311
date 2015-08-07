package replication;

import java.util.List;

import common.UdpServer;
import common.util.Config;
import replication.messageObjects.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Sequencer extends UdpServer {
	private List<Integer> replicaManagers;
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
			// TODO: Handle a new order
			break;
		case OrderResponseMessage:
			// TODO: Handle order responses
			break;
		case RegisterRmMessage:
			// TODO: Register an RM
			break;
		case UnregisterRmMessage:
			// TODO: Unregister an RM
			break;
		default:
			break; 
		}
	}
	
	/**
	 * Registers a Replication Manager 
	 */
	private void registerRM(Integer port) {
		replicaManagers.add(port);
		// TODO: also register with frontEnd!
	}
	
	/**
	 * Unregisters a Replication Manager
	 */
	private void unregisterRM(Integer port) {
		replicaManagers.remove(port);
		// TODO: also unregister from frontEnd!
	}
	
	/**
	 * Multicasts a message to all Replication Managers
	 * @param o The message to broadcast
	 */
	private void multicast(Object o) {
		throw new NotImplementedException();
	}	
}
