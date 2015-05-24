package stockexchange;

import business.Share;
//import com.sun.tools.classfile.Annotation;

import java.util.ArrayList;

/**
 * Created by Gay on 15-05-23.
 */
public class Broker {

    public static void main(String[] args){

        ShareList ShareList = new ShareList(createListofShares());

        //Create new Customer
        Customer newCust = new Customer(1,"Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );

        ShareSalesStatusList stockService = new ShareSalesStatusList().sellShares(ShareList,newCust);

    }

    private static ArrayList<ShareItem> createListofShares() {

        //ShareItemList
        ArrayList<ShareItem> lstShares = new ArrayList<ShareItem>();

        lstShares.add(new ShareItem("MSFT.B.B", "convertible", 523.32f, 100));
        lstShares.add(new ShareItem("MSFT.C","preferred",541.28f,200));
        lstShares.add(new ShareItem("GOOG","common",540.11f,100));

        return lstShares;

    }
}
