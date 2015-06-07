package client;

import logger.TimerLoggerClient;
import org.junit.Before;
import org.junit.Test;
import share.ShareType;
import stockexchange.broker.BrokerInterface;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class BrokerServiceClientTest {

    BrokerServiceClient client;

    @Before
    public void setUp() throws Exception {
        client = new BrokerServiceClient();
    }


    @Test
    /**
     * Outputs the time of the operation only.  No other logging messages currently.
     */
    public void testRMI() throws Exception {
        TimerLoggerClient tlc = new TimerLoggerClient();
        tlc.start();
        BrokerInterface broker = client.getBroker();

        assertNotNull(broker.getBusinessTicker("google"));
        assertNotNull(broker.getBusinessTicker("yahoo"));
        assertNotNull(broker.getBusinessTicker("microsoft"));

        assertTrue(broker.getBusinessTicker("google").equals("GOOG"));
        assertTrue(broker.getBusinessTicker("yahoo").equals("YHOO"));
        assertTrue(broker.getBusinessTicker("microsoft").equals("MSFT"));

        tlc.end();
    }

    @Test
    /**
     *  Test a client purchasing a share
     */
    public void testSharePurchase()  {
        try {
            BrokerInterface broker = client.getBroker();
            ArrayList<String> tickers = new ArrayList<String>();
            tickers.add("GOOG");
            Customer customer = new Customer("Batman");

            assertTrue(broker.sellShares(tickers, ShareType.COMMON, 100, customer));

            tickers = new ArrayList<String>();
            tickers.add("MSFT");
            customer = new Customer("Spiderman");
            assertTrue(broker.sellShares(tickers, ShareType.COMMON, 100, customer));

        } catch(Exception e) {
            e.printStackTrace();
        }





    }


}