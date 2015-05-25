package stockexchange;

import business.*;

/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareItem extends Share {

    private String businessSymbol;
    private String shareType;
    private float unitPrice; //Isn't price suppose to always be a double...?
    private int quantity;

    //TODO: Include shareorder object in each shareItem for traceability
    private String  orderNum;

    /**
     *
     * @param businessSymbol
     * @param shareType
     * @param unitPrice
     * @param quantity
     */
    public ShareItem(String orderNum,String businessSymbol, String shareType, float unitPrice, int quantity) {

        super(businessSymbol,shareType,unitPrice);
        this.orderNum = orderNum;
        this.quantity = quantity;

    }

    /**
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     *
     * @param reduceBy
     */
    public void reduceQuantity(int reduceBy) {

        this.quantity = this.quantity - reduceBy;
    }

    /**
     *
     * @return
     */
    public String printShareInfo() {

        StringBuilder shareDescription = new StringBuilder();

        shareDescription.append(this.getBusinessSymbol());
        shareDescription.append(" ");
        shareDescription.append(this.getShareType());
        shareDescription.append(" ");
        shareDescription.append(this.getUnitPrice());
        shareDescription.append(" ");
        shareDescription.append(this.getQuantity());


        return shareDescription.toString();
    }


}
