import FrontEnd.FrontEnd;
import business.BusinessServer;
import common.Customer;
import common.share.ShareType;
import corba.broker_domain.iBroker;
import org.junit.Before;
import org.junit.Test;
import stockexchange.broker.BrokerServer;
import stockexchange.exchange.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    @Before
    public void setUp()  {

    }

    public ArrayList<Thread> startServers(boolean exchange, boolean business, boolean broker) {
        ArrayList<Thread> threads = new ArrayList<Thread>();

        if(exchange) {
            threads.add(ExchangeServer.launch());
        }

        if(business) {
            threads.add(BusinessServer.launch("GOOG"));
            threads.add(BusinessServer.launch("AAPL"));
            threads.add(BusinessServer.launch("YHOO"));
            threads.add(BusinessServer.launch("MSFT"));
        }

        if(broker){
            threads.add(BrokerServer.launch());
        }
        try {
            Thread.sleep(5000);
        } catch(Exception e) {
            System.out.println("Integration Exception: " + e.getMessage());
        }
        return threads;
    }

    private void stopServers(ArrayList<Thread> threads) {
        for(Thread t : threads) {
            t.interrupt();
        }
        try {
            Thread.sleep(5000);
        } catch(Exception e) {
            System.out.println("Integration Exception: " + e.getMessage());
        }
    }

    @Test
    public void distributedExchangeGetBusinessTest() throws Exception {
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;
        // We want to validate that the exchange can get all the businesses
        assertNotNull(exchange.getBusiness("GOOG"));
        assertNotNull(exchange.getBusiness("AAPL"));
        assertNotNull(exchange.getBusiness("YHOO"));
        assertNotNull(exchange.getBusiness("MSFT"));
        stopServers(threads);
    }

    @Test
    public void distributedExchangeRegisterBusinessTest() {

        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;

        // We can re-register the google business server
        // It was registered during business server start up
        assertTrue(exchange.registerBusiness("GOOG", 10000));
        // Cant register something that doesn't exist
        assertFalse(exchange.registerBusiness("FAKEAROO", 1000));

        stopServers(threads);
    }

    @Test
    public void distributedExchangeUnregisterBusinessTest() {
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;
        assertNotNull(exchange.getBusiness("GOOG"));
        assertTrue(exchange.unregister("GOOG"));
        assertTrue(exchange.getBusiness("GOOG").isEmpty());



        stopServers(threads);
    }


    @Test
    public void distributedExchangeGetBusinessIFance() {
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;

        assertNotNull(exchange.getBusinessIFace("GOOG"));
        assertNotNull(exchange.getBusinessIFace("YHOO"));
        assertNotNull(exchange.getBusinessIFace("APPL"));
        assertNotNull(exchange.getBusinessIFace("MSFT"));

        stopServers(threads);


    }

    @Test
    public void distributedExchangeSellShares() {
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;

        // Build share order
        ArrayList<ShareItem> shareItems = new ArrayList<ShareItem>();
        shareItems.add(new ShareItem("00001", "GOOG", ShareType.COMMON, 1000, 10000));
        // Add to arraylist wrapper
        ShareList shareList = new ShareList(shareItems);
        // Identify customer to make purchase
        Customer ross = new Customer("Ross");

        // Make the sale
        ShareSalesStatusList shareStatusList = exchange.sellShares(shareList, ross);

        // Validate sale
        List<ShareItem> custShareItems = shareStatusList.getShares(ross);
        assertNotNull(custShareItems);
        assertTrue(custShareItems.size() == 1);
        assertTrue(custShareItems.get(0).getBusinessSymbol().equals("GOOG"));

        // Make another sale
        Customer pat = new Customer("Pat");
        // Pat will buy google and appl
        ArrayList<ShareItem> shareItems2 = new ArrayList<ShareItem>();
        shareItems2.add(new ShareItem("00002", "MSFT", ShareType.PREFERRED, 750, 1000));
        shareItems2.add(new ShareItem("00003", "GOOG", ShareType.COMMON, 1000, 1000));

        ShareList shareList2 = new ShareList(shareItems2);
        // Make the sale
        ShareSalesStatusList shareStatusListPat = exchange.sellShares(shareList2, pat);
        // Validate sale
        List<ShareItem> custShareItemsPat = shareStatusListPat.getShares(pat);
        assertNotNull(custShareItemsPat);
        System.out.println(custShareItemsPat);
        assertTrue(custShareItemsPat.size() == 2);
        assertTrue(custShareItemsPat.get(0).getBusinessSymbol().equals("MSFT"));
        assertTrue(custShareItemsPat.get(1).getBusinessSymbol().equals("GOOG"));

        // Validate that ross still has his shares
        // Validate sale
        custShareItems = shareStatusList.getShares(ross);
        assertNotNull(custShareItems);
        assertTrue(custShareItems.size() == 1);
        assertTrue(custShareItems.get(0).getBusinessSymbol().equals("GOOG"));


        stopServers(threads);
    }


    @Test
    public void distributedExchangeGetListingTest() {
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;

        ArrayList<String> listing = exchange.getListing();
        assertEquals(listing.size(), 4);
        assertTrue(listing.contains("GOOG"));
        assertTrue(listing.contains("AAPL"));
        assertTrue(listing.contains("MSFT"));
        assertTrue(listing.contains("YHOO"));

        stopServers(threads);

    }

    @Test
    public void distributedExchangeGetBusinessTickerTest() {
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;

        assertEquals(exchange.getBusinessTicker("GOOG"), "GOOG");
        assertEquals(exchange.getBusinessTicker("MSFT"), "MSFT");
        assertEquals(exchange.getBusinessTicker("AAPL"), "AAPL");
        assertEquals(exchange.getBusinessTicker("YHOO"), "YHOO");

        stopServers(threads);
    }


    @Test
    public void distribuedBrokerSellSharesTest() throws Exception {
        iBroker broker = FrontEnd.getBroker();

        // First try make a sale without registering



    }








}
