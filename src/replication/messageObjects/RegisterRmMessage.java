package replication.messageObjects;

import java.io.Serializable;

/**
 * Informs the sequencer and FrontEnd that a new RM has 
 * been created and wishes to receive multicast messages and 
 * vote on requests.
 * Message flow: RM -> Sequencer -> FrontEnd
 */
public class RegisterRmMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private long replicaID;
	private int replicaPort;
	private String replicaAddress;
	
	public RegisterRmMessage(long replicaID, int replicaPort) {
		this.replicaID = replicaID;
		this.replicaPort = replicaPort;
		this.replicaAddress = "localhost";
	}

	public long getReplicaID() {
		return this.replicaID;
	}
	
	public int getReplicaPort() {
		return this.replicaPort;
	}
	
	public String getAddress() {
		return this.replicaAddress;
	}
}
