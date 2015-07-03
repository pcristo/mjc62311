import FrontEnd.FrontEnd;
import business.BusinessServer;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import stockexchange.exchange.Exchange;
import stockexchange.exchange.ExchangeServer;

import static org.junit.Assert.assertNotNull;

public class IntegrationTest {

    private Thread thread;

    @Before
    public void setUp()  {

    }



    @Test
    public void TestLaunchFourBusinessServers() throws InterruptedException {
        ExchangeServer.launch();

        Thread.sleep(5000);


        Thread[] servers = { BusinessServer.launch("GOOG"),
                    BusinessServer.launch("APPL"), BusinessServer.launch("YHOO"),
                    BusinessServer.launch("MSFT") };

        Thread.sleep(5000); // give the threads plenty of time to launch

        //Client Purchase Test
        Thread client = FrontEnd.launch();
        client.sleep(5000);


        Exchange exchange = Exchange.exchange;
        assertNotNull(exchange);
        assertNotNull(exchange.getBusinessIFace("GOOG"));

        // test passes if servers are still running
        for (Thread s : servers) {
            TestCase.assertTrue(s.isAlive());
            s.interrupt();
            System.out.println("Killed a business thread.");
        }



    }

}
