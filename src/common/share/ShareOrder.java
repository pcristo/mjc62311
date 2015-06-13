package common.share;

import java.io.Serializable;

/**
 * A class to create an order for shares
 * @author patrick
 */
public class ShareOrder extends Share implements Serializable  {
	private static final long serialVersionUID = 1L;	// required for serialization
	private String orderNum;
	private String brokerRef;
	private int quantity;
	private float unitPriceOrder;
	
	/**
	 * Constructor for ShareOrder object
	 * @param orderNum
	 * @param brokerRef
	 * @param businessSymbol
	 * @param shareType
	 * @param unitPrice
	 * @param quantity
	 * @param unitPriceOrder
	 */
	public ShareOrder(String orderNum, String brokerRef, String businessSymbol, ShareType shareType, 
			float unitPrice, int quantity, float unitPriceOrder) {
		super(businessSymbol, shareType, unitPrice);
		setOrderNum(orderNum);
		setBrokerRef(brokerRef);
		setQuantity(quantity);
		setUnitPriceOrder(unitPriceOrder);		
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getUnitPriceOrder() {
		return unitPriceOrder;
	}

	public void setUnitPriceOrder(float unitPrice) {
		this.unitPriceOrder = unitPrice;
	}	
}
