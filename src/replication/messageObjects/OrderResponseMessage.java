package replication.messageObjects;

/**
 * Represents the response to an order from a replica
 * @author Patrick
 */
public class OrderResponseMessage {
	private long sequence;
	private long replicaID;
	private boolean result;
	
	/**
	 * @param sequence The original sequence number of the request
	 * @param replicaID The ID (port number is a good choice) of the RM
	 * @param result The result of the transaction (true/false)
	 */
	public OrderResponseMessage(long sequence, long replicaID, boolean result) {
		this.sequence = sequence;
		this.replicaID = replicaID;
		this.result = result;
	}
	
	public long getSequence() {
		return this.sequence;
	}
	
	public long getReplicaID() {
		return this.replicaID;
	}
	
	public boolean getResult() {
		return this.result;
	}
}
