import client.BrokerServiceClient;
import common.logger.TimerLoggerClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    @Before
    public void setUp()  {
        try {
            // I know this is long ... but we need the servers to start up
            // TODO use join on the project launcher thread
            Thread.sleep(10000);
        } catch(Exception e) {
            System.out.println("How much Exceptions could an exception catcher catch if an exception catcher" +
                    " could catch exceptions?");
        }
    }


    @Test
    public void testRMI() throws Exception {
        BrokerServiceClient client = new BrokerServiceClient();
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
