package business;

import org.junit.Before;
import org.junit.Test;

import share.Share;
import share.ShareOrder;
import share.ShareType;
import util.Config;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Ross on 2015-05-26.
 */
public class BusinessTest {
    Business business;

    @Before
    public void setUp() {
    	business = new Business(Config.getInstance().getAttr("google"));
    }

    @Test
    public void testIssueShares() {
    	ShareOrder aSO = new ShareOrder("a00", "broker1", "GOOG", ShareType.PREFERRED, 0, 150, (float) 1000.0);
    	assertTrue(business.issueShares(aSO));
    }

    @Test
    public void testShareTypeExists() {
    	assertTrue(business.getShareInfo(ShareType.PREFERRED) != null);
    }
}