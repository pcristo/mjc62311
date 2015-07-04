import FrontEnd.FrontEnd;
import business.BusinessServer;
import corba.broker_domain.iBroker;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import stockexchange.broker.BrokerServer;
import stockexchange.exchange.Exchange;
import stockexchange.exchange.ExchangeServer;

import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
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
            threads.add(BusinessServer.launch("APPL"));
            threads.add(BusinessServer.launch("YHOO"));
            threads.add(BusinessServer.launch("MSFT"));
        }

        if(broker){
            threads.add(BrokerServer.launch());
        }
        try {
            Thread.sleep(3000);
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
            Thread.sleep(3000);
        } catch(Exception e) {
            System.out.println("Integration Exception: " + e.getMessage());
        }
    }

    @Test
    public void distributedGetBusinessTest() throws Exception {
        // Exchange should be null untill the servers start up
        assertNull(Exchange.exchange);
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;
        // We want to validate that the exchange can get all the businesses
        assertNotNull(exchange.getBusiness("GOOG"));
        assertNotNull(exchange.getBusiness("APPL"));
        assertNotNull(exchange.getBusiness("YHOO"));
        assertNotNull(exchange.getBusiness("MSFT"));
        stopServers(threads);
    }

    @Test
    public void distributedRegisterBusinessTest() {

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
    public void distributedUnregisterBusinessTest() {
        ArrayList<Thread> threads = startServers(true, true, false);
        Exchange exchange = Exchange.exchange;
        assertNotNull(exchange.getBusiness("GOOG"));
        assertTrue(exchange.unregister("GOOG"));
        assertTrue(exchange.getBusiness("GOOG").isEmpty());

        stopServers(threads);
    }



    @Test
    public void distributedGetBusinessIFance() {

    }

    @Test
    public void distributedSellShares() {

    }

    @Test
    public void dsitributedGetShares() {

    }

    @Test
    public void distributedGetListing() {

    }

    @Test
    public void distributedGetBusinessTicker() {

    }



    @Test
    public void TestLaunchBrokerServer() {
        Thread brokerThread = BrokerServer.launch();
        try {
            // Give thread time to launch
            Thread.sleep(1000);
            iBroker broker = FrontEnd.getBroker();
            broker.registerCustomer("Ross", "", "", "", "", "");
        } catch(Exception e) {
            // Server is running, so it shouldn't reach here
            fail();
        } finally {
            brokerThread.interrupt();
        }

    }


    @Test
    public void TestLaunchFourBusinessServers() throws InterruptedException {
        Thread exchangeThread = ExchangeServer.launch();

        Thread.sleep(5000);


        Thread[] servers = { BusinessServer.launch("GOOG"),
                    BusinessServer.launch("APPL"), BusinessServer.launch("YHOO"),
                    BusinessServer.launch("MSFT") };

        Thread.sleep(5000); // give the threads plenty of time to launch


        // Test that we have an exchange object
        // Test that all the businesses are registered correctly
        Exchange exchange = Exchange.exchange;
        assertNotNull(exchange);
        assertNotNull(exchange.getBusinessIFace("GOOG"));
        assertNotNull(exchange.getBusinessIFace("YHOO"));
        assertNotNull(exchange.getBusinessIFace("MSFT"));
        assertNotNull(exchange.getBusinessIFace("APPL"));

        exchangeThread.interrupt();

        // test passes if servers are still running
        for (Thread s : servers) {
            TestCase.assertTrue(s.isAlive());
            s.interrupt();
            System.out.println("Killed a business thread.");
        }



    }




}
