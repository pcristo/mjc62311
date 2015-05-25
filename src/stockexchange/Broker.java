package stockexchange;

import business.Share;
//import com.sun.tools.classfile.Annotation;

import java.util.ArrayList;

/**
 * Created by Gay on 15-05-23.
 */
public class Broker {

    // IS THIS THE EXCHANGE GUY?
    private synchronized static ShareSalesStatusList exchange;

    public static void main(String[] args){

        ShareList ShareList = new ShareList(createListofShares());

        //Create new Customer
        Customer newCust = new Customer(1,"Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );

        ShareSalesStatusList stockService = new ShareSalesStatusList().sellShares(ShareList,newCust);

    }

    public Broker() {


    }



    /**
     *
     * @return list of available shares
     */
    public ArrayList<ShareItem> getShares() {

        ArrayList<ShareItem> listShares = new ArrayList<ShareItem>();
        listShares.add(new ShareItem("MSFT.B.B", "convertible", 523.32f, 100));
        listShares.add(new ShareItem("MSFT.C","preferred",541.28f,200));
        listShares.add(new ShareItem("GOOG","common",540.11f,100));
        return listShares;
    }
}
