package stockexchange;

import java.util.ArrayList;

/**
 * Created by gay.hazan on 22/05/2015.
 */
public class ShareList {

    private ArrayList<ShareItem> lstShareItems;

    /**
     *
     * @param lstShareItems
     */
    public ShareList(ArrayList<ShareItem> lstShareItems) {
        this.lstShareItems = lstShareItems;
    }

    /**
     *
     * @return List of ShareItem
     */
    public ArrayList<ShareItem> getLstShareItems() {
        return lstShareItems;
    }

    /**
     *
     * @param lstShareItems
     */
    public void setLstShareItems(ArrayList<ShareItem> lstShareItems) {
        this.lstShareItems = lstShareItems;
    }

    public String toString() {
        return lstShareItems.toString();
    }
}
