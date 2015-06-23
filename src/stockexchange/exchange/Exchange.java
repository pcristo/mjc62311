package stockexchange.exchange;

import distribution.RMI.Client;
import business.BusinessInterface;
import common.Customer;
import common.logger.LoggerClient;
import common.logger.TimerLoggerClient;
import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * The exchange class acts as an intermediary between businesses and stock brokers. Brokers
 * make requests to purchase stock from the exchange, which then either sells existing shares
 * to the broker or requests new shares be issued from the business.
 * Please note that the exchange assumes that all share types within a business have the same
 * ticker symbol and price.
 */
public class Exchange {
    private static  final int COMMISSION_MARKUP = 10;
    private static  final int RESTOCK_THRESHOLD = 100;
    private static  int orderInt = 1100;
    protected static ShareSalesStatusList shareStatusSaleList;

    private Client<BusinessInterface> client = new Client<BusinessInterface>();

    /**
     * Business directory that maps stock symbols to remote interfaces
     */
    private Map<String, BusinessInterface> businessDirectory = new HashMap<String, BusinessInterface>();
    
    /**
     * Directory that maps stock symbols to stock prices
     */
    private Map<String, Float> priceDirectory = new HashMap<String, Float>();

    /**
     * Create exchange object by preparing the local list of available shares
     */
    public Exchange() {
        shareStatusSaleList = new ShareSalesStatusList();
        initializeShares();
    }

    /**
     * Registers a new business with the exchange, providing an initial price.
     * @param symbol to enlist
     * @param price to make shares available at
     * @throws NotBoundException 
     * @throws RemoteException 
     */
    public void registerBusiness(String symbol, float price) throws RemoteException, NotBoundException {
    	businessDirectory.put(symbol, getBusinessIFace(symbol));
    	priceDirectory.put(symbol, price);
    }
    
    /**
     * Delists a business from the exchange
     * @param symbol to delist
     * @throws Exception when the symbol is not listed
     */
    public void unregisterBusiness(String symbol) throws Exception {
    	// try to remove the stock from the business and price registers. If the symbol
    	// is not found, throw an exception
    	BusinessInterface bi = businessDirectory.remove(symbol);
    	if ((bi == null) || (priceDirectory.remove(symbol) == null)) 
    		throw new Exception("Symbol " + symbol + " not registered.");
    	
    	// TODO: business must be unbound from the client?
    }
    
    /**
     * Returns a business interface for making calls to the remote business server.
     * @param businessName looking for (specified as the stock symbol)
     * @return business object
     * @throws RemoteException
     * @throws NotBoundException
     */
    public BusinessInterface getBusinessIFace(String businessName) throws RemoteException, NotBoundException{
        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        return bindBusinessPort(businessName, 9095);
    }

    /**
     * Finds an available port to bind a business interface to, and returns the interface. 
     * The method will automatically increment to the next port if the one selected is not 
     * available.
     * @param businessName The symbol or name of the business
     * @param port to start trying to bind on. Must be between 9095 and 9099.
     * @return business object, null if failed
     */
    private BusinessInterface bindBusinessPort(String businessName, int port) throws RemoteException {
    	if(port > 9099 || port < 9095) {
            return null;
        }
        try {
            BusinessInterface server = client.getService("localhost", port, businessName);
            LoggerClient.log("Bound " + businessName + " on " + port);
            return server;
        } catch(NotBoundException nbe) {
            port++;
            return bindBusinessPort(businessName, port);
        }
    }


    /**
     * Getter : Business Directory
     * @return Map of all business in exchange
   
    public Map<String, String> getBusinessDirectory() {
        return businessDirectory;
    }  */

    /**
     * NOT IMPLEMENTED
     * Buy shares from a customer (ie Customer is SELLING shares)
     * @param shareItemList ShareList of share to purchase from customer
     * @param info Customer selling shares
     * @return
     */
    public ShareSalesStatusList buyShares(ShareList shareItemList, Customer info) {
        // TODO implement, not yet required by the specifications
        return shareStatusSaleList;
    }

    /**
     * Sell Shares to a Customer (ie Customer is BUYING shares)
     * @param shareItemList ShareList of shares to transact
     * @param info Customer object making the transaction
     * @return ShareSalesStatusList - Can access sold shares and available shares lists
     */
    public ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) {
        ShareItem soldShare;

        for  (ShareItem s : shareItemList.getLstShareItems())
        {
            soldShare = shareStatusSaleList.isShareAvailable(s);

            if (soldShare != null) {

                synchronized (soldShare) {

                    shareStatusSaleList.addToSoldShares(s, info);

                    if (payBusiness(soldShare) ) {
                        System.out.println(" \n " + "Shares paid for " + soldShare.getBusinessSymbol());
                    } else {
                        System.out.println(" \n " + "Shares not paid: " + soldShare.printShareInfo());
                    }
                }
            }
        }

        //Restock Share Lists
        this.restock();
        return  shareStatusSaleList;
    }

    /**
     * Given a customer, determine all of that customers' stocks
     * @param customer wanting stock information
     * @return list of customers' stocks
     */
    public List<ShareItem> getShares(Customer customer) {
        return shareStatusSaleList.getShares(customer);
    }

    /**
     * @return arrayList of all tickers listed on exchange
     */
    public ArrayList<String> getListing() {
        ArrayList<String> tickerList = new ArrayList<String>();

        for(String ticker : businessDirectory.keySet()) {
            tickerList.add(ticker);
        }

        return tickerList;
    }

    /**
     * @param businessName string of business TODO enum
     * @return the ticker commonly used to identify a company | Null if not found
     * @deprecated Should refer to businesses exclusively by their ticker symbol
     */
    public String getBusinessTicker(String businessName) {
        switch (businessName) {
		    case "google":
		        return "GOOG"; //google.getTicker();
		    case "microsoft":
		        return "MSFT"; //microsoft.getTicker();
		    case "yahoo":
		        return "YHOO"; //yahoo.getTicker();
		    default:
		        return null;
		}
    }

    /**
     * Used to issue common.share on Ecxhange start up
     */
    protected void initializeShares() {

        List<ShareItem> lstShares = new ArrayList<ShareItem>();

        //For Testing
        lstShares.add(new ShareItem("", "MSFT", ShareType.COMMON, 540.11f, 100));
        lstShares.add(new ShareItem("","MSFT.B",ShareType.CONVERTIBLE,523.32f,100));
        lstShares.add(new ShareItem("","MSFT.C",ShareType.PREFERRED,541.28f,100));
        lstShares.add(new ShareItem("","GOOG",ShareType.COMMON,540.11f,100));
        lstShares.add(new ShareItem("","GOOG.B",ShareType.CONVERTIBLE,532.23f,100));
        lstShares.add(new ShareItem("","GOOG.C",ShareType.PREFERRED,541.28f,100));
        lstShares.add(new ShareItem("","GOOG",ShareType.COMMON,540.11f,100));


        for(ShareItem shareItem : lstShares) {

            ShareItem addShareItem = this.issueSharesRequest(shareItem);

            if (addShareItem != null) {
                shareStatusSaleList.addToAvailableShares(addShareItem);
                shareStatusSaleList.addToNewAvShares(addShareItem);


            }
        }


    }

    /**
     *Method to restock any available common.share that is below the threshold
     */
    private void restock() {

        System.out.println(" \n " + "...... Restocking Shares .......");

        //Check Available stock amount
        for (ShareItem sItem : shareStatusSaleList.getAvailableShares()) {

            synchronized (sItem) {

                if (sItem.getQuantity() < RESTOCK_THRESHOLD) {

                    ShareItem newShares = this.issueSharesRequest(sItem);

                    if (newShares != null) {
                        sItem.setOrderNum(newShares.getOrderNum());
                        sItem.setQuantity(newShares.getQuantity());

                        shareStatusSaleList.addToNewAvShares(sItem);

                    }
                }
            }
        }
    }

    /**
     * Pays a business for shares that were previously issued but not paid for.
     * @param soldShare ShareItem requiring business payment
     * @return true if payment is processed
     */
	private boolean payBusiness(ShareItem soldShare) {
		// if the business is not registered, there is no interface, and null is returned
		BusinessInterface bi = businessDirectory.get(soldShare.getBusinessSymbol());
		if (bi == null) return false;
		
		try {
			return bi.recievePayment(soldShare.getOrderNum(),
					soldShare.getUnitPrice() * soldShare.getQuantity());
		} catch (Exception e) {
			System.out.println(" \n " + e.getMessage());
		}

		return false;
	}

    /**
     * Request a business to issue shares
     * @param sItem ShareItem to be issued
     * @return ShareItem (null will be returned if the transaction fails)
     */
	private ShareItem issueSharesRequest(ShareItem sItem) {
		Boolean sharesIssued = false;

		BusinessInterface bi = businessDirectory.get(sItem.getBusinessSymbol());
		if (bi == null) return null;

		String orderNum = generateOrderNumber();

		synchronized (orderNum) {
			try {
				sharesIssued = bi.issueShares(new ShareOrder(orderNum, 
						"not applicable", sItem.getBusinessSymbol(), sItem.getShareType(), 
						sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));
			} catch (Exception e) {
				System.out.println(" \n " + e.getMessage());
			}
		}

		if (sharesIssued) {
			ShareItem newShareItem = new ShareItem(orderNum,sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD);
			return newShareItem;
		}

		return null;
	}


    /**
     * Method to generate unique sequential order number for issue common.share
     */
    private synchronized String generateOrderNumber() {
        orderInt = orderInt + 1;
        String orderNumber = Integer.toString(orderInt);
        return orderNumber;
    }

}