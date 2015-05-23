package business;

import java.io.Serializable;

/**
 * A class to store a record of an order, including whether or not it is paid
 * @author patrick
 *
 */
public class OrderRecord extends ShareOrder implements Serializable {
	private static final long serialVersionUID = 1L;		// required for serialization
	private boolean isPaid;
	
	public OrderRecord(ShareOrder sO, boolean isPaid) {
		super(sO.getOrderNum(), sO.getBrokerRef(), sO.getBusinessSymbol(), sO.getShareType(),sO.getUnitPrice(),
				sO.getQuantity(), sO.getUnitPriceOrder());
		setPaid(isPaid);
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

}
