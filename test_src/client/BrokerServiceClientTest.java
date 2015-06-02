package client;

import logger.TimerLoggerClient;
import org.junit.Before;
import org.junit.Test;
import stockexchange.broker.Broker;
import stockexchange.broker.BrokerInterface;

import static org.junit.Assert.*;


public class BrokerServiceClientTest {

    BrokerServiceClient client;

    @Before
    public void setUp() throws Exception {
        client = new BrokerServiceClient();
    }


    @Test
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