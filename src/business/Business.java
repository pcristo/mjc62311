package business;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for businesses to process transactions/manage shares
 * 
 * @author patrick
 */
public class Business {
	private static final String[] ACCEPTABLE_TYPES = { "common", "preferred", "convertible" };
	private static final String ORDER_RECORD_FILENAME = "orderRecord.xml";
	private Share[] businessRegistry;

	/**
	 * Constructor to create a business
	 * 
	 * @param sourceData
	 *            A CSV file that contains stock information
	 */
	public Business(String sourceData) {
		loadRegistry(sourceData);
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
		try {
			saveRecord(aSO);
		} catch (FileNotFoundException e) {
			// Failed to write to the record... Return false
			e.printStackTrace();
			return false;
		}

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
		for (Share s : getRegistry())
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
	private Share[] getRegistry() {
		return businessRegistry;
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
	
	/**
	 * Loads the list of available shares and their prices from file
	 * 
	 * @param filename
	 *            The file to load data from
	 */
	private void loadRegistry(String filename) {
		// TODO: Load from file
	}

}
;;