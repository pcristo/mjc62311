package replication.messageObjects;

import java.io.Serializable;

public class RegisterRmMessage implements Serializable {
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
