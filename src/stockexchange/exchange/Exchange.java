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
import java.util.*;

/** 
 * The exchange class acts as an intermediary between businesses and stock brokers. Brokers
 * make requests to purchase stock from the exchange, which then either sells existing shares
 * to the broker or requests new shares be issued from the business.
 * Please note that the exchange assumes that all share types within a business have the same
 * ticker symbol and price.
 */
public class Exchange {
    private static  final int COMMISSION_MARKUP = 10;
    private static  final int RESTOCK_THRESHOLD = 500;
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
    protected Map<String, Float> priceDirectory = new HashMap<String, Float>();

    public BusinessInterface yahoo;
    public BusinessInterface microsoft;
    public BusinessInterface google;

    /**
     * Create exchange object by preparing the local list of available shares
     */
    public Exchange() {

        try {
            //google = getBusiness("google");
            //yahoo = getBusiness("yahoo");
            //microsoft = getBusiness("microsoft");
        }
        catch(Exception e) {

            System.out.println(e.getMessage());
        }
        //shareStatusSaleList = new ShareSalesStatusList();
        //initializeShares();
    }

    /**
     *
     * @param businessName looking for
     * @return business object
     * @throws RemoteException
     * @throws NotBoundException
     */
    public BusinessInterface getBusiness(String businessName) throws RemoteException, NotBoundException{
        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        return findBusiness(businessName, 9095);
    }

    /**
     * Ports 9095 to 9099 reserved for business server
     *
     * @param businessName looking for
     * @param port looking for business on
     * @return business object
     */
    private BusinessInterface findBusiness(String businessName, int port) throws RemoteException {
        if(port > 9099) {
            return null;
        }
        try {
            BusinessInterface server = client.getService("localhost", port, businessName);
            LoggerClient.log("Bound " + businessName + " on " + port);
            return server;
        } catch(NotBoundException nbe) {
            port++;
            return findBusiness(businessName, port);
        }
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

        ShareItem soldShare = null;

        for  (ShareItem s : shareItemList.getLstShareItems())
        {
            int requestedShares = s.getQuantity();
            int toComplete = requestedShares;

            //Is business registered in Exchange
            if (this.priceDirectory.get(s.getBusinessSymbol()) != null){

                //Business Shares Listing
                List<ShareItem> lstShares = shareStatusSaleList.newAvShares.get(s.getBusinessSymbol());

                synchronized(lstShares)
                {
                    //Is quantity on hand
                    if (this.getShareQuantity(lstShares, s.getShareType()) >= s.getQuantity() ) {

                        for (ShareItem sItem : lstShares) {

                            //Populate new Sold Share
                            if (soldShare == null) {

                                soldShare = new ShareItem("",
                                        sItem.getBusinessSymbol(),
                                        sItem.getShareType(),
                                        sItem.getUnitPrice(),
                                        requestedShares);
                            }

                            //Just iterate through the companies share of a specific share type
                            if (sItem.getShareType() == s.getShareType()) {

                                if(toComplete == requestedShares && sItem.getQuantity() >= requestedShares) {

                                    //Reduce the available amount
                                    sItem.reduceQuantity(requestedShares);
                                    break;

                                } else {
                                    //Share will be coming from more then one order
                                    if (toComplete > 0) {

                                        if (sItem.getQuantity() >= toComplete) {
                                            sItem.reduceQuantity(toComplete);
                                            toComplete = 0;
                                        } else {
                                            toComplete -= sItem.getQuantity();
                                            sItem.reduceQuantity(sItem.getQuantity());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //Pay the companies once shares are at 0 in the newAvailable Shares
                    List<String> lstOrders = new ArrayList<String>();

                    for(ShareItem sItem : lstShares) {

                        if (sItem.getQuantity() == 0) {
                            lstOrders.add(sItem.getOrderNum());
                        }
                    }

                    //Pay all orders if needed
                    if (lstOrders.size()>0){
                        payBusiness(lstOrders);
                    }

                }

                //TODO: Review this synchronization
                synchronized (soldShare) {

                    shareStatusSaleList.addToSoldShares(s, info);
                }
            }

            //Restock Share Lists
            this.restock();
        }


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
        lstShares.add(new ShareItem("", "GOOG.C", ShareType.PREFERRED, 541.28f, 100));
        lstShares.add(new ShareItem("", "GOOG", ShareType.COMMON, 540.11f, 100));


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
        /*for (ShareItem sItem : shareStatusSaleList.getAvailableShares()) {

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
        }*/

        for(Map.Entry<String, List<ShareItem>> entry : shareStatusSaleList.newAvShares.entrySet()){

            List<ShareItem> addToList = new ArrayList<ShareItem>();

            for(ShareItem sItem : entry.getValue()) {

                synchronized (sItem){

                    if (sItem.getQuantity() < RESTOCK_THRESHOLD) {

                        ShareItem newShares = this.issueSharesRequest(sItem);

                        if (newShares != null && sItem.getQuantity() == 0) {
                            sItem.setOrderNum(newShares.getOrderNum());
                            sItem.increaseQuantity(newShares.getQuantity());

                            shareStatusSaleList.addToOrderedShares(
                                    new ShareItem(sItem.getOrderNum(),sItem.getBusinessSymbol(),sItem.getShareType(), sItem.getUnitPrice(),sItem.getQuantity())
                            );
                        }
                        else
                        {
                            addToList.add(newShares);
                            shareStatusSaleList.addToOrderedShares(
                                    new ShareItem(newShares.getOrderNum(),newShares.getBusinessSymbol(),newShares.getShareType(), newShares.getUnitPrice(),newShares.getQuantity())
                            );
                        }

                    }
                }


            }

            entry.getValue().addAll(addToList);
        }
    }

    /**
     * Pays a business for shares that were previously issued but not paid for.
     * @param lstOrders List of order numbers that have been depleted
     * @return true if payment is processed
     */
	private boolean payBusiness(List<String> lstOrders) {

        boolean paid = false;

        for(String orderNumber : lstOrders ){

            ShareItem shareToBePaid = shareStatusSaleList.orderedShares.get(orderNumber);

            synchronized (shareToBePaid) {

                // if the business is not registered, there is no interface, and null is returned
		        BusinessInterface bi = businessDirectory.get(shareToBePaid.getBusinessSymbol());
                if (bi != null) {
                    try {
                        paid = bi.recievePayment(shareToBePaid.getOrderNum(),
                                shareToBePaid.getUnitPrice() * shareToBePaid.getQuantity());

                        if (paid) {

                            shareStatusSaleList.orderedShares.remove(orderNumber);
                        }

                    } catch (Exception e) {
                        System.out.println(" \n " + e.getMessage());
                    }
                }
            }
        }

		return paid;

	}

    /**
     * Request a business to issue shares
     * @param sItem ShareItem to be issued
     * @return ShareItem (null will be returned if the transaction fails)
     */
	protected ShareItem issueSharesRequest(ShareItem sItem) {
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
    protected synchronized String generateOrderNumber() {
        orderInt = orderInt + 1;
        String orderNumber = Integer.toString(orderInt);
        return orderNumber;
    }

    /**
     * Retrieve the total quantity of share and share type
     * @param lstShareItem
     * @param sType
     * @return
     */
    private int getShareQuantity(List<ShareItem> lstShareItem, ShareType sType) {

        int totQuantity = 0;

        //Retrieve Business Shares in Available list
        for (ShareItem sItem : lstShareItem){

            if (sItem.getShareType() == sType){
                totQuantity = totQuantity + sItem.getQuantity();
            }

        }

        return totQuantity;

    }

}