package stockexchange.exchange;

import business.BusinessInterface;
import common.Customer;
import common.logger.LoggerServer;
import common.share.ShareType;
import mock.MockExchange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExchangeTest {

    MockExchange exchange;
    Thread logger;

    @Before
    public void setUp() throws Exception {
        logger = new Thread(
                ()-> LoggerServer.main(null)
        );
        logger.start();
        exchange = new MockExchange();
    }

    @After
    public void tearDown() throws Exception {
        exchange = null;
        logger.interrupt();
        Thread.sleep(1000);
    }

    @Test
    public void testGetBusiness() throws Exception {
        BusinessInterface google = exchange.getBusiness("google");
        assertEquals(google.getTicker(), "GOOG");
    }

    @Test
    public void testGetBusinessDirectory() throws Exception {
        assertTrue(exchange.getBusinessDirectory().get("GOOG") == "GOOGLE");
        assertTrue(exchange.getBusinessDirectory().get("MSFT") == "MICROSOFT");
        assertTrue(exchange.getBusinessDirectory().get("YHOO") == "YAHOO");
    }


    @Test
    public void testSellShares() throws Exception {
        ShareList sharelist = new ShareList(new ArrayList<ShareItem>() {{
            add(new ShareItem("5", "GOOG", ShareType.COMMON, 1000, 100));
        }});
        exchange.sellShares(sharelist, new Customer("Ross"));

        // TODO ALOT MORE OF THESE TESTS, ONES THAT FAIL, ONES THAT PASS ETC
    }

    @Test
    public void testGetShares() throws Exception {
        assertNull(exchange.getShares(new Customer("Ross")));


        Customer customer = new Customer("Ross");

        ShareList sharelist = new ShareList(new ArrayList<ShareItem>() {{
            add(new ShareItem("5", "GOOG", ShareType.COMMON, 1000, 100));
        }});
        exchange.sellShares(sharelist, customer);
        assertNotNull(exchange.getShares(customer));
        assertEquals(exchange.getShares(customer).get(0).getBusinessSymbol(), "GOOG");
        // TODO ALOT MORE OF THESE TESTS
    }

    @Test
    public void testGetListing() throws Exception {
        assertTrue(exchange.getListing().contains("GOOG"));
    }

    @Test
    public void testGetBusinessTicker() throws Exception {
        assertTrue(exchange.getBusinessTicker("google").equals("GOOG"));
    }
}