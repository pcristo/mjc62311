package replication.messageObjects;

import java.io.Serializable;

/**
 * This message should be sent by the sequencer in response to an
 * order from the Front End. It provides the Front End with the correct
 * sequence ID, so that the Front End can later identify the response
 * from the RMs
 * @author Patrick
 */
public class SequencerResponseMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private long tag;
	private long sequence;
	
	/**
	 * @param tag The value the order was tagged with.
	 * @param sequence The correct sequence number assigned to the order
	 */
	public SequencerResponseMessage(long tag, long sequence) {
		this.tag = tag;
		this.sequence = sequence;
	}
	
	public long getTag() {
		return this.tag;
	}
	
	public long getSequence(){
		return this.sequence;
	}
}
