package stockexchange.broker;

import common.Customer;
import common.share.ShareType;
import mock.MockBroker;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class BrokerTest {

    MockBroker broker;
    ArrayList<String> shares;
    Customer customer;

    @Before
    public void setUp() throws Exception {
        broker = new MockBroker();
        shares = new ArrayList<String>(){{
            add("GOOG");
            add("MSFT");
            add("YHOO");
            add("APPL");
        }};
        customer = new Customer("Ross", "Ross Street", "Ross Street 2", "Ross town", "Ross Province", "ROS SSMI", "Ross County");
    }

    @Test
    public void getTickerListingTest() {
        // 3 businesses
        assertEquals(broker.getTickerListing().size(), 4);
    }

    @Test
    public void sellSharesTest()  {
        assertTrue(broker.sellShares(shares, ShareType.COMMON, 100, customer));
    }

    @Test
    public void getBusinessTickerTest() {
        assertTrue(broker.getBusinessTicker("GOOG").equals("GOOG"));
    }

}