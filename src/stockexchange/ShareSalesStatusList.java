package stockexchange;

import client.Customer;
import share.ShareType;

import java.util.*;


/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareSalesStatusList{


    private  Map<Integer,List<ShareItem>> soldShares;
    private  List<ShareItem> availableShares;


    // ----------------------     CONSTRUCTOR     ----------------------------------


    public ShareSalesStatusList() {

        this.soldShares = new HashMap<Integer, List<ShareItem>>();
        this.availableShares = new ArrayList<ShareItem>();

    }


    //----------------------     SETTERS     ----------------------------------



    //----------------------     GETTERS     ----------------------------------

    /**
     *
     * @return
     */
    public Map<Integer,List<ShareItem>> getSoldShares() {
        return soldShares;
    }

    /**
     *
     * @return
     */
    public List<ShareItem> getAvailableShares() {

        return this.availableShares;
    }


    /**
     * Given a customer determine get all of that customers stocks
     * @param customer wanting stock information
     * @return list of customers stocks
     */
    public List<ShareItem> getShares(Customer customer) {

       return this.soldShares.get(customer.getCustomerReferenceNumber());

    }


    //---------------------- PUBLIC METHODS ----------------------------------


    /**
     *
     */
    public void printShares() {

        System.out.println("- ******** Available Share ******** -");

        availableShares.forEach( k -> this.printMessage(k.printShareInfo()));

        System.out.println("\n - ******** Sold Shares ******** -");

        //Print Customer referemce and all shares belonging to customer
        Iterator it = this.soldShares.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            this.printMessage("Customer: " + pair.getKey() );
            for(ShareItem sItem : (List<ShareItem>)pair.getValue()) {
                this.printMessage(sItem.printShareInfo());
            }
        }
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

                businessSymbol = this.availableShares.get(i).getBusinessSymbol();

                quantity = this.availableShares.get(i).getQuantity();

                if (share.getBusinessSymbol().equals(businessSymbol) && quantity >= share.getQuantity()) {


                    soldShare = new ShareItem(this.availableShares.get(i).getOrderNum(),
                                                this.availableShares.get(i).getBusinessSymbol(),
                                                this.availableShares.get(i).getShareType(),
                                                this.availableShares.get(i).getUnitPrice(),
                                                this.availableShares.get(i).getQuantity()) ;

                    this.getAvailableShares().get(i).reduceQuantity(share.getQuantity());

                    break;
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

            this.availableShares.remove(indexAvailableShare);

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

            List<ShareItem> custShares = this.soldShares.get(customer.getCustomerReferenceNumber());

            if (custShares == null) {

                List<ShareItem> newShares = new ArrayList<ShareItem>();
                newShares.add(shareItem);

                this.soldShares.put(customer.getCustomerReferenceNumber(), newShares);

            } else {

                //TODO : Not sure about the synchronized
                synchronized(custShares){

                    custShares.add(shareItem);

                }
            }

        }

    }


    /**
     *
     */
    public void addToAvailableShares(ShareItem aShare) {

         //Find this type of share that is at quantity 0

        this.availableShares.add(aShare);
        /*for(ShareItem s : this.getAvailableShares()) {

            if (s.getBusinessSymbol() == aShare.getBusinessSymbol()) {

                synchronized (s) {

                    s.setOrderNum(aShare.getOrderNum());
                    s.setQuantity(aShare.getQuantity());
                }

            }
        }*/
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

    public String toString() {
        return "Sold SHares: " + soldShares.toString() + "\nAvailable Shares: " + availableShares;
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