package business;

import org.junit.Before;
import org.junit.Test;
import util.Config;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Ross on 2015-05-26.
 */
public class BusinessTest {

    Business b;

    @Before
    public void setUp() {
        b = new Business(Config.getInstance().getAttr("google"));
    }

    @Test
    public void testTickers() {
        assertTrue(b.getCompanyTicker().equals("GOOG"));

        // Three types of stocks
        assertTrue(b.getAllTickers().size() == 3);

        assertTrue(b.getTicker("preferred").equals("GOOG.C"));

    }




}