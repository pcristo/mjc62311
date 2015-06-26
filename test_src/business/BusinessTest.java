package business;

import common.logger.LoggerServer;
import common.share.Share;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;

import java.rmi.RemoteException;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Ross on 2015-05-26. Edited by Patrick on 2015-05-27
 */
public class BusinessTest {
	static Thread logger;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		// start up the logger
		logger = new Thread(() -> LoggerServer.main(null));
		logger.start();
		Thread.sleep(4000);

		if (!logger.isAlive())
			throw new InterruptedException();

		System.out.println("Logger started OK");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		logger.interrupt();
		System.out.println("Logger stopped OK");
	}

	@Test
	public void testCreateBusiness() {
		new Business(Config.getInstance().getAttr("GOOG"));
		new Business(Config.getInstance().getAttr("MSFT"));
		new Business(Config.getInstance().getAttr("YHOO"));
		new Business(Config.getInstance().getAttr("APPL"));
	}

	@Test
	public void testGetTicker() throws RemoteException {
		Business google = new Business(Config.getInstance().getAttr("GOOG"));
		assertTrue(google.getTicker().equals("GOOG"));
	}

	@Test
	public void TestOrderShares() throws Exception {
		Business google = new Business(Config.getInstance().getAttr("GOOG"));
		Share gshareCommon = google.getShareInfo(ShareType.COMMON);

		// Create a (valid) order and send the request
		ShareOrder aSO = new ShareOrder("1", "broker1",
				gshareCommon.getBusinessSymbol(), gshareCommon.getShareType(),
				0, 150, gshareCommon.getUnitPrice());
		assertTrue(google.issueShares(aSO));
	}

	@Test
	public void TestInvalidOrderFails_PriceTooLow() throws Exception {
		Business google = new Business(Config.getInstance().getAttr("GOOG"));
		Share gshareConvertible = google.getShareInfo(ShareType.CONVERTIBLE);

		// Can't issue, price is too low
		ShareOrder aSO = new ShareOrder("2", "broker2",
				gshareConvertible.getBusinessSymbol(),
				gshareConvertible.getShareType(), 0, 150, 0);
		assertFalse(google.issueShares(aSO));
	}

	@Test
	public void TestInvalidOrderFails_OrderQuantityBad() throws Exception {
		Business google = new Business(Config.getInstance().getAttr("GOOG"));
		Share gshareConvertible = google.getShareInfo(ShareType.CONVERTIBLE);

		// Can't issue, need to order at least 1 share
		ShareOrder aSO = new ShareOrder("3", "broker2",
				gshareConvertible.getBusinessSymbol(),
				gshareConvertible.getShareType(), 0, 0,
				gshareConvertible.getUnitPrice());
		assertFalse(google.issueShares(aSO));
	}

	@Test
	public void TestInvalidOrderFails_DuplicateOrderNumber() throws Exception {
		Business google = new Business(Config.getInstance().getAttr("GOOG"));
		Share gshareCommon = google.getShareInfo(ShareType.COMMON);

		// Order successfully the first time, then repeat and fail because same
		// order #
		ShareOrder aSO = new ShareOrder("4", "broker1",
				gshareCommon.getBusinessSymbol(), gshareCommon.getShareType(),
				0, 150, gshareCommon.getUnitPrice());
		assertTrue(google.issueShares(aSO));
		assertFalse(google.issueShares(aSO));
	}

	@Test
	public void TestGetShareInfo() {
		Business google = new Business(Config.getInstance().getAttr("GOOG"));

		assertTrue(google.getShareInfo(ShareType.PREFERRED) != null);
		assertTrue(google.getShareInfo(ShareType.COMMON) != null);
		assertTrue(google.getShareInfo(ShareType.CONVERTIBLE) != null);
	}

	@Test
	public void TestGetShareList() {
		Business google = new Business(Config.getInstance().getAttr("GOOG"));

		List<Share> shares = google.getSharesList();
		shares.forEach((share) -> {
			share.getBusinessSymbol();
			assertTrue(share.getBusinessSymbol().contains("GOOG"));
			ShareType type = share.getShareType();
			assertTrue(type == ShareType.COMMON || type == ShareType.PREFERRED
					|| type == ShareType.CONVERTIBLE);

		});
	}

	@Test
	public void TestLaunchFourBusinessServers() throws InterruptedException {
		Thread[] servers = { BusinessServer.launch("GOOG"),
				BusinessServer.launch("APPL"), BusinessServer.launch("YHOO"),
				BusinessServer.launch("MSFT") };

		Thread.sleep(5000); // give the thread a whole three seconds to launch

		// test passes if servers are still running
		for (Thread s : servers) {
			assertTrue(s.isAlive());
			s.interrupt();
			System.out.println("Killed a thread.");
		}
	}

}