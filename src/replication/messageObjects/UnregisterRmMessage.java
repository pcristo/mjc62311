package replication.messageObjects;

public class UnregisterRmMessage {
	private long replicaID;
	
	public UnregisterRmMessage(long replicaID) {
		this.replicaID = replicaID;
	}
	
	public long getReplicaID() {
		return this.replicaID;
	}
	
}
