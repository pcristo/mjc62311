package stockexchange.exchange;

import business.BusinessInterface;
import business_domain.interface_business;
import business_domain.interface_businessHelper;
import common.Customer;
import common.share.ShareType;
import common.util.Config;
import java.rmi.NotBoundException;
import java.util.*;

import exchange_domain.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import java.util.Properties;

/** 
 * The exchange class acts as an intermediary between businesses and stock brokers. Brokers
 * make requests to purchase stock from the exchange, which then either sells existing shares
 * to the broker or requests new shares be issued from the business.
 * Please note that the exchange assumes that all share types within a business have the same
 * ticker symbol and price.
 */
public class Exchange extends iExchangePOA {
    private static  final int COMMISSION_MARKUP = 10;
    private static  final int RESTOCK_THRESHOLD = 500;
    private static  int orderInt = 1100;
    protected static ShareSalesStatusList shareStatusSaleList;

    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    
    /**
     * Business directory that maps stock symbols to remote interfaces
     */
    private Map<String, interface_business> businessDirectory = new HashMap<String, interface_business>();
    
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

            shareStatusSaleList = new ShareSalesStatusList();
        }
        catch(Exception e) {

            System.out.println(e.getMessage());
        }

    }

    /**
     *
     * @param businessName looking for
     * @return business object
     * @throws NotBoundException
     */
    public void getBusiness(String businessName) {

        float price = this.priceDirectory.get(businessName);

        System.out.println("Businesss : " + businessName + " Price : " + this.priceDirectory.get(businessName));
    }


    /**
     * Registers a new business with the exchange, providing an initial price.
     * @param symbol
     * @param price
     * @return
     */
    public boolean registerBusiness(String symbol, float price) {

        try{
            businessDirectory.put(symbol, getBusinessIFace(symbol));
            priceDirectory.put(symbol, price);

            return this.businessDirectory.get(symbol).issueShares(generateOrderNumber(),"br01",symbol,0,price,1000, price);

        }
        catch (Exception e) {

            System.out.println(e.getMessage());
        }


        return false;
    }
    
    /**
     * Delists a business from the exchange
     * @param symbol to delist
     * @throws Exception when the symbol is not listed
     */
    public boolean unregister(String symbol) {
    	// try to remove the stock from the business and price registers. If the symbol
    	// is not found, throw an exception
    	interface_business bi = businessDirectory.remove(symbol);
    	if ((bi == null) || (priceDirectory.remove(symbol) == null)) 
    		return false;
    	
    	// TODO: business must be unbound from the client?

        return true;
    }
    
    /**
     * Returns a business interface for making calls to the remote business server.
     * @param businessName looking for (specified as the stock symbol)
     * @return business object
     * @throws RemoteException
     * @throws NotBoundException
     */
    public interface_business getBusinessIFace(String businessName){

        try {
            // Set up ORB properties
            // Hi Gay! I had to add this to allow setting of port and IP
            // address. see Config.json. -patrick
            Properties p = new Properties();
            p.put("org.omg.CORBA.ORBInitialPort",
                    Config.getInstance().getAttr("namingServicePort"));
            p.put("org.omg.CORBA.ORBInitialHost",
                    Config.getInstance().getAttr("namingServiceAddr"));

            ORB orb = ORB.init(new String[0],p);
            org.omg.CORBA.Object objRef = orb
                    .resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            interface_business iBusiness = (interface_business) interface_businessHelper.narrow(ncRef
                    .resolve_str("business-"+businessName.toUpperCase()));

            return iBusiness;
        }
        catch(Exception e) {

            System.out.println(e.getMessage());
        }

        return null;
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
        for(Map.Entry<String, Float> entry : this.priceDirectory.entrySet()) {

            lstShares.add(new ShareItem("",entry.getKey(), ShareType.COMMON,entry.getValue(),100));
            lstShares.add(new ShareItem("",entry.getKey(), ShareType.CONVERTIBLE,entry.getValue(),100));
            lstShares.add(new ShareItem("",entry.getKey(), ShareType.PREFERRED,entry.getValue(),100));
        }

        for(ShareItem shareItem : lstShares) {

            ShareItem addShareItem = this.issueSharesRequest(shareItem);

            if (addShareItem != null) {
                shareStatusSaleList.addToNewAvShares(addShareItem);
            }
        }
    }

    /**
     *Method to restock any available common.share that is below the threshold
     */
    private void restock() {

        System.out.println(" \n " + "...... Restocking Shares .......");

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
		        //TODO To Review
		        //BusinessInterface bi = businessDirectory.get(shareToBePaid.getBusinessSymbol());
                //if (bi != null) {
                    try {
                        paid = google.recievePayment(shareToBePaid.getOrderNum(),
                                shareToBePaid.getUnitPrice() * shareToBePaid.getQuantity());

                        if (paid) {

                            shareStatusSaleList.orderedShares.remove(orderNumber);
                        }

                    } catch (Exception e) {
                        System.out.println(" \n " + e.getMessage());
                    }
                //}
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

		interface_business bi = businessDirectory.get(sItem.getBusinessSymbol());
		if (bi == null) return null;

		String orderNum = generateOrderNumber();

		/*synchronized (orderNum) {
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
		}*/

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

     /**
     *  updateSharePrice(SYMBOL, PRICE) updates existing business's share price. If business
     *  does not exist, it's an error.
     * @param symbol
     * @param price
     * @return
     */
    public boolean updateSharePrice(String symbol, float price) {

        //TODO: Remove
        System.out.println(symbol);

        //Does business exist in exchange
        if (this.priceDirectory.get(symbol) == null) {
            return false;
        } else {

            //Update Registry price
            synchronized (this.priceDirectory.get(symbol)){

                this.priceDirectory.put(symbol, price);
                System.out.println("IN 1");
            }

            //Update all prices in available shares
           /* synchronized (shareStatusSaleList.newAvShares.get(symbol)){

                for (ShareItem sItem : shareStatusSaleList.newAvShares.get(symbol)){
                    sItem.setUnitPrice(price);
                }
            }*/
        }

        return false;

    }
}