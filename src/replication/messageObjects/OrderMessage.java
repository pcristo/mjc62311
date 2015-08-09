package replication.messageObjects;

import java.io.Serializable;

import common.Customer;
import common.share.ShareOrder;

/**
 * A message containing order information, including an ID. 
 * Message flow: FrontEnd -> Sequencer -> RMs
 */
public class OrderMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private long sequenceID;
	private ShareOrder aSO;
	private Customer customer;
	
	/**
	 * Constructor
	 * @param seq The sequence number (or a tag ID)
	 * @param order
	 */
	public OrderMessage(long seq, ShareOrder order, Customer customer) {
		this.sequenceID = seq;
		this.aSO = order;
		this.customer = customer;
	}

	public long getSequenceID() {
		return this.sequenceID;
	}
	
	public void setSequenceID(long id) {
		this.sequenceID = id;
	}
	
	public ShareOrder getShareOrder() {
		return this.aSO;
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
}
