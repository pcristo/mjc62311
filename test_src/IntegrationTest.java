import business.BusinessServer;
import client.BrokerServiceClient;
import common.logger.TimerLoggerClient;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import stockexchange.exchange.Exchange;
import stockexchange.exchange.ExchangeServer;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    private Thread thread;

    @Before
    public void setUp()  {

    }



    @Test
    public void TestLaunchFourBusinessServers() throws InterruptedException {
        Thread thread;
        try {

            Exchange exchange = ExchangeServer.servant;
            assertNull(exchange);
            thread = new Thread() {
                public void run() {
                    try {
                        // Make sure ORBD is running on 9999
                        ExchangeServer.main(null);
                        //          projectLauncher.main(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();

            Thread.sleep(5000);


            Thread[] servers = { BusinessServer.launch("GOOG"),
                    BusinessServer.launch("APPL"), BusinessServer.launch("YHOO"),
                    BusinessServer.launch("MSFT") };

            Thread.sleep(5000); // give the threads plenty of time to launch

            //Client Purchase Test
            Thread client = BrokerServiceClient.launch();
            client.sleep(5000);



            // test passes if servers are still running
            for (Thread s : servers) {
                TestCase.assertTrue(s.isAlive());
                s.interrupt();
                System.out.println("Killed a business thread.");
            }


            //assertNotNull(exchange.getBusinessIFace("GOOG"));


            //thread.interrupt();

        } catch(Exception e) {
            System.out.println("Exeption in Integration test: " + e.getMessage());
        }
    }

}
