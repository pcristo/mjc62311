package stockexchange;

import client.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import share.ShareType;


/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareSalesStatusList{


    private static Map<ShareItem, Customer> soldShares = new HashMap<ShareItem,Customer>();
    private static List<ShareItem> availableShares = new ArrayList<ShareItem>();


    // ----------------------     CONSTRUCTOR     ----------------------------------


    public ShareSalesStatusList() {

        availableShares = this.populateAvailable();

    }


    //----------------------     SETTERS     ----------------------------------



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
    public List<ShareItem> getAvailableShares() {

        return availableShares;
    }


    /**
     * Given a customer determine get all of that customers stocks
     * @param customer wanting stock information
     * @return list of customers stocks
     */
    public ArrayList<ShareItem> getShares(Customer customer) {
        ArrayList<ShareItem> customerShares = new ArrayList<ShareItem>();
        Map<ShareItem, Customer> soldShares = getSoldShares();
        for (Map.Entry<ShareItem, Customer> entry : soldShares.entrySet()) {
            ShareItem key = entry.getKey();
            Customer value = entry.getValue();
            if(value == customer) {
                customerShares.add(key);
            }
        }
        return customerShares;


    }




    //---------------------- PUBLIC METHODS ----------------------------------


    /**
     *
     */
    public void printShares() {

        System.out.println("- ******** Available Share ******** -");

        availableShares.forEach( k -> this.printMessage(k.printShareInfo()));

        System.out.println("\n - ******** Sold Shares ******** -");

        this.soldShares.forEach((ShareItem k, Customer v) -> {

            this.printMessage(v.getCustomerReferenceNumber() + " " + k.printShareInfo());

        });


    }


    /**
     * Verify if share is available
     * @param share
     * @return -1 if not available
     */
    public ShareItem isShareAvailable(ShareItem share) {

        int notAvailable = -1;

        ShareItem soldShare = null;

        String businessSymbol;

        int quantity = 0;

        //Is Share available
        for (int i =0; i < this.availableShares.size(); i++)
        {
            synchronized (this.availableShares.get(i)) {
                businessSymbol = this.availableShares.get(i).getBusinessSymbol();

                quantity = this.availableShares.get(i).getQuantity();

                if (share.getBusinessSymbol() == businessSymbol && quantity >= share.getQuantity()) {


                    soldShare = this.availableShares.get(i);
                    this.getAvailableShares().get(i).reduceQuantity(share.getQuantity());

                    break;

                }
            }
        }

        return soldShare;
    }

    /**
     *
     * @param soldShareItem
     */
    public void updateShares(ShareItem soldShareItem, Customer customer,  int indexAvailableShare) {


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
    public void addToSoldShares(ShareItem shareItem, Customer customer) {

        synchronized (shareItem){
            soldShares.put(shareItem,customer);
        }

    }


    /**
     *
     */
    public void addToAvailableShares(ShareItem aShare) {

         //Find this type of share that is at quantity 0
        for(ShareItem s : this.getAvailableShares()) {

            if (s.getBusinessSymbol() == aShare.getBusinessSymbol()) {

                synchronized (s) {

                    s.setOrderNum(aShare.getOrderNum());
                    s.setQuantity(aShare.getQuantity());
                }

            }
        }
    }


    /**
     *
     * @return
     */
    private ArrayList<ShareItem> populateAvailable() {

        ArrayList<ShareItem> availableShares = new ArrayList<ShareItem>();

        //For Testing
        ShareItem share1 = new ShareItem("901","MSFT",ShareType.COMMON,540.11f,100);
        ShareItem share2 = new ShareItem("902","MSFT.B",ShareType.CONVERTIBLE,523.32f,400);
        ShareItem share3 = new ShareItem("903","MSFT.C",ShareType.PREFERRED,541.28f,700);
        ShareItem share4 = new ShareItem("904","GOOG",ShareType.COMMON,540.11f,100);
        ShareItem share5 = new ShareItem("905","GOOG.B",ShareType.CONVERTIBLE,523.32f,400);
        ShareItem share6 = new ShareItem("906","GOOG.C",ShareType.PREFERRED,541.28f,700);
        ShareItem share7 = new ShareItem("907","GOOG",ShareType.COMMON,540.11f,100);


        availableShares.add(share1);
        availableShares.add(share2);
        availableShares.add(share3);
        availableShares.add(share4);
        availableShares.add(share5);
        availableShares.add(share6);
        availableShares.add(share7);

        return availableShares;

    }


    //---------------------- PRIVATE METHODS ----------------------------------

    /**
     *
     * @param message
     */
    private void printMessage(String message) {

        System.out.println(" \n " + message);
    }




}