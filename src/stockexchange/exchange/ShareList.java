package stockexchange.exchange;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareList implements Serializable{

    private static final long serialVersionUID = 1L;
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
