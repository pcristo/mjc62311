package business;

import Distribution.RMI.Server;
import logger.LoggerClient;
import share.Share;
import share.ShareOrder;
import share.ShareType;
import util.Config;

import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for businesses to process transactions/manage shares
 * 
 * @author patrick
 */
public class Business implements Serializable, BusinessInterface {
	private static final long serialVersionUID = 1L;
	private static final String ORDER_RECORD_FILENAME = Config.getInstance().getAttr("businessXmlLog");
	private List<Share> sharesList = new ArrayList<Share>();
	private List<OrderRecord> orderRecords = new ArrayList<OrderRecord>();
	private Object recordLock = new Object();

	private static Server<BusinessInterface> server = new Server<BusinessInterface>();

	
	/**
	 * Constructor to create a business
	 * 
	 * @param identifier
	 *            The name of the company to create a business object for
	 */
	public Business(String identifier) {
		try {
			// Dynamically load the file
			String filePath = Config.getInstance().getAttr("files") + "/";
			URL sourceURL = Thread.currentThread().getContextClassLoader()
					.getResource(filePath + identifier);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(sourceURL.openStream()));

			// reset the shares list
			this.sharesList = new ArrayList<Share>();

			// Process each line of the csv file
			String row;
			String[] column;
			while ((row = bufferedReader.readLine()) != null) {
				// split the line
				column = row.split(",");

				// confirm there are at least 3 values in the array
				if (column.length < 3) {
					bufferedReader.close();
					throw new IOException("The CSV file is not correctly formatted.");
				}

				// extract the share information and create a share object
				Share s = new Share(column[0], ShareType.valueOf(column[1]), Float.parseFloat(column[2]));

				// add the share to the list
				this.sharesList.add(s);
			}

			// Close the file
			bufferedReader.close();
			
			// log the activity
			log("Business " + identifier + " object created.");

		} catch (IOException ioe) {
			log("Failed to create business object " + identifier + ": " + ioe.getMessage());
		}
	}

	/**
	 * @return @return the ticker commonly used to identify a company
	 */
	public String getTicker() throws RemoteException {
		// all shares must have the same symbol, but may have different 'extensions'
		// return the common part of the symbol here:

		// Some share types don't have extension in that case return whole symbol
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
	 * @throws RemoteException 
	 */
	public boolean issueShares(ShareOrder aSO) throws RemoteException {
		// fetch the share that is relevant to this order
		Share listedShare = getShareInfo(aSO.getShareType());

		// if no valid listed share was found, return false
		if (listedShare == null) {
			log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol() + " " + aSO.getShareType() + 
					": No valid share found for " + aSO.getShareType() + " (order #" + aSO.getOrderNum() + ")");			
			return false;
		}

		// if the order price lower than the current value, return false
		if (aSO.getUnitPriceOrder() < listedShare.getUnitPrice()) {
			log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol()  + " " + aSO.getShareType() + 
					": Order price " + aSO.getUnitPriceOrder() + " is less than minimum issue price " + 
					aSO.getUnitPrice() + " (order #" + aSO.getOrderNum() + ")");
			return false;
		}

		// validate the order is for at least 1 share, otherwise return false
		if (aSO.getQuantity() <= 0) {
			log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol() + " " + aSO.getShareType() + 
					": Invalid number of shares requested (order #" + aSO.getOrderNum() + ")");
			return false;
		}
		
		// validate the order number is unique
		if (!validateOrderNumber(aSO.getOrderNum())) {
			log(getTicker() + " failed to issue shares of " + aSO.getBusinessSymbol()  + " " + aSO.getShareType() + 
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
		log(getTicker() + " successfully issued " + aSO.getQuantity() + " shares of " + aSO.getBusinessSymbol() + 
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
	 * Checks if a share type exists for this business, and returns the share
	 * info
	 * 
	 * @return a share corresponding to the type requested, or null if not
	 *         available
	 */
	public Share getShareInfo(ShareType shareType) {
		// flip through the registry searching for a share type that matches the
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
	 *            The type of share
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
	 * Getter to return an array of all the share types available for this
	 * business
	 * 
	 * @return An array of shares
	 */
	public List<Share> getSharesList() {
		return sharesList;
	}

	/**
	 * Saves an order of issued shares to an XML record
	 * 
	 * @param order
	 *            The order to write to file
	 * @throws FileNotFoundException
	 *             If the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason then a FileNotFoundException is
	 *             thrown.
	 * @deprecated Replaced by saveRecordToList()
	 */
	private void saveRecord(ShareOrder order) throws FileNotFoundException {
		// create the order record
		OrderRecord orderRecord = new OrderRecord(order, false);

		synchronized(recordLock) {
			// write to file
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(ORDER_RECORD_FILENAME, true)));
			e.writeObject(orderRecord); // append the new order to the record
			e.close();
		}
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
				log("Payment for order " + orderNum + " successful.");
				return true; 			// return
			}

		}
		
		// handle error cases
		if (!orderExists) {
			log("Payment for order " + orderNum + " failed. The order does not exist.");
			return false;		
		}
		
		if (!totalPriceOK) {
			log("Payment " + totalPrice  + " for order " + orderNum + " failed. The recorded "
					+ "total order price does not match: " + orderRecord.getQuantity() +
					" units sold at " + orderRecord.getUnitPriceOrder());
			return false;	
		}

		if (!isNotPaid) {
			log("Payment for order " + orderNum + " failed. The order has already been paid.");
			return false;		
		}
		
		log("Payment for order " + orderNum + " failed and no reason is known.");
		return false;
		
		// deprecated method using XML:
/*	    // As the xml record gets large, this method's performance will
		// drop off dramatically. A database implementation would be far more
		// efficient.
		
		List<OrderRecord> orderRecords = new ArrayList<OrderRecord>();

		// do not allow any other checks for received payment AND do not allow
		// any new issued shares to be recorded until this request is processed		
		synchronized(recordLock) {	

			// load all the orders from the xml file
			XMLDecoder d;
			try {
				d = new XMLDecoder(new BufferedInputStream(new FileInputStream(
						ORDER_RECORD_FILENAME)));
			} catch (FileNotFoundException e1) {
				// no file means no records means no match, return false
				return false;
			}
			boolean isDone = false;
			while (!isDone) {
				try {
					orderRecords.add((OrderRecord) d.readObject());
				} catch (ArrayIndexOutOfBoundsException e) {
					isDone = true;
				}
			}
			d.close();

			// check to see if there is a match that is not already paid
			boolean hasMatch = false;
			for (OrderRecord o : orderRecords) {
				if ((o.getOrderNum().equals(orderNum))
						&& ((o.getQuantity() * o.getUnitPriceOrder()) == totalPrice)
						&& (!o.isPaid())) {
					hasMatch = true; // match found, set match as true
					o.setPaid(true); // update the status to paid
					break; // no need to continue processing after a match is found
				}
			}

			// no match, return false
			if (!hasMatch)
				return false;

			// if match, save file, return true
			XMLEncoder e;
			try {
				e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(
						ORDER_RECORD_FILENAME)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return false;
			}
			for (OrderRecord o : orderRecords)
				e.writeObject(o);
			e.close();
		}
		
		return true;*/
	}

	/**
	 * Create business interfaces and bind them to ports 9095 - 9099
	 * @param args
	 */
	public static void main(String args[]) {
		//TODO business name in enum

		// Start google server
		String googleName = "google";
		String googleCsv = Config.getInstance().getAttr(googleName);
		BusinessInterface google = new Business(googleCsv);

		// Start yahoo server
		String yahooName = "yahoo";
		String yahooCsv = Config.getInstance().getAttr(yahooName);
		BusinessInterface yahoo = new Business(yahooCsv);

		// Start msoft server
		String msoftName = "microsoft";
		String msoftCsv = Config.getInstance().getAttr(msoftName);
		BusinessInterface msoft = new Business(msoftCsv);

		try {
			// Reserver port 9095 - 9099 for business services
			server.start(google, googleName, 9095);
			server.start(yahoo, yahooName, 9096);
			server.start(msoft, msoftName, 9097);

		} catch(RemoteException rme) {
			System.out.println(rme.getMessage());
			LoggerClient.log("Remote Exception in Business Server: " + rme.getMessage());
		}
	}

	
	/**
	 * Logs a message to both the console and the logging server
	 * @param msg
	 */
	private void log(String msg) {
		// System.out.println(msg);
		LoggerClient.log(msg, this.getClass().getName());
	}
}
