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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Exchange {

    private static  final int COMMISSION_MARKUP = 10;
    private static  final int RESTOCK_THRESHOLD = 100;
    private static  Integer orderInt = 1100;
    protected static ShareSalesStatusList shareStatusSaleList;


    private Client<BusinessInterface> client = new Client<BusinessInterface>();

    private Map<String, String> businessDirectory = new HashMap<String, String>();
    public BusinessInterface yahoo;
    public BusinessInterface microsoft;
    public BusinessInterface google;

    /**
     * Create Exchange object, initializes three businesses
     * @throws AccessException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public Exchange() throws RemoteException, NotBoundException  {
        google = getBusiness("google");
        yahoo = getBusiness("yahoo");
        microsoft = getBusiness("microsoft");

        createBusinessDirectory();
        shareStatusSaleList = new ShareSalesStatusList();

        initializeShares();
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
     * Getter : Business Directory
     * @return Map of all business in exchange
     */
    public Map<String, String> getBusinessDirectory() {
        return businessDirectory;
    }

    /**
     *
     * @param shareItemList ShareList of share to purchase from customer
     * @param info Customer selling shares
     * @return
     */
    public ShareSalesStatusList buyShares(ShareList shareItemList, Customer info) {
        // TODO implement
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
     * Given a customer determine get all of that customers stocks
     * @param customer wanting stock information
     * @return list of customers stocks
     */
    public List<ShareItem> getShares(Customer customer) {

        return shareStatusSaleList.getShares(customer);

    }

    /**
     *
     * @return arrayList of all tickers listed on exchange
     */
    public ArrayList<String> getListing() {

        Map<String, String> businesses = getBusinessDirectory();

        ArrayList<String> tickerList = new ArrayList<String>();

        for(String ticker : businesses.keySet()) {
            tickerList.add(ticker);
        }

        return tickerList;
    }

    /**
     *
     * @param businessName string of business TODO enum
     * @return the ticker commonly used to identify a company | Null if not found
     */
    public String getBusinessTicker(String businessName) {
        //TODO when businessDirectory stores business objects look it up there
        try {
            switch (businessName) {
                case "google":
                    return google.getTicker();
                case "microsoft":
                    return microsoft.getTicker();
                case "yahoo":
                    return yahoo.getTicker();
                default:
                    return null;
            }
        } catch(RemoteException rme) {
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

                    }
                }

            }
        }



    }

    /**
     *
     * @param soldShare ShareItem requiring business payment
     * @return true if payment is processed
     */
    private boolean payBusiness(ShareItem soldShare) {

        String businessName = businessDirectory.get(soldShare.getBusinessSymbol());


        switch (businessName.toLowerCase()) {

            case "microsoft" :
                try {
                     return microsoft.recievePayment(soldShare.getOrderNum(),soldShare.getUnitPrice() * soldShare.getQuantity());
                } catch (Exception e) {
                    System.out.println(" \n " + e.getMessage());
                }
                break;

            case "yahoo" :
                try {

                    return yahoo.recievePayment(soldShare.getOrderNum(), soldShare.getUnitPrice() * soldShare.getQuantity());

                } catch (Exception e) {

                    System.out.println(" \n " + e.getMessage());
                }
                break;
            case "google" :
                try {

                    return google.recievePayment(soldShare.getOrderNum(),soldShare.getUnitPrice() * soldShare.getQuantity());
                } catch (Exception e) {

                    System.out.println(" \n " + e.getMessage());
                }
                break;
        }

        return false;

    }




    /**
     * Initialize the business directory
     */
    protected void createBusinessDirectory() {
        businessDirectory.put("YHOO", "YAHOO");
        businessDirectory.put("YHOO.B","YAHOO");
        businessDirectory.put("YHOO.C", "YAHOO");
        businessDirectory.put("MSFT", "MICROSOFT");
        businessDirectory.put("MSFT.B", "MICROSOFT");
        businessDirectory.put("MSFT.C", "MICROSOFT");
        businessDirectory.put("GOOG", "GOOGLE");
        businessDirectory.put("GOOG.B", "GOOGLE");
        businessDirectory.put("GOOG.C", "GOOGLE");
    }



    /**
     * Called to send a common.share request issue to businesses
     * @param sItem ShareItem to be issued
     * @return ShareItem
     */
    private ShareItem issueSharesRequest(ShareItem sItem) {

        Boolean sharesIssued = false;

        String businessName = businessDirectory.get(sItem.getBusinessSymbol());

        String orderNum = this.generateOrderNumber();


        synchronized (orderNum) {

            switch (businessName.toLowerCase()) {

                case "microsoft":
                    try {
                        sharesIssued = microsoft.issueShares(new ShareOrder(orderNum, "BR123", sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));
                    } catch (Exception e) {
                        System.out.println(" \n " + e.getMessage());
                    }
                    break;

                case "yahoo":
                    try {

                        sharesIssued = yahoo.issueShares(new ShareOrder(orderNum, "BR123", sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));

                    } catch (Exception e) {

                        System.out.println(" \n " + e.getMessage());
                    }
                    break;
                case "google":
                    try {

                        sharesIssued = google.issueShares(new ShareOrder(orderNum, "BR123", sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));
                    } catch (Exception e) {

                        System.out.println(" \n " + e.getMessage());
                    }
                    break;
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