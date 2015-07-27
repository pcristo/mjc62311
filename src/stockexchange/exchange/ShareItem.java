package stockexchange.exchange;

import common.share.Share;
import common.share.ShareType;

public class ShareItem extends Share {
	private static final long serialVersionUID = 1L;
	private int quantity;
    private float commission;
    private String  orderNum;

    public ShareItem() {}


    /**
     * @param businessSymbol
     * @param shareType
     * @param unitPrice
     * @param quantity
     */
    public ShareItem(String orderNum,String businessSymbol, ShareType shareType, float unitPrice, int quantity) {

        super(businessSymbol, shareType, unitPrice);
        setOrderNum(orderNum);
        setQuantity(quantity);
        setCommission(0.05f);
    }

    /**
     *
     * @param orderNum
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }


    /**
     * Set quantity
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @param numberSold
     */
    public void setSoldShares(int numberSold) {
    }

    public void setCommission(float comPerc) {

       this.commission =  this.getUnitPrice() * comPerc;
    }
    /**
     * Get Quantity
     * @return
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     *
     * @return String
     */
    public String getOrderNum() {
        return this.orderNum;
    }

    /**
     *
     * @return
     */
    public float getCommission() {
        return commission;
    }

    /**
     * Increase the share quantity of current share
     * @param increaseBy
     */
    public void increaseQuantity(int increaseBy){

        this.quantity = this.quantity + increaseBy;
    }

    /**
     *  Remove common.share that were sold from the available listing.
     * @param reduceBy
     */
    public void reduceQuantity(int reduceBy) {
        this.quantity = this.quantity - reduceBy;
    }

    /**
     *  Method to show the complete description of a common.share
     * @return String description of the common.share
     */
    public String printShareInfo() {

        StringBuilder shareDescription = new StringBuilder();

        shareDescription.append((this.getOrderNum()));
        shareDescription.append(" ");
        shareDescription.append(this.getBusinessSymbol());
        shareDescription.append(" ");
        shareDescription.append(this.getShareType());
        shareDescription.append(" ");
        shareDescription.append(this.getUnitPrice());
        shareDescription.append(" ");
        shareDescription.append(this.getQuantity());
        return shareDescription.toString();
    }

    public String toString(){
        return "Symbol: " + getBusinessSymbol() + " Comission: " +  getCommission() + " Qty:" + getQuantity();
    }
}
