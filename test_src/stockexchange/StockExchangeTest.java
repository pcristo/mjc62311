package stockexchange;

import client.Customer;

import logger.TimerLoggerClient;
import org.junit.Before;
import org.junit.Test;

import share.ShareType;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by Gay on 15-05-24.
 */
public class StockExchangeTest {

    private Exchange exchange;

    @Before
    public void setUp() throws Exception {

        this.exchange = new Exchange();
    }

    @Test
    public void allCompaniesAreInTheDirectoryMapping()
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
        Customer newCust = new Customer("Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );

        //ShareItemList
        ArrayList<ShareItem> lstShares = new ArrayList<ShareItem>();

        lstShares.add(new ShareItem("","MSFT.B",ShareType.CONVERTIBLE, 523.32f, 100));
        lstShares.add(new ShareItem("","MSFT.C",ShareType.PREFERRED,541.28f,100));
        lstShares.add(new ShareItem("", "GOOG", ShareType.COMMON, 540.11f, 100));

        TimerLoggerClient tlc = new TimerLoggerClient();

        ShareSalesStatusList shareStatus = this.exchange.sellShares(new ShareList(lstShares),newCust);

      //  assertTrue(shareStatus.getShares(newCust).size() == 3);

    }

    @Test
    public void PrintAllShareFromBusiness() {

        try {
           // System.out.println(this.exchange.yahoo.getSharesList());
            System.out.println(this.exchange.microsoft.getSharesList());
            System.out.println(this.exchange.google.getSharesList());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
