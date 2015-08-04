package business;

import common.logger.LoggerClient;
import common.share.Share;
import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;
import stockQuotes.Company;
import stockQuotes.GoogleFinance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for businesses to process transactions/manage shares
 */
public class Business implements IBusiness, Serializable {
	private static final long serialVersionUID = 1L;
	private List<Share> sharesList = new ArrayList<Share>();
	private List<OrderRecord> orderRecords = new ArrayList<OrderRecord>();


	/**
	 * Constructor to create a business
	 * 
	 * @param identifier
	 *            The name of the company to create a business object for
	 */
	public Business(String identifier) {
		try {
			// Dynamically load the file
			String filePath = Config.getInstance().getAttr("files") + "/" +
					Config.getInstance().getAttr(identifier);
			URL sourceURL = Thread.currentThread().getContextClassLoader()
					.getResource(filePath);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(sourceURL.openStream()));

			// reset the shares list
			this.sharesList = new ArrayList<Share>();
			
			// Process each line of the csv file
			String row;
			String[] column;
			while ((row = bufferedReader.readLine()) != null) {
				column = row.split(",");

				if (column.length < 3) {
					bufferedReader.close();
					throw new IOException("The CSV file is not correctly formatted.");
				}
				
				GoogleFinance tickers = new GoogleFinance();
				Company company = new Company(column[0], new stockQuotes.Exchange(column[2]));
				
				String price = "";
				if (Config.getInstance().getAttr("GoogleFinanceEnabled").equals("true"))
					price = tickers.getStock(company);	
								
				// either Google Finance failed or the user disabled it. Randomly create a price.
				if(price.isEmpty()) {
					price = String.format("%.2f", Math.random() * 200 + 100);
				}
				Share s = new Share(column[0], ShareType.valueOf(column[1]), Float.parseFloat(price));

				this.sharesList.add(s);
				
				LoggerClient.log("  Price set: " + s.getBusinessSymbol() + 
						"/" + s.getShareType().toString() + 				
						" = " + s.getUnitPrice());
			}

			bufferedReader.close();
			
			LoggerClient.log("Business " + identifier + " object created.");

		} catch (IOException ioe) {
			LoggerClient.log("Failed to create business object " + identifier + ": " + ioe.getMessage());
		}
	}

	/**
	 * @return the ticker commonly used to identify a company
	 */
	public String getTicker()  {
		// all shares must have the same symbol, but may have different 'extensions'
		// return the common part of the symbol here:

		// Some common.share types don't have extension in that case return whole symbol
		String shareSymbol = sharesList.get(0).getBusinessSymbol();
		if (shareSymbol.contains(".")) {
			return sharesList.get(0).getBusinessSymbol().split(".")[0];
		} else {
			return shareSymbol;
		}
	}

	/**
	 * Checks if an order is valid, and if so, issues the requested number of
	 * shares
	 * 
	 * @param aSO
	 *            A ShareOrder to process
	 * @return true if successful, false if failed
	 */
	public boolean issueShares(ShareOrder aSO)  {
		// fetch the common.share that is relevant to this order
		Share listedShare = getShareInfo(aSO.getShareType());

		// if no valid listed common.share was found, return false
		if (listedShare == null) {
			LoggerClient.log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol() + " " + aSO.getShareType() +
					": No valid share found for " + aSO.getShareType() + " (order #" + aSO.getOrderNum() + ")");
			return false;
		}

		// if the order price lower than the current value, return false

		if (aSO.getUnitPriceOrder() < listedShare.getUnitPrice()) {
			LoggerClient.log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol() + " " + aSO.getShareType() +
					": Order price " + aSO.getUnitPriceOrder() + " is less than minimum issue price " +
					listedShare.getUnitPrice() + " (order #" + aSO.getOrderNum() + ")");
			return false;
		}

		// validate the order is for at least 1 common.share, otherwise return false
		if (aSO.getQuantity() <= 0) {
			LoggerClient.log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol() + " " + aSO.getShareType() +
					": Invalid number of shares requested (order #" + aSO.getOrderNum() + ")");
			return false;
		}
		
		// validate the order number is unique
		if (!validateOrderNumber(aSO.getOrderNum())) {
			LoggerClient.log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol() + " " + aSO.getShareType() +
					": The order number " + aSO.getOrderNum() + " already exists");
			return false;
		}		

		// call authorizeShare as required
		int authorizations = (int) Math.floor(aSO.getQuantity() / 100);
		int remainder = aSO.getQuantity() % 100;
		for (int i = 0; i <= authorizations; i++)
			authorizeShare(aSO.getShareType(), 100);
		authorizeShare(aSO.getShareType(), remainder);

		// record to local memory file
		saveRecordToList(aSO);

		// return true
		LoggerClient.log(getTicker() + " successfully issued " + aSO.getQuantity() + " shares of " + aSO.getBusinessSymbol() +
				" " + aSO.getShareType() + " (order #" + aSO.getOrderNum() + ")");
		return true;
	}

	/**
	 * Checks if an order number is unique, returns true if so
	 * @param orderNum The order number to check
	 * @return true if unique, false if not
	 */
	private boolean validateOrderNumber(String orderNum) {
		for (OrderRecord o : orderRecords)
			if (o.getOrderNum().equals(orderNum)) return false;
		
		return true;
	}

	/**
	 * Checks if a common.share type exists for this business, and returns the common.share
	 * info
	 * 
	 * @return a common.share corresponding to the type requested, or null if not
	 *         available
	 */
	public Share getShareInfo(ShareType shareType) {
		// flip through the registry searching for a common.share type that matches the
		// request

		for (Share s : getSharesList())
			if (s.getShareType().equals(shareType))
				return s;

		// nothing found... return null
		return null;
	}

	/**
	 * Authorizes shares if they are the an acceptable type and valid quantity
	 * 
	 * @param shareType
	 *            The type of common.share
	 * @param quantity
	 *            The number of shares
	 * @return true if authorized
	 */
	private boolean authorizeShare(ShareType shareType, int quantity) {
		if (quantity > 100 || quantity <= 0)
			return false;

		boolean typeOK = false;
		for (ShareType s : ShareType.values()) {
			if (s.equals(shareType))
				typeOK = true;
		}

		return typeOK;
	}

	/**
	 * Getter to return an array of all the common.share types available for this
	 * business
	 * 
	 * @return An array of shares
	 */
	public List<Share> getSharesList() {
		return sharesList;
	}

	/**
	 * Saves an order of issued shares to local memory
	 * 
	 * @param order The order to save
	 */
	private void saveRecordToList(ShareOrder order) {
		synchronized(orderRecords) {
			orderRecords.add(new OrderRecord(order, false));
		}
	}
	
	/**
	 * Checks the order record for an exact match, and if found, updates the
	 * record to indicate the order is paid and returns true. Otherwise returns
	 * false.
	 * 
	 * @param orderNum
	 *            The order number to look for
	 * @param totalPrice
	 *            The total price of the transaction (quantity * order unit
	 *            price)
	 * @return true if a match found, false if not or if the match has already
	 *         been paid
	 */
	public boolean recievePayment(String orderNum, float totalPrice) {
		boolean orderExists = false;
		boolean totalPriceOK = false;
		boolean isNotPaid = false;
		OrderRecord orderRecord = null;
		
		// check to see if there is a match that is not already paid
		synchronized(orderRecords) {			
			for (OrderRecord o : orderRecords) {				
				if (o.getOrderNum().equals(orderNum)) {
					orderExists = true;

					if ((o.getQuantity() * o.getUnitPriceOrder()) == totalPrice)
						totalPriceOK = true;

					if (!o.isPaid())
						isNotPaid = true;

					orderRecord = o;

					break; 				// order was found, stop searching
				}				
			}

			// handle success case
			if (isNotPaid && totalPriceOK && orderExists) {
				orderRecord.setPaid(true);
				LoggerClient.log("Payment to business for order " + orderNum + " successful.");
				return true; 			// return
			}

		}
		
		// handle error cases
		if (!orderExists) {
			LoggerClient.log("Payment to business for order " + orderNum + " failed. The order does not exist.");
			return false;		
		}
		
		if (!totalPriceOK) {
			LoggerClient.log("Payment " + totalPrice + " to business for order " + orderNum + " failed. The recorded "
					+ "total order price does not match: " + orderRecord.getQuantity() +
					" units sold at " + orderRecord.getUnitPriceOrder());
			return false;	
		}

		if (!isNotPaid) {
			LoggerClient.log("Payment to business for order " + orderNum + " failed. The order has already been paid.");
			return false;		
		}

		LoggerClient.log("Payment to business for order " + orderNum + " failed and no reason is known.");
		return false;

	}

}
