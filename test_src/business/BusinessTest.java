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
    Business google;
    Thread logger;

    @Before
    public void setUp() {
//        logger = new Thread(
//                ()-> LoggerServer.main(null)
//        );
//        logger.start();

        google = new Business(Config.getInstance().getAttr("google"));

    }

    @After
    public void tearDown() throws Exception{
        google = null;
   //     logger.interrupt();
      //  Thread.sleep(1000);
    }


    @Test
    public void testGetTicker() throws RemoteException {
        assertTrue(google.getTicker().equals("GOOG"));
    }

    @Test
    public void testIssueShares() throws Exception{

        // Valid order
    	ShareOrder aSO = new ShareOrder("a00", "broker1", "GOOG", ShareType.PREFERRED, 0, 150, (float) 1000.0);
        assertTrue(google.issueShares(aSO));

        // Cant issue, price is too low
        aSO = new ShareOrder("test1", "broker2", "GOOG", ShareType.CONVERTIBLE, 0 ,150, (float) 10.0);
        assertFalse(google.issueShares(aSO));

        // Cant issue, atleast by 1 man
        aSO = new ShareOrder("test2", "broker2", "GOOG", ShareType.CONVERTIBLE, 0 , -1, (float) 1000.0);
        assertFalse(google.issueShares(aSO));

        // Cant issue, invalid order number
        aSO = new ShareOrder("a00", "broker2", "GOOG", ShareType.CONVERTIBLE, 0 , 150, (float) 1000.0);
        assertFalse(google.issueShares(aSO));
    }

    @Test
    public void testGetShareInfo() {
    	assertTrue(google.getShareInfo(ShareType.PREFERRED) != null);
        assertTrue(google.getShareInfo(ShareType.COMMON) != null);
        assertTrue(google.getShareInfo(ShareType.CONVERTIBLE) != null);
    }

    @Test
    public void testGetShareList() {
        List<Share> shares = google.getSharesList();
        shares.forEach((share)-> {
            share.getBusinessSymbol();
            assertTrue(share.getBusinessSymbol().contains("GOOG"));
            ShareType type = share.getShareType();
            assertTrue(type == ShareType.COMMON || type == ShareType.PREFERRED || type == ShareType.CONVERTIBLE);

        });


    }


}