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
	protected void incomingMessageHandler(Object o) {
		if (o instanceof OrderMessage) {
			// TODO: Handle a new order
		}
		
		if (o instanceof OrderResponseMessage) {
			// TODO: Handle order responses
		}
		
		if (o instanceof RegisterRmMessage) {
			// TODO: Register an RM
		}
		
		if (o instanceof UnregisterRmMessage) {
			// TODO: Unregister an RM
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
