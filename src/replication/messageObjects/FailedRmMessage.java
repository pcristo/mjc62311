package replication.messageObjects;

public class FailedRmMessage {
	private Long failedRmId;
	private Long sequenceId = (long) -1;
	
	public FailedRmMessage(Long id) {
		this.failedRmId = id;
	}
	
	public Long getFailedRmId() {
		return this.failedRmId;
	}
	
	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	public Long getSequenceId() {
		return this.sequenceId;
	}
}
