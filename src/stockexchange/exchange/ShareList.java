package stockexchange.exchange;

import java.util.ArrayList;

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

    public String toString() {
        return lstShareItems.toString();
    }
}
