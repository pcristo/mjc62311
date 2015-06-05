package stockexchange;

import business.*;
import share.*;

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


    // -------------------------- CONSTRUCTOR ----------------------------------------
    /**
     *
     * @param businessSymbol
     * @param shareType
     * @param unitPrice
     * @param quantity
     */
    public ShareItem(String orderNum,String businessSymbol, ShareType shareType, float unitPrice, int quantity) {

        super(businessSymbol,shareType,unitPrice);
        this.orderNum = orderNum;
        this.quantity = quantity;

    }


    // ---------------------------- SETTERS ---------------------------------------------

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


    // ----------------------------- GETTERS ---------------------------------------------

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


    // ----------------------------- PUBLIC METHODS ------------------------------------------

    /**
     *  Remove share that were sold from the available listing.
     * @param reduceBy
     */
    public void reduceQuantity(int reduceBy) {

        this.quantity = this.quantity - reduceBy;
    }

    /**
     *  Method to show the complete description of a share
     * @return String description of the share
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


}
