package business;

import logger.LoggerClient;
import share.Share;
import share.ShareOrder;
import share.ShareType;
import util.Config;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for businesses to process transactions/manage shares
 * 
 * @author patrick
 */
public class Business implements Serializable, BusinessInterface {
	private static final long serialVersionUID = 1L;
	private static final String ORDER_RECORD_FILENAME =  Config.getInstance().getAttr("businessXmlLog");
	private List<Share> sharesList = new ArrayList<Share>();
	private Object recordLock = new Object(); 

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
			log("Business " + identifier + " created successfully.");
		} catch (IOException ioe) {
			log("Failed to create business " + identifier + ": " + ioe.getMessage());
		}
	}

	/**
	 * @return @return the ticker commonly used to identify a company
	 */
	public String getTicker() throws RemoteException {
		// all shares must have the same symbol, but may have different 'extensions'
		// return the common part of the symbol here:

		// Some share types don't have extension in that case return whole symbol
		// TODO: Validate this is required? Is an error thrown if no . found?
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
	public boolean issueShares(ShareOrder aSO) {
		// fetch the share that is relevant to this order
		Share listedShare = getShareInfo(aSO.getShareType());

		// if no valid listed share was found, return false
		if (listedShare == null) {
			log("No valid share found for " + aSO.getShareType() + " (broker ref " + aSO.getBrokerRef() + ")");			
			return false;
		}

		// if the order price lower than the current value, return false
		if (aSO.getUnitPriceOrder() < listedShare.getUnitPrice()) {
			log("Order price less than minimum issue price " + " (broker ref " + aSO.getBrokerRef() + ")");
			return false;
		}

		// validate the order is for at least 1 share, otherwise return false
		if (aSO.getQuantity() <= 0) {
			log("Invalid number of shares requested " + " (broker ref " + aSO.getBrokerRef() + ")");
			return false;
		}

		// call authorizeShare as required
		int authorizations = (int) Math.floor(aSO.getQuantity() / 100);
		int remainder = aSO.getQuantity() % 100;
		for (int i = 0; i <= authorizations; i++)
			authorizeShare(aSO.getShareType(), 100);
		authorizeShare(aSO.getShareType(), remainder);

		// record to XML file
		try {
			saveRecord(aSO);
		} catch (FileNotFoundException e) {
			// Failed to write to the record... Return false
			e.printStackTrace();
			log("Error processing broker ref " + aSO.getBrokerRef() + ": "
					+ e.getMessage());
			return false;
		}

		// return true
		log(aSO.getQuantity() + " shares issued successfully for broker ref " + aSO.getBrokerRef());
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
		// TODO: As the xml record gets large, this method's performance will
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
		
		return true;
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
			Business.startRMIServer(google, googleName, 9095);
			Business.startRMIServer(yahoo, yahooName, 9096);
			Business.startRMIServer(msoft, msoftName, 9097);
		} catch(RemoteException rme) {
			LoggerClient.log("Remote Exception in Business Server: " + rme.getMessage());
		}
	}

	/**
	 *
	 * @param business interface to be bound
	 * @param businessName to bind to
	 * @param port to bind business to
	 * @throws RemoteException
	 */
	public static void startRMIServer(BusinessInterface business, String businessName, int port) throws RemoteException {

		//TODO remove this.  See updated Config class.
		//System.setProperty("java.security.policy", Config.getInstance().loadMacSecurityPolicy());

		System.setProperty("java.security.policy", Config.getInstance()
				.loadSecurityPolicy());

		// load security policy
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		// create local rmi registery
		LocateRegistry.createRegistry(port);

		// bind service to default port portNum
		BusinessInterface stub = (BusinessInterface) UnicastRemoteObject
					.exportObject(business, port);
		Registry registry = LocateRegistry.getRegistry(port);
		registry.rebind(businessName, stub);
		//System.out.println(businessName + " bound on " + port);
		LoggerClient.log(businessName + " server bound on " + port);
	}
	
	/**
	 * Logs a message to both the console and the logging server
	 * @param msg
	 */
	private void log(String msg) {
		System.out.println(msg);
		LoggerClient.log(msg, this.getClass().getName());
	}
}