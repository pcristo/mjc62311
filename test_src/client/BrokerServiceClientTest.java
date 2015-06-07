package client;

import logger.TimerLoggerClient;
import org.junit.Before;
import org.junit.Test;
import stockexchange.broker.BrokerInterface;

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



}