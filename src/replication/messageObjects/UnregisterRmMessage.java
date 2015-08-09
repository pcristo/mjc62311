package replication.messageObjects;

/**
 * A message telling the sequencer and FrontEnd that an RM has
 * been removed from the network and should no longer receive 
 * multicast messages, and should no longer be allowed to vote 
 */
public class UnregisterRmMessage {
	private long replicaID;
	
	public UnregisterRmMessage(long replicaID) {
		this.replicaID = replicaID;
	}
	
	public long getReplicaID() {
		return this.replicaID;
	}
	
}
