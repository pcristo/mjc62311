package stockexchange;

import business.Business;
import business.ShareOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareSalesStatusList{


    private static  final int COMMISSION_MARKUP = 10;
    private static  final int RESTOCK_THRESHOLD = 1000;


    private static Map<ShareItem,Customer>  soldShares = new HashMap<ShareItem,Customer>();
    private static List<ShareItem> availableShares = new ArrayList<ShareItem>();
    private static Map<String, String> businessDirectory = new HashMap<String, String>();

    private Business yahoo;
    private Business microsoft;
    private Business google;

    // ----------------------     CONSTRUCTOR     ----------------------------------


    public ShareSalesStatusList() {

        this.availableShares = this.populateAvailable();
        this.setBusinessDirectory();

        //Initialize Businesses

        yahoo = new Business("C://Users//Gay.Hazan//OneDrive//Documents//MASTERS//COMP6231//Project//Repo//mjc62311//src//business//yahoo_data.csv");
        microsoft = new Business("C://Users//Gay.Hazan//OneDrive//Documents//MASTERS//COMP6231//Project//Repo//mjc62311//src//business//microsoft_data.csv");
        google = new Business("C://Users//Gay.Hazan//OneDrive//Documents//MASTERS//COMP6231//Project//Repo//mjc62311//src//business//google_data.csv");
    }


    //----------------------     SETTERS     ----------------------------------

    /**
     *
     */
    public void setBusinessDirectory() {


        createBusinessDirectory();
    }


    //----------------------     GETTERS     ----------------------------------

    /**
     *
     * @return
     */
    public Map<ShareItem, Customer> getSoldShares() {
        return soldShares;
    }

    /**
     *
     * @return
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
    public ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) {

        int shareIndex  = -1;

        //TODO : See if share are available
        for  (ShareItem s : shareItemList.getLstShareItems())
        {
            shareIndex = isShareAvailable(s);

            if (shareIndex >= 0)
            {
                //TODO : Contact business service to complete sale

                //TODO : Add shares to SOLD list
                this.updateShares(s,info,shareIndex);



                //TODO : Calculate comission and call restock

            }
        }

        //Restock Share Lists
        this.restock();

        this.printShares();

        return  this;
    }


    /**
     *
     */
    public void printShares() {

        System.out.println("- ******** Available Share ******** -");

        for (ShareItem s : this.availableShares)
        {
            StringBuilder shareDescription = new StringBuilder();

            shareDescription.append(s.getBusinessSymbol());
            shareDescription.append(" ");
            shareDescription.append(s.getShareType());
            shareDescription.append(" ");
            shareDescription.append(s.getUnitPrice());
            shareDescription.append(" ");
            shareDescription.append(s.getQuantity());


            System.out.println(shareDescription.toString());



        }


        System.out.println("\n - ******** Sold Shares ******** -");

        this.soldShares.forEach((k, v) -> this.printMessage(v.getCustomerReferenceNumber() + " " + k.printShareInfo()));


    }


    /**
     *
     * @param message
     */
    public void printMessage(String message) {

        System.out.println(" \n " + message);
    }


    // ---------------------- PRIVATE METHODS ----------------------------------

    /**
     *
     */
    private void restock() {

        this.printMessage("...... Restocking Shares .......");

        List<ShareItem> tempShares = new ArrayList<>();

        //Check Available stock amount
        for (ShareItem sItem : availableShares) {

            if (sItem.getQuantity() < RESTOCK_THRESHOLD){

                 ShareItem newShares = this.issueSharesRequest(sItem);

                if (newShares != null) {

                    tempShares.add(newShares);
                }
            }

        }

        availableShares.addAll(tempShares);
    }

    /**
     * Verify if share is available
     * @param share
     * @return -1 if not available
     */
    private int isShareAvailable(ShareItem share) {

        int notAvailable = -1;

        String businessSymbol;

        int quantity = 0;

        //Is Share available
        for (int i =0; i < this.availableShares.size(); i++)
        {
            businessSymbol = this.availableShares.get(i).getBusinessSymbol();
            quantity = this.availableShares.get(i).getQuantity();

            if (share.getBusinessSymbol() ==  businessSymbol &&   quantity >= share.getQuantity())
            {

                return i;

            }
        }

        return notAvailable;
    }

    /**
     *
     * @param soldShareItem
     */
    private void updateShares(ShareItem soldShareItem, Customer customer,  int indexAvailableShare) {


        this.addToSoldShares(soldShareItem,customer);

        //Update or remove share item depending on share amount sold.
        ShareItem availableShare = availableShares.get(indexAvailableShare);

        if (availableShare.getQuantity() <= soldShareItem.getQuantity()) {

            availableShares.remove(indexAvailableShare);

        } else {

            availableShare.reduceQuantity(soldShareItem.getQuantity());
        }

    }

    /**
     *
     * @return
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
     * @return
     */
    private void addToSoldShares(ShareItem shareItem, Customer customer) {


        soldShares.put(shareItem,customer);
    }


    /**
     *
     * @return
     */
    private ArrayList<ShareItem> populateAvailable() {

        ArrayList<ShareItem> availableShares = new ArrayList<ShareItem>();

        //For Testing
        ShareItem share1 = new ShareItem("ini01","MSFT","common",540.11f,100);
        ShareItem share2 = new ShareItem("ini01","MSFT.B","convertible",523.32f,400);
        ShareItem share3 = new ShareItem("ini01","MSFT.C","preferred",541.28f,700);
        ShareItem share4 = new ShareItem("ini01","GOOG","common",540.11f,100);
        ShareItem share5 = new ShareItem("ini01","GOOG.B","convertible",523.32f,400);
        ShareItem share6 = new ShareItem("ini01","GOOG.C","preferred",541.28f,700);
        ShareItem share7 = new ShareItem("ini01","GOOG","common",540.11f,100);


        availableShares.add(share1);
        availableShares.add(share2);
        availableShares.add(share3);
        availableShares.add(share4);
        availableShares.add(share5);
        availableShares.add(share6);
        availableShares.add(share7);

        return availableShares;

    }

    /**
     *
     * @param sItem
     * @return
     */
    private ShareItem issueSharesRequest(ShareItem sItem) {

        Boolean sharesIssued = false;

        String businessName = businessDirectory.get(sItem.getBusinessSymbol());

        String orderNum = "test001";

        switch (businessName.toLowerCase()) {

            case "microsoft" : sharesIssued = microsoft.issueShares(new ShareOrder("MSFT01","BR123",sItem.getBusinessSymbol(),sItem.getShareType(),sItem.getUnitPrice(),RESTOCK_THRESHOLD,sItem.getUnitPrice()));

            case "yahoo" : sharesIssued = yahoo.issueShares(new ShareOrder("MSFT01", "BR123", sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));

            case "google" : sharesIssued = google.issueShares(new ShareOrder("MSFT01","BR123",sItem.getBusinessSymbol(),sItem.getShareType(),sItem.getUnitPrice(),RESTOCK_THRESHOLD,sItem.getUnitPrice()));
        }

        if (sharesIssued) {

            ShareItem newShareItem = new ShareItem(orderNum,sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD);
            return newShareItem;
        }

        return null;
    }

}