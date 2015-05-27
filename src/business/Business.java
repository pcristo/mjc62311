package business;

import share.Share;
import share.ShareOrder;
import util.Config;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.rmi.registry.LocateRegistry.createRegistry;

/**
 * A class for businesses to process transactions/manage shares
 * 
 * @author patrick
 */
public class Business implements Serializable, BusinessInterface {
	private static final long serialVersionUID = 1L;
	// TODO PLEASE MOVE THIS TO ENUM

	private static final String[] ACCEPTABLE_TYPES = { "common", "preferred", "convertible" };
	private static final String ORDER_RECORD_FILENAME = "orderRecord.xml";
	private List<Share> sharesList = new ArrayList<Share>();


	protected String companyTicker = null;
	protected Map<String, String> allTickers = null;

	/**
	 * Constructor to create a business
	 * 
	 * @param filename
	 *            A CSV file that contains stock information
	 */
	public Business(String filename) {

		allTickers = new HashMap<String, String>();

		try {
			// Dynamically load the file
			String filePath = Config.getInstance().getAttr("files") + "/";
			URL sourceURL = Thread.currentThread().getContextClassLoader().getResource(filePath + filename);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sourceURL.openStream()));

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
				Share s = new Share(column[1], column[2], Float.parseFloat(column[3]));

				// TODO remove from loop somehow
				companyTicker = column[0];
				allTickers.put(column[2], column[1]);

				// add the share to the list
				this.sharesList.add(s);
			}

			// Close the file
			bufferedReader.close();
		} catch (IOException ioe ) {
			System.out.println(ioe.getMessage());
		}

	}


	/**
	 *
	 * @return company ticker used to identify company
	 */
	public String getCompanyTicker() {
		return companyTicker;
	}

	/**
	 *
	 * @return all company tickers (different stock types have different tickers)
	 */
	public Map<String, String> getAllTickers() {
		return allTickers;
	}

	/**
	 *
	 * @param type of stock to get
	 * @return ticker of type
	 */
	public String getTicker(String type) {
		return allTickers.get(type);
	}

	/**
	 * Checks if an order is valid, and if so, issues the requested number of shares
	 * 
	 * @param aSO
	 *            A ShareOrder to process
	 * @return true if successful, false if failed
	 */
	public boolean issueShares(ShareOrder aSO) {
		// fetch the share that is relevant to this order
		Share listedShare = getShareInfo(aSO.getShareType());

		// if no valid listed share was found, return false
		if (listedShare == null)
			return false;

		// if the order price lower than the current value, return false
		if (aSO.getUnitPrice() < listedShare.getUnitPrice())
			return false;

		// validate the order is for at least 1 share, otherwise return false
		if (aSO.getQuantity() <= 0)
			return false;

		// call authorizeShare as required
		int authorizations = (int) Math.floor(aSO.getQuantity() / 100);
		int remainder = aSO.getQuantity() % 100;
		for (int i = 0; i <= authorizations; i++)
			authorizeShare(aSO.getShareType(), 100);
		authorizeShare(aSO.getShareType(), remainder);

		// record to XML file
		/*try {
			saveRecord(aSO);
		} catch (FileNotFoundException e) {
			// Failed to write to the record... Return false
			e.printStackTrace();
			return false;
		}*/

		// return true
		return true;
	}


	/**
	 * Checks if a share type exists for this business, and returns the share
	 * info
	 * 
	 * @return a share corresponding to the type requested, or null if not
	 *         available
	 */
	public Share getShareInfo(String aShareType) {
		// flip through the registry searching for a share type that matches the request
		for (Share s : getSharesList())
			if (s.getShareType().equals(aShareType))
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
	private boolean authorizeShare(String shareType, int quantity) {
		if (quantity > 100 || quantity <= 0)
			return false;

		boolean typeOK = false;
		for (String s : ACCEPTABLE_TYPES) {
			if (s.equals(shareType))
				typeOK = true;
		}

		return typeOK;
	}

	/**
	 * Getter to return an array of all the share types available for this business
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

		// write to file
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(ORDER_RECORD_FILENAME, true)));
		e.writeObject(orderRecord); // append the new order to the record
		e.close();
	}

	/**
	 * Checks the order record for an exact match, and if found, updates the record to indicate the order is paid and returns true. Otherwise returns false.
	 * @param orderNum The order number to look for
	 * @param totalPrice The total price of the transaction (quantity * order unit price)
	 * @return true if a match found, false if not or if the match has already been paid
	 */
	public boolean recievePayment(String orderNum, float totalPrice) {
		List<OrderRecord> orderRecords = new ArrayList<OrderRecord>();
		
		// load all the orders from the xml file
		XMLDecoder d;
		try {
			d = new XMLDecoder(new BufferedInputStream(new FileInputStream(ORDER_RECORD_FILENAME)));
		} catch (FileNotFoundException e1) {
			// no file means no records means no match, return false	
			return false;
		}
		boolean isDone = false;
		while (!isDone) {
				try {
					orderRecords.add((OrderRecord) d.readObject());
				}
				catch (ArrayIndexOutOfBoundsException e){
					isDone = true;
				}
		}
		d.close();
		
		// check to see if there is a match that is not already paid
		boolean hasMatch = false;
		for (OrderRecord o : orderRecords) {
			if ((o.getOrderNum().equals(orderNum)) && 
					((o.getQuantity() * o.getUnitPriceOrder()) == totalPrice) &&
					(!o.isPaid())) {
				hasMatch = true;		// match found, set match as true
				o.setPaid(true);		// update the status to paid
				break;					// no need to continue processing after a match is found
			}			
		}
		
		// no match, return false
		if (!hasMatch) return false;
		
		// if match, save file, return true
		XMLEncoder e;
		try {
			e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(ORDER_RECORD_FILENAME)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		for (OrderRecord o : orderRecords) 
			e.writeObject(o);		
		e.close();
				
		return true;
	}



	public static void main(String args[]) {
		Business.startRMIServer("businesService", 1098);
	}


	public static void startRMIServer(String serviceName, int portNum) {

		System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

		//load security policy
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {

			String microsoftCsv = Config.getInstance().getAttr("microsoft");
			BusinessInterface service = new Business(microsoftCsv);
			//create local rmi registery
			createRegistry(portNum);

			//bind service to default port portNum
			BusinessInterface stub =
					(BusinessInterface) UnicastRemoteObject.exportObject(service, portNum);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(serviceName, stub);
			System.out.println(serviceName + " bound on " + portNum);
		} catch (Exception e) {
			System.err.println("broker service creation exception:");
			e.printStackTrace();
		}
	}

}