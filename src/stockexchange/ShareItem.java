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

    public ShareItem(String businessSymbol, String shareType, float unitPrice, int quantity) {

        super(businessSymbol,shareType,unitPrice);
        this.quantity = quantity;

    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity(int quantity) {
        return this.quantity;
    }


}
