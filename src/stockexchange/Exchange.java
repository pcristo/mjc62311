package stockexchange;

import business.*;
import client.*;
import util.Config;
import share.*;

import java.rmi.registry.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by Gay on 15-05-25.
 */
public class Exchange {

    private static  final int COMMISSION_MARKUP = 10;
    private static  final int RESTOCK_THRESHOLD = 1000;
    private static  int orderInt = 1000;
    private static  ShareSalesStatusList shareStatusSaleList;



    private static Map<String, String> businessDirectory = new HashMap<String, String>();

    //private Business yahoo;
    //private Business microsoft;
    private BusinessServerInterface google;

    // ----------------------     CONSTRUCTOR     ----------------------------------


    public Exchange() {

        this.setBusinessDirectory();

        shareStatusSaleList = new ShareSalesStatusList();

        //Initialize Businesses


        Config instance = Config.getInstance();

        //yahoo = new Business(instance.getAttr("yahoo"));
        //microsoft = new Business(instance.getAttr("microsoft"));
        //google = new Business(instance.getAttr("google"));

        this.startBusinessService();
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
     * Called to send a share request issue to businesses
     * @param sItem
     * @return ShareItem
     */
    private ShareItem issueSharesRequest(ShareItem sItem) {

        Boolean sharesIssued = false;

        String businessName = businessDirectory.get(sItem.getBusinessSymbol());

        String orderNum = this.generateOrderNumber();

        //TODO: Change to businessName once all services are coded
        switch ("google") {

            //case "microsoft" : sharesIssued = microsoft.issueShares(new ShareOrder(orderNum,"BR123",sItem.getBusinessSymbol(),sItem.getShareType(),sItem.getUnitPrice(),RESTOCK_THRESHOLD,sItem.getUnitPrice()));

            //case "yahoo" : sharesIssued = yahoo.issueShares(new ShareOrder(orderNum, "BR123", sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));

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

    private boolean startBusinessService() {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            BusinessServiceClient client = new BusinessServiceClient("stockService", 1099);
            Registry registry = LocateRegistry.getRegistry(client.getPort());
            this.google = (BusinessServerInterface) registry.lookup(client.getServiceName());

            return true;

        } catch (Exception e) {
            //System.err.println("Client exception:");
            //e.printStackTrace();

            return false;
        }
    }

}
