package business;

import common.share.ShareOrder;
import common.share.ShareType;

import java.io.Serializable;

/**
 * A class to store a record of an order, including whether or not it is paid
 *
 */
public class OrderRecord extends ShareOrder implements Serializable {
	private static final long serialVersionUID = 1L;		// required for serialization
	private boolean isPaid;
	
	/**
	 * Default constructor needed for serialization to XML. Do not call. 
	 */
	public OrderRecord() {
		super(null, null, null, ShareType.PREFERRED, 0, 0, 0);
	}


	/**
	 * Constructor
	 *
	 * @param sO to adapt onto
	 * @param isPaid flag indicating if order has been paid for
	 */
	public OrderRecord(ShareOrder sO, boolean isPaid) {
		super(sO.getOrderNum(), sO.getBrokerRef(), sO.getBusinessSymbol(), sO.getShareType(),sO.getUnitPrice(),
				sO.getQuantity(), sO.getUnitPriceOrder());
		setPaid(isPaid);
	}

	/**
	 *
	 * @return true if order is paid, false otherwise
	 */
	public boolean isPaid() {
		return isPaid;
	}

	/**
	 *
	 * @param isPaid boolean flag representing order payment state
	 */
	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

}
