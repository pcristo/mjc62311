package stockexchange.exchange;

import common.Customer;

import java.util.*;


public class ShareSalesStatusList{


    private volatile Map<Integer,List<ShareItem>> soldShares;
    private volatile List<ShareItem> availableShares;
    protected volatile Map<String, ShareItem> orderedShares;
    protected volatile Map<String, List<ShareItem>> newAvShares;


    public ShareSalesStatusList() {
        this.soldShares = Collections.synchronizedMap(new HashMap<Integer, List<ShareItem>>());
        this.availableShares = Collections.synchronizedList(new ArrayList<ShareItem>());
        this.orderedShares = Collections.synchronizedMap(new HashMap<String, ShareItem>());
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
     * Add share to available shares list
     * @param aShare
     */
    public void addToNewAvShares(ShareItem aShare){

        //Is share already in list
        List<ShareItem> lstShares = this.newAvShares.get(aShare.getBusinessSymbol());
        ShareItem newShare = new ShareItem(aShare.getOrderNum(),aShare.getBusinessSymbol(), aShare.getShareType(),aShare.getUnitPrice(),aShare.getQuantity());

        if (lstShares == null) {
            lstShares = new ArrayList<ShareItem>(){{add(newShare);}};
            this.newAvShares.put(aShare.getBusinessSymbol(),lstShares);

        } else {

                lstShares.add(newShare);
        }

        //Add to Ordered shares list
        this.addToOrderedShares(aShare);

    }

    /**
     * Keep track of ordered shared until they are paid
     * @param aShare
     */
    public void addToOrderedShares(ShareItem aShare){

        //Add to Ordered shares list
        this.orderedShares.put(aShare.getOrderNum(), aShare);

    }

    /**
     *
     * @return Map of share items index by customer id
     */
    public Map<Integer,List<ShareItem>> getSoldShares() {
        return soldShares;
    }



    /**
     * Display all sold shares, avaialble shares and ordered shares
     */
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

        for(Map.Entry<String, ShareItem> entry : this.orderedShares.entrySet()){
            System.out.println(entry.getValue().printShareInfo());
        }

        //Sold Shares
        System.out.println();
        System.out.println("Sold Shares List\n");

        for(Map.Entry<Integer, List<ShareItem>> entry : this.soldShares.entrySet()){
            System.out.println(entry.getKey());
            for(ShareItem sItem : entry.getValue()){

                System.out.println(sItem.printShareInfo());
            }
        }
    }

    @Override
    public String toString() {
        return "Sold SHares: " + soldShares.toString() + "\nAvailable Shares: " + availableShares;
    }









}