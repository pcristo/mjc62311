package business;

/**
 * A class to create an order for shares
 * @author patrick
 */
public class ShareOrder {
	private String orderNum;
	private String brokerRef;
	private Share share;
	private int quantity;
	private float unitPrice;
	
	/**
	 * Constructor for ShareOrder object
	 * @param aOrderNum
	 * @param aBrokerRef
	 * @param aShare
	 * @param aQuantity
	 * @param aUnitPrice
	 */
	public ShareOrder(String orderNum, String brokerRef, Share share, int quantity, float unitPrice) {
		setOrderNum(orderNum);
		setBrokerRef(brokerRef);
		setShare(share);
		setQuantity(quantity);
		setUnitPrice(unitPrice);
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getBrokerRef() {
		return brokerRef;
	}

	public void setBrokerRef(String brokerRef) {
		this.brokerRef = brokerRef;
	}

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}	
}
