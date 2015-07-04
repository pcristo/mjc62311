package stockexchange.exchange;

import common.Customer;
import common.share.ShareType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ShareSalesStatusListTest {

    ShareSalesStatusList shareSalesStatusList;

    @Before
    public void setUp() throws Exception {
        shareSalesStatusList = new ShareSalesStatusList();
    }

    @After
    public void tearDown() throws Exception {
        shareSalesStatusList = null;
    }

    @Test
    public void testGetShares() throws Exception {
        assertNull(shareSalesStatusList.getShares(new Customer("Ross")));
        shareSalesStatusList.addToNewAvShares(new ShareItem("test", "GOOG", ShareType.COMMON, (float) 1000, 100));
        assertNull(shareSalesStatusList.getShares(new Customer("Ross")));
    }

    @Test
    public void testAddToSoldShares() throws Exception {
        Customer customer = new Customer("Ross");
        shareSalesStatusList.addToSoldShares(new ShareItem("test", "GOOG", ShareType.COMMON, (float) 1000, 100), customer);

        assertNotNull(shareSalesStatusList.getShares(customer));
        ShareItem shareItem = shareSalesStatusList.getShares(customer).get(0);
        assertEquals(shareItem.getBusinessSymbol(), "GOOG");
        assertEquals(shareItem.getQuantity(), 100);
        assertEquals(shareItem.getShareType(), ShareType.COMMON);
    }


    @Test
    public void testGetSoldShares() throws Exception {
        assertTrue(shareSalesStatusList.getSoldShares().isEmpty());
        Customer customer = new Customer("Ross");
        shareSalesStatusList.addToSoldShares(new ShareItem("test", "GOOG", ShareType.COMMON, (float) 1000, 100), customer);

        List<ShareItem> shareItem = shareSalesStatusList.getSoldShares().get(customer.getCustomerReferenceNumber());
        assertEquals(shareItem.get(0).getQuantity(), 100);
        assertEquals(shareItem.get(0).getBusinessSymbol(), "GOOG");

    }

}