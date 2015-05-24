package stockexchange;

import business.Share;

/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareItem extends Share {

    private String businessSymbol;
    private String shareType;
    private float unitPrice;
    private int quantity;

    /**
     *
     * @param businessSymbol
     * @param shareType
     * @param unitPrice
     * @param quantity
     */
    public ShareItem(String businessSymbol, String shareType, float unitPrice, int quantity) {

        super(businessSymbol,shareType,unitPrice);
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


}
