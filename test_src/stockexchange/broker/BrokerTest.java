package stockexchange.broker;

import client.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class BrokerTest {

    Broker broker;
    ArrayList<String> shares;
    Customer customer;

    @Before
    public void setUp() {
        broker = new Broker();
        shares = new ArrayList<String>(){{
            add("GOOG");
            add("MSOFT");
            add("AAPL");
        }};
        customer = new Customer(1001, "Ross", "Ross Street", "Ross Street 2", "Ross town", "Ross Province", "ROS SSMI", "Ross County");

    }

    @Test
    public void getTickerListingTest() {
        // PM1 identfiies only 3 businenss on exchange
        assertEquals(broker.getTickerListing().size(), 3);
    }

    @Test
    public void sellSharesTest() {
        assertTrue(broker.sellShares(shares, "preferred", 150, customer));
    }

    @Test
    public void buySharesTest() {
        assertTrue(broker.buyShares(shares, "preferred", 150, customer));
    }

}