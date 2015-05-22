//package business;
//
///**
// * A class for businesses to process transactions/manage shares
// * @author patrick
// */
//public class Business {
//	private static final String[] ACCEPTABLE_TYPES = {"common","preferred","convertible"};
//	private Share[] businessRegistry;
//
//	/**
//	 * Constructor to create a business
//	 * @param sourceData A CSV file that contains stock information
//	 */
//	public Business(String sourceData) {
//		loadRegistry(sourceData);
//	}
//
//	public boolean issueShares(ShareOrder aSO) {
//		// TODO: This is a stub
//	}
//
//	public Share getShareInfo(String aShareType) {
//		// TODO: This is a stub
//	}
//
//	public boolean recievePayment(String orderNum, float totalPrice) {
//		// TODO: This is a stub
//	}
//
//	/**
//	 * Authorizes shares if they are the an acceptable type and valid quantity
//	 * @param shareType The type of share
//	 * @param quantity The number of shares
//	 * @return true if authorized
//	 */
//	private boolean authorizeShare(String shareType, int quantity) {
//		if (quantity > 100) return false;
//
//		boolean typeOK = false;
//		for (String s : ACCEPTABLE_TYPES) {
//			if (s.equals(shareType)) typeOK = true;
//		}
//
//		return typeOK;
//	}
//
//	private Share[] getRegistry() {
//		return businessRegistry;
//	}
//
//	/**
//	 * Loads the list of available shares and their prices from file
//	 * @param filename The file to load data from
//	 */
//	private void loadRegistry(String filename) {
//		// TODO: Load from file
//	}
//
//}
