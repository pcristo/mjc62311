package stockexchange.exchange;

import common.Customer;
import common.share.ShareType;

import java.util.*;


public class ShareSalesStatusList{


    private volatile Map<Integer,List<ShareItem>> soldShares;
    private volatile List<ShareItem> availableShares;
    private volatile Map<String, List<ShareItem>> orderedShares;
    private volatile Map<String, List<ShareItem>> newAvShares;


    public ShareSalesStatusList() {
        this.soldShares = Collections.synchronizedMap(new HashMap<Integer, List<ShareItem>>());
        this.availableShares = Collections.synchronizedList(new ArrayList<ShareItem>());
        this.orderedShares = Collections.synchronizedMap(new HashMap<String, List<ShareItem>>());
        this.newAvShares = Collections.synchronizedMap(new HashMap<String, List<ShareItem>>());
    }

    /**
     *
     * @return List of ShareItems
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

    /**
     * Verify if common.share is available
     * @param share ShareItem to check if is available
     * @return -1 if not available | ShareItem
     */
    public ShareItem isShareAvailable(ShareItem share) {
        ShareItem soldShare = null;
        String businessSymbol;
        int quantity = 0;

        synchronized (availableShares) {
            //Is Share available
            for (int i = 0; i < this.availableShares.size(); i++) {
                businessSymbol = this.availableShares.get(i).getBusinessSymbol();
                quantity = this.availableShares.get(i).getQuantity();

                if (share.getBusinessSymbol().equals(businessSymbol) && quantity >= share.getQuantity()) {
                    soldShare = new ShareItem(this.availableShares.get(i).getOrderNum(),
                            this.availableShares.get(i).getBusinessSymbol(),
                            this.availableShares.get(i).getShareType(),
                            this.availableShares.get(i).getUnitPrice(),
                            this.availableShares.get(i).getQuantity());
                    ShareItem si = this.getAvailableShares().get(i);
                    si.reduceQuantity(share.getQuantity());
                    break;
                }
            }
        }
        return soldShare;
    }

    /**
     *
     * @param shareItem ShareItem to be added
     * @param customer Customer who made the transaction
     */
    public void addToSoldShares(ShareItem shareItem, Customer customer) {

        synchronized (shareItem){
            List<ShareItem> custShares = this.soldShares.get(customer.getCustomerReferenceNumber());
            if (custShares == null) {
                List<ShareItem> newShares = new ArrayList<ShareItem>();
                newShares.add(shareItem);
                this.soldShares.put(customer.getCustomerReferenceNumber(), newShares);
            } else {
                synchronized(custShares){
                    custShares.add(shareItem);
                }
            }

        }

    }

    /**
     *
     * @param aShare ShareItem to add to available shares
     */
    public void addToAvailableShares(ShareItem aShare) {

        //Find this type of common.share that is at quantity 0
        synchronized (availableShares) {
            this.availableShares.add(aShare);
        }
    }

    public void addToNewAvShares(ShareItem aShare){

        //Is share already in list
        List<ShareItem> lstShares = this.newAvShares.get(aShare.getBusinessSymbol());

        if (lstShares == null) {

            lstShares = new ArrayList<ShareItem>(){{add(aShare);}};
            this.newAvShares.put(aShare.getBusinessSymbol(),lstShares);

        } else {

                lstShares.add(aShare);
        }

        //Add to Ordered shares list
        this.orderedShares.put(aShare.getOrderNum(),new ArrayList<ShareItem>(){{add(aShare);}});

    }

    @Override
    public String toString() {
        return "Sold SHares: " + soldShares.toString() + "\nAvailable Shares: " + availableShares;
    }


    /**
     *
     * @return ArrayList of ShareItems of shares that are available for purchase
     */
    private ArrayList<ShareItem> populateAvailable() {

        ArrayList<ShareItem> availableShares = new ArrayList<ShareItem>();
        synchronized (availableShares) {
            //For Testing
            ShareItem share1 = new ShareItem("901", "MSFT", ShareType.COMMON, 540.11f, 100);
            ShareItem share2 = new ShareItem("902", "MSFT.B", ShareType.CONVERTIBLE, 523.32f, 400);
            ShareItem share3 = new ShareItem("903", "MSFT.C", ShareType.PREFERRED, 541.28f, 700);
            ShareItem share4 = new ShareItem("904", "GOOG", ShareType.COMMON, 540.11f, 100);
            ShareItem share5 = new ShareItem("905", "GOOG.B", ShareType.CONVERTIBLE, 523.32f, 400);
            ShareItem share6 = new ShareItem("906", "GOOG.C", ShareType.PREFERRED, 541.28f, 700);
            ShareItem share7 = new ShareItem("907", "GOOG", ShareType.COMMON, 540.11f, 100);


            availableShares.add(share1);
            availableShares.add(share2);
            availableShares.add(share3);
            availableShares.add(share4);
            availableShares.add(share5);
            availableShares.add(share6);
            availableShares.add(share7);
        }
        return availableShares;

    }

    /**
     *
     * @return Map of share items index by ? //TODO
     */
    public Map<Integer,List<ShareItem>> getSoldShares() {
        return soldShares;
    }


    public void PrintNewAvShares(){

        System.out.println();
        System.out.println("Available Shares List\n");

        for(Map.Entry<String, List<ShareItem>> entry : this.newAvShares.entrySet()){

                System.out.println(entry.getKey());

            for(ShareItem share : entry.getValue()){

                System.out.println(share.printShareInfo());
            }

        }

        //Ordered Shares
        System.out.println();
        System.out.println("Ordered Shares List\n");

        for(Map.Entry<String, List<ShareItem>> entry : this.orderedShares.entrySet()){

            System.out.println(entry.getKey());

            for(ShareItem share : entry.getValue()){

                System.out.println(share.printShareInfo());
            }

        }
    }



}