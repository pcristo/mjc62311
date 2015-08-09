package replication.messageObjects;

import java.io.Serializable;

/**
 * Message sent from the Front End to the RMs via the sequencer to 
 * inform the RMs that one of them has failed. Message flow:
 * FrontEnd -> Sequencer -> RMs
 */
public class FailedRmMessage implements Serializable {
	private static final long serialVersionUID = 1L;
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
