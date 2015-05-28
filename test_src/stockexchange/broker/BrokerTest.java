package stockexchange.broker;

import client.Customer;

import org.junit.Before;
import org.junit.Test;

import share.ShareType;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class BrokerTest {

    Broker broker;
    ArrayList<String> shares;
    Customer customer;

    @Before
    public void setUp() throws Exception {
        broker = new Broker();
        shares = new ArrayList<String>(){{
            add("GOOG");
            add("MSOFT");
            add("AAPL");
        }};
        customer = new Customer(1001, "Ross", "Ross Street", "Ross Street 2", "Ross town", "Ross Province", "ROS SSMI", "Ross County");

    }

    @Test
    public void getTickerListingTest() throws RemoteException{
        // 3 businesses * 3
        assertEquals(broker.getTickerListing().size(), 9);
    }

    @Test
    public void sellSharesTest() throws RemoteException {
        assertTrue(broker.sellShares(shares, ShareType.PREFERRED, 150, customer));
    }

    @Test
    public void buySharesTest() throws RemoteException{
            assertTrue(broker.buyShares(shares, ShareType.PREFERRED, 150, customer));

    }

}