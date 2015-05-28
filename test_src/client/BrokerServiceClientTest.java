package client;

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


    /** RUN THIS TO TEST RMI CONNECTIONS **/
    /** TO RUN
     * 0) Update json.config
     * 1) Start rmiregistry from the out/ directory of project
     * 2) Compile entire project
     * 3) Run Business.java
     * 4) Run Broker.java
     * 5) Run this test
     *
     * @throws Exception
     */
    @Test
    public void testRMI() throws Exception {
        BrokerInterface broker = client.getBroker();

        assertNotNull(broker.getBusinessTicker("google"));
        assertNotNull(broker.getBusinessTicker("yahoo"));
        assertNotNull(broker.getBusinessTicker("microsoft"));

        assertTrue(broker.getBusinessTicker("google").equals("GOOG"));
        assertTrue(broker.getBusinessTicker("yahoo").equals("YHOO"));
        assertTrue(broker.getBusinessTicker("microsoft").equals("MSFT"));
    }
}