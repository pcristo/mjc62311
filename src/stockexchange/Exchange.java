package stockexchange;

import business.BusinessInterface;
import client.Customer;
import logger.LoggerClient;
import share.ShareOrder;
import util.Config;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Gay on 15-05-25.
 */
public class Exchange {

    private static  final int COMMISSION_MARKUP = 10;
    private static  final int RESTOCK_THRESHOLD = 1000;
    private static  int orderInt = 1000;
    private static  ShareSalesStatusList shareStatusSaleList;



    private static Map<String, String> businessDirectory = new HashMap<String, String>();

    private BusinessInterface yahoo;
    private BusinessInterface microsoft;
    private BusinessInterface google;


    // ----------------------     CONSTRUCTOR     ----------------------------------


    //TODO make business name in getBusiness an enum

    /**
     * Create Exchange object, initializes three businesses
     * @throws AccessException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public Exchange() throws AccessException, RemoteException, NotBoundException  {

        google = getBusiness("google");
        yahoo = getBusiness("yahoo");
        microsoft = getBusiness("microsoft");


        this.setBusinessDirectory();
        shareStatusSaleList = new ShareSalesStatusList();
    }


    //TODO make businessName an enum

    /**
     *
     * @param businessName looking for
     * @return business object
     * @throws RemoteException
     * @throws NotBoundException
     */
    public BusinessInterface getBusiness(String businessName) throws RemoteException, NotBoundException{

        //System.setProperty("java.security.policy", Config.getInstance().loadMacSecurityPolicy());

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
            Registry registry = LocateRegistry.getRegistry("localhost", port);
            BusinessInterface server = (BusinessInterface) registry.lookup(businessName);

            LoggerClient.log("Bound " + businessName + " on " + port);
            return server;
        } catch(NotBoundException nbe) {
            port++;
            return findBusiness(businessName, port);
        }
    }



    //----------------------     SETTERS     ----------------------------------

    /**
     * Setter BusinessDirector
     */
    private void setBusinessDirectory() {


        createBusinessDirectory();
    }


    //----------------------     GETTERS     ----------------------------------

    /**
     * Getter : Business Directory
     * @return Map of all business in exchange
     */
    public Map<String, String> getBusinessDirectory() {
        return businessDirectory;
    }


    //---------------------- PUBLIC METHODS ----------------------------------

    /**
     *
     * @param shareItemList
     * @param info
     * @return
     */
    public ShareSalesStatusList buyShares(ShareList shareItemList, Customer info) {
        //TODO
        return shareStatusSaleList;
    }

    /**
     * Sell Shares
     * @param shareItemList
     * @param info
     * @return ShareSalesStatusList - Can access sold shares and avaible shares lists
     */
    public ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) {

        int shareIndex  = -1;

        //TODO : See if share are available
        for  (ShareItem s : shareItemList.getLstShareItems())
        {
            shareIndex = shareStatusSaleList.isShareAvailable(s);

            if (shareIndex >= 0)
            {
                //TODO : Contact business service to complete sale

                //TODO : Add shares to SOLD list
                shareStatusSaleList.updateShares(s, info, shareIndex);



                //TODO : Calculate comission and call restock

            }
        }

        //Restock Share Lists
        this.restock();

        shareStatusSaleList.printShares();

        return  shareStatusSaleList;
    }



    /**
     * Given a customer determine get all of that customers stocks
     * @param customer wanting stock information
     * @return list of customers stocks
     */
    public ArrayList<ShareItem> getShares(Customer customer) {
        ArrayList<ShareItem> customerShares = new ArrayList<ShareItem>();
        Map<ShareItem, Customer> soldShares = shareStatusSaleList.getSoldShares();
        for (Map.Entry<ShareItem, Customer> entry : soldShares.entrySet()) {
            ShareItem key = entry.getKey();
            Customer value = entry.getValue();
            if(value == customer) {
                customerShares.add(key);
            }
        }
        return customerShares;


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


    // ---------------------- PRIVATE METHODS ----------------------------------

    /**
     *Method to restock any available share that is below the threshold
     */
    private void restock() {

        this.printMessage("...... Restocking Shares .......");

        List<ShareItem> tempShares = new ArrayList<ShareItem>();

        //Check Available stock amount
        for (ShareItem sItem : shareStatusSaleList.getAvailableShares()) {

            if (sItem.getQuantity() < RESTOCK_THRESHOLD){

                ShareItem newShares = this.issueSharesRequest(sItem);

                if (newShares != null) {

                    tempShares.add(newShares);
                }
            }

        }

        shareStatusSaleList.addToAvailableShares(tempShares);
    }

    /**
     * Method to print message to console
     * @param message
     */
    private void printMessage(String message) {

        System.out.println(" \n " + message);
    }

    /**
     * Initialize the business directory
     */
    private void createBusinessDirectory() {


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
     * Called to send a share request issue to businesses
     * @param sItem
     * @return ShareItem
     */
    private ShareItem issueSharesRequest(ShareItem sItem) {

        Boolean sharesIssued = false;

        String businessName = businessDirectory.get(sItem.getBusinessSymbol());

        String orderNum = this.generateOrderNumber();

        switch (businessName) {

            case "microsoft" :
                try {
                    sharesIssued = microsoft.issueShares(new ShareOrder(orderNum,"BR123",sItem.getBusinessSymbol(),sItem.getShareType(),sItem.getUnitPrice(),RESTOCK_THRESHOLD,sItem.getUnitPrice()));
                } catch (Exception e) {
                    printMessage(e.getMessage());
                }

            case "yahoo" :
                try {

                    sharesIssued = yahoo.issueShares(new ShareOrder(orderNum, "BR123", sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));

                } catch (Exception e) {

                    printMessage(e.getMessage());
                }

            case "google" :
                try {

                    sharesIssued = google.issueShares(new ShareOrder(orderNum, "BR123", sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));
                }
                catch (Exception e) {

                    printMessage(e.getMessage());
                }
        }

        if (sharesIssued) {

            ShareItem newShareItem = new ShareItem(orderNum,sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD);
            return newShareItem;
        }

        return null;
    }


    /**
     * Method to generate unique sequential order number for issue share
     */
    private String generateOrderNumber() {

        orderInt = orderInt + 1;

        String orderNumber = Integer.toString(orderInt);

        return orderNumber;

    }

}
