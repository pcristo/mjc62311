package client;

import common.logger.TimerLoggerClient;
import mock.MockBroker;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import common.share.ShareType;
import stockexchange.broker.Broker;
import stockexchange.broker.BrokerInterface;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class BrokerServiceClientTest {

    static BrokerServiceClient client;
    MockBroker broker;

    @Before
    public void setUp() throws Exception {

    	broker = new MockBroker();
        client = new BrokerServiceClient();
    }

    @After
    public void tearDown() {
        broker = null;
    }


    @Test
    public void testPrintOp() {
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        ServerDisplayMsgs.setInputStream(in);
        assertTrue(ServerDisplayMsgs.printOps() == 1);
    }

    @Test
    public void testPrintOptions() {
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        ServerDisplayMsgs.setInputStream(in);
        assertTrue(ServerDisplayMsgs.printOptions() == 1);
    }

    @Test
    public void testEnterTicker() {
        ByteArrayInputStream in = new ByteArrayInputStream("hello".getBytes());
        ServerDisplayMsgs.setInputStream(in);
        assertTrue(ServerDisplayMsgs.enterTicker().equals("hello"));
    }

    @Test
    public void testEnterTickerType() {
        ByteArrayInputStream in = new ByteArrayInputStream("common".getBytes());
        ServerDisplayMsgs.setInputStream(in);
        assertTrue(ServerDisplayMsgs.enterTickerType() == ShareType.COMMON);
    }

    @Test
    public void testEnterTickerQuantity() {
        ByteArrayInputStream in = new ByteArrayInputStream("22".getBytes());
        ServerDisplayMsgs.setInputStream(in);
        assertTrue(ServerDisplayMsgs.enterTickerQuantity() == 22);
    }

    @Test
    public void testGetCustomerInfo() {
        ByteArrayInputStream in = new ByteArrayInputStream("22\n1\n2\n3\n4\n5\n6\n7".getBytes());
        ServerDisplayMsgs.setInputStream(in);
        assertTrue(ServerDisplayMsgs.getCustomerInfo().equals("22;;d1;;d2;;d3;;d4;;d5;;d6;;d7"));
    }

}