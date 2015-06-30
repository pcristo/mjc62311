package business;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import business_domain.interface_businessPOA;
import common.logger.LoggerServer;
import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;
import stockexchange.exchange.Exchange;

/**
 * This class provides CORBA servant implementation for the business class.
 * 
 * @author patrick
 */
public class BusinessServant extends interface_businessPOA {
	private Business business;
	private String businessSymbol = "";

	/**
	 * Sets the business symbol handled by this servant. Can only be set once.
	 * 
	 * @param businessSymbol
	 */
	public void setBusinessSymbol(String businessSymbol) {
		if (this.businessSymbol.equals("")) {
			this.businessSymbol = businessSymbol;
		}

	}

	/**
	 * Gets the price of a share
	 * @return price of share
	 */
	public float getUnitPrice() {
		// All share types are assumed to have the same price, so return 
		// price of a common share
		return business.getShareInfo(ShareType.COMMON).getUnitPrice();
	}
	
	/*
	 * IMPLEMENTATION OF THE BUSINESS STUB
	 */

	public String getTicker() {
		return business.getTicker();
	}

	public boolean issueShares(String orderNum, String brokerRef,
			String businessSymbol, int shareType, float unitPrice,
			int quantity, float unitPriceOrder) {
		ShareType aST = ShareType.values()[shareType];
		ShareOrder aSO = new ShareOrder(orderNum, brokerRef, businessSymbol,
				aST, unitPrice, quantity, unitPriceOrder);
		return business.issueShares(aSO);
	}

	public boolean recievePayment(String orderNum, float totalPrice) {
		return business.recievePayment(orderNum, totalPrice);
	}

}
