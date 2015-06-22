package business;

import common.logger.LoggerServer;
import common.share.Share;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;

import java.rmi.RemoteException;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Ross on 2015-05-26.
 * Edited by Patrick on 2015-05-27
 */
public class BusinessTest {
    Thread logger;

    @Before
    public void setUp() throws InterruptedException {
		// start up the logger
    	logger = new Thread(()->LoggerServer.main(null));
		logger.start();
		Thread.sleep(4000);
    }

    @After
    public void tearDown() throws Exception{
    	logger.interrupt();
    }

    @Test
    public void testCreateBusiness() {
        new Business(Config.getInstance().getAttr("google"));
        new Business(Config.getInstance().getAttr("microsoft"));
        new Business(Config.getInstance().getAttr("yahoo"));
    }

    @Test
    public void testGetTicker() throws RemoteException {
    	Business google = new Business(Config.getInstance().getAttr("google"));
        assertTrue(google.getTicker().equals("GOOG"));
    }

    @Test
    public void TestOrderShares() throws Exception{
    	Business google = new Business(Config.getInstance().getAttr("google"));
    	Share gshareCommon = google.getShareInfo(ShareType.COMMON);
    	
        // Create a (valid) order and send the request
    	ShareOrder aSO = new ShareOrder("1", "broker1", gshareCommon.getBusinessSymbol(), 
    			gshareCommon.getShareType(), 0, 150, gshareCommon.getUnitPrice());
        assertTrue(google.issueShares(aSO));
    }
    
    @Test
    public void TestInvalidOrderFails_PriceTooLow() throws Exception{
    	Business google = new Business(Config.getInstance().getAttr("google"));
    	Share gshareConvertible = google.getShareInfo(ShareType.CONVERTIBLE);
    	
        // Can't issue, price is too low
    	ShareOrder aSO = new ShareOrder("2", "broker2", gshareConvertible.getBusinessSymbol(),
        		gshareConvertible.getShareType(), 0 , 150, 0);
        assertFalse(google.issueShares(aSO));
    }
    
    @Test
    public void TestInvalidOrderFails_OrderQuantityBad() throws Exception{
    	Business google = new Business(Config.getInstance().getAttr("google"));
    	Share gshareConvertible = google.getShareInfo(ShareType.CONVERTIBLE);
    	
        // Can't issue, need to order at least 1 share
    	ShareOrder aSO = new ShareOrder("3", "broker2", gshareConvertible.getBusinessSymbol(),
        		gshareConvertible.getShareType(), 0 , 0, gshareConvertible.getUnitPrice());
    	assertFalse(google.issueShares(aSO));
    }

    
    @Test
    public void TestInvalidOrderFails_DuplicateOrderNumber() throws Exception{
    	Business google = new Business(Config.getInstance().getAttr("google"));
    	Share gshareCommon = google.getShareInfo(ShareType.COMMON);
    	
        // Order successfully the first time, then repeat and fail because same order #
    	ShareOrder aSO = new ShareOrder("4", "broker1", gshareCommon.getBusinessSymbol(), 
    			gshareCommon.getShareType(), 0, 150, gshareCommon.getUnitPrice());
        assertTrue(google.issueShares(aSO));
        assertFalse(google.issueShares(aSO));
    }    
    
    @Test
    public void TestGetShareInfo() {
    	Business google = new Business(Config.getInstance().getAttr("google"));
    	    	
    	assertTrue(google.getShareInfo(ShareType.PREFERRED) != null);
        assertTrue(google.getShareInfo(ShareType.COMMON) != null);
        assertTrue(google.getShareInfo(ShareType.CONVERTIBLE) != null);
    }

    @Test
    public void TestGetShareList() {
    	Business google = new Business(Config.getInstance().getAttr("google"));
    	
        List<Share> shares = google.getSharesList();
        shares.forEach((share)-> {
            share.getBusinessSymbol();
            assertTrue(share.getBusinessSymbol().contains("GOOG"));
            ShareType type = share.getShareType();
            assertTrue(type == ShareType.COMMON || type == ShareType.PREFERRED || type == ShareType.CONVERTIBLE);

        });


    }


}