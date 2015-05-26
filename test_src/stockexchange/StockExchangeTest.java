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

    private Exchange exchange;

    @Before
    public void setUp() {

        this.exchange = new Exchange();
    }

    @Test
    public void allCompaniesAreInTheDirecotryMapping()
    {

        assertTrue(this.exchange.getBusinessDirectory().get("GOOG") == "GOOGLE");
        assertTrue(this.exchange.getBusinessDirectory().get("GOOG.B") == "GOOGLE");
        assertTrue(this.exchange.getBusinessDirectory().get("GOOG.C") == "GOOGLE");


        assertTrue(this.exchange.getBusinessDirectory().get("YHOO") == "YAHOO");
        assertTrue(this.exchange.getBusinessDirectory().get("YHOO.C") == "YAHOO");
        assertTrue(this.exchange.getBusinessDirectory().get("YHOO.B") == "YAHOO");

        assertTrue(this.exchange.getBusinessDirectory().get("MSFT") == "MICROSOFT");
        assertTrue(this.exchange.getBusinessDirectory().get("MSFT.C") == "MICROSOFT");
        assertTrue(this.exchange.getBusinessDirectory().get("MSFT.B") == "MICROSOFT");


    }

    @Test
    public void BySharesAndPrintResult() {

        //Create new Customer
        Customer newCust = new Customer(1,"Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );

        //ShareItemList
        ArrayList<ShareItem> lstShares = new ArrayList<ShareItem>();

        lstShares.add(new ShareItem("801","MSFT.B", "convertible", 523.32f, 100));
        lstShares.add(new ShareItem("802","MSFT.C","preferred",541.28f,200));
        lstShares.add(new ShareItem("803","GOOG","common",540.11f,100));

        ShareSalesStatusList shareStatus = this.exchange.sellShares(new ShareList(lstShares),newCust);


        assertTrue(shareStatus.getSoldShares().get(lstShares.get(0)) == newCust);

    }
}
