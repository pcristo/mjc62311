package stockexchange;

import client.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by Gay on 15-05-24.
 */
public class StockExchangeTest {

    private ShareSalesStatusList shareSalesStatusList;

    @Before
    public void setUp() {

        this.shareSalesStatusList = new ShareSalesStatusList();
    }

    @Test
    public void allCompaniesAreInTheDirecotryMapping()
    {

        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("GOOG") == "GOOGLE");
        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("GOOG.B") == "GOOGLE");
        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("GOOG.C") == "GOOGLE");


        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("YHOO") == "YAHOO");
        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("YHOO.C") == "YAHOO");
        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("YHOO.B") == "YAHOO");

        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("MSFT") == "MICROSOFT");
        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("MSFT.C") == "MICROSOFT");
        assertTrue(this.shareSalesStatusList.getBusinessDirectory().get("MSFT.B") == "MICROSOFT");


    }

    @Test
    public void BySharesAndPrintResult() {

        //Create new Customer
        Customer newCust = new Customer(1,"Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );

        //ShareItemList
        ArrayList<ShareItem> lstShares = new ArrayList<ShareItem>();

        lstShares.add(new ShareItem("","MSFT.B", "convertible", 523.32f, 100));
        lstShares.add(new ShareItem("","MSFT.C","preferred",541.28f,200));
        lstShares.add(new ShareItem("","GOOG","common",540.11f,100));

        ShareSalesStatusList stockService = new ShareSalesStatusList().sellShares(new ShareList(lstShares),newCust);


        assertTrue(this.shareSalesStatusList.getSoldShares().get(lstShares.get(0)) == newCust);

    }
}
