package business;

/**
 * A class for businesses to process transactions/manage shares
 * @author patrick
 */
public class Business {
	private static final String[] ACCEPTABLE_TYPES = {"common","preferred","convertible"};
	private Share[] businessRegistry;

	/**
	 * Constructor to create a business
	 * @param sourceData A CSV file that contains stock information
	 */
	public Business(String sourceData) {
		loadRegistry(sourceData);
	}

	public boolean issueShares(ShareOrder aSO) {
		// fetch the share that is relevant to this order
		Share listedShare = getShareInfo(aSO.getShareType());
		
		// if no valid listed share was found, return false
		if (listedShare == null) return false;
		
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
        saveRecord(aSO);
		
		// return true
		return true;
	}

	public Share getShareInfo(String aShareType) {	
		// flip through the registry searching for a share type that matches the request
		for(Share s : getRegistry())
			if (s.getShareType().equals(aShareType)) return s;
		
		// nothing found... return null
		return null;
	}

	public boolean recievePayment(String orderNum, float totalPrice) {
		// TODO: This is a stub
        return true;
	}

	/**
	 * Authorizes shares if they are the an acceptable type and valid quantity
	 * @param shareType The type of share
	 * @param quantity The number of shares
	 * @return true if authorized
	 */
	private boolean authorizeShare(String shareType, int quantity) {
		if (quantity > 100 || quantity <= 0) return false;

		boolean typeOK = false;
		for (String s : ACCEPTABLE_TYPES) {
			if (s.equals(shareType)) typeOK = true;
		}

		return typeOK;
	}

	private Share[] getRegistry() {
		return businessRegistry;
	}

	private void saveRecord(ShareOrder order) {
		// TODO: Save to XML file
	}
	
	/**
	 * Loads the list of available shares and their prices from file
	 * @param filename The file to load data from
	 */
	private void loadRegistry(String filename) {
		// TODO: Load from file
	}

}
