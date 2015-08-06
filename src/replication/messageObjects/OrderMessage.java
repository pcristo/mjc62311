package replication.messageObjects;

import java.io.Serializable;

import common.share.ShareOrder;

public class OrderMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private long sequenceID;
	private ShareOrder aSO;
	
	/**
	 * Constructor
	 * @param seq The sequence number (or a tag ID)
	 * @param order
	 */
	OrderMessage(long seq, ShareOrder order) {
		this.sequenceID = seq;
		this.aSO = order;
	}

	public long getSequenceID() {
		return this.sequenceID;
	}
	
	public ShareOrder getShareOrder() {
		return this.aSO;
	}
	
}
