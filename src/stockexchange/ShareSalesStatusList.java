package stockexchange;

import java.util.ArrayList;

/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareSalesStatusList{


    private static  int COMMISSION_MARKUP = 10;
    private int totalCommission;

    private ArrayList<ShareItem> soldShares;
    private ArrayList<ShareItem> availableShares;


    public ShareSalesStatusList() {
        this.soldShares = this.populateSold();
        this.availableShares = this.populateAvailable();
    }

    /**
     *
     * @param shareItemList
     * @param info
     * @return
     */
    public ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) {

        this.printMessage("-----------BEFORE SALE----------------");
        this.printShares();

        int shareIndex  = -1;

        //TODO : See if share are available
        for  (ShareItem s : shareItemList.getLstShareItems())
        {
            shareIndex = isShareAvailable(s);

            if (shareIndex >= 0)
            {
                //TODO : Contact business service to complete sale

                //TODO : Add shares to SOLD list
                this.updateShares(s,shareIndex);



                //TODO : Calculate comission and call restock

            }
        }

        this.printMessage("------------ AFTER SALE --------------");
        this.printShares();

        return  this;
    }

    /**
     *
     */
    public void restock() {

        //TODO : Implement restock
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
    private void updateShares(ShareItem soldShareItem, int indexAvailableShare) {


        this.soldShares.add(soldShareItem);

        //Update or remove share item depending on share amount sold.
        ShareItem availableShare = this.availableShares.get(indexAvailableShare);

        if (availableShare.getQuantity() <= soldShareItem.getQuantity()) {

            this.availableShares.remove(indexAvailableShare);

        } else {

            availableShare.reduceQuantity(soldShareItem.getQuantity());
        }

    }


    private ArrayList<ShareItem> populateSold() {

        ArrayList<ShareItem> soldShares = new ArrayList<ShareItem>();

        //For Testing
        ShareItem share1 = new ShareItem("GOOG","common",540.11f,100);
        ShareItem share2 = new ShareItem("GOOG.B","convertible",523.32f,400);
        ShareItem share3 = new ShareItem("GOOG.C","preferred",541.28f,700);
        ShareItem share4 = new ShareItem("GOOG","common",540.11f,100);

        soldShares.add(share1);
        soldShares.add(share2);
        soldShares.add(share3);
        soldShares.add(share4);

        return soldShares;

    }

    private ArrayList<ShareItem> populateAvailable() {

        ArrayList<ShareItem> availableShares = new ArrayList<ShareItem>();

        //For Testing
        ShareItem share1 = new ShareItem("MSFT","common",540.11f,100);
        ShareItem share2 = new ShareItem("MSFT.B.B","convertible",523.32f,400);
        ShareItem share3 = new ShareItem("MSFT.C","preferred",541.28f,700);
        ShareItem share4 = new ShareItem("GOOG","common",540.11f,100);
        ShareItem share5 = new ShareItem("GOOG.B","convertible",523.32f,400);
        ShareItem share6 = new ShareItem("GOOG.C","preferred",541.28f,700);
        ShareItem share7 = new ShareItem("GOOG","common",540.11f,100);


        availableShares.add(share1);
        availableShares.add(share2);
        availableShares.add(share3);
        availableShares.add(share4);
        availableShares.add(share5);
        availableShares.add(share6);
        availableShares.add(share7);

        return availableShares;

    }

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

        for (ShareItem s : this.soldShares)
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
    }

    public void printMessage(String message) {

        System.out.println(" \n " + message + " \n ");
    }
}
