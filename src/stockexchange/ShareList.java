package stockexchange;

import java.util.ArrayList;

/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareList {

    private ArrayList<ShareItem> lstShareItems;

    public ShareList(ArrayList<ShareItem> lstShareItems) {
        this.lstShareItems = lstShareItems;
    }

    public ArrayList<ShareItem> getLstShareItems() {
        return lstShareItems;
    }

    public void setLstShareItems(ArrayList<ShareItem> lstShareItems) {
        this.lstShareItems = lstShareItems;
    }
}
