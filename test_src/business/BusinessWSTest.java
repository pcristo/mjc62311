package business;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.share.ShareOrder;
import common.share.ShareType;
import business.WSClient.BusinessWSImplService;

public class BusinessWSTest {
	static BusinessWSImplService bizws;
	static business.WSClient.IBusiness biz;
	
	static float buyPrice = 800;
	static int buyQuantity = 100;
	static String stock = "GOOG";	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BusinessWSPublisher.createBusiness(stock);
		BusinessWSPublisher.StartAllWebservices();
		BusinessWSPublisher.RegisterAllWithExchange();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		BusinessWSPublisher.unload();
	}

	@Before
	public void connectToWebService() throws MalformedURLException {
		bizws = new BusinessWSImplService(stock);
		biz = bizws.getBusinessWSImplPort();
	}
	
	@Test
	public void connectTest() throws MalformedURLException {
		// just test @Before method
	}
	
	@Test
	public void getTickerTest() {
		assertTrue(biz.getTicker().equals(stock));
	}
	
	@Test
	public void issueAndRecievePaymentTest() {
		assertTrue( biz.recievePayment(issueShares(), buyQuantity * buyPrice) );
	}		

	public String issueShares() {
		String orderNumber = LocalDateTime.now().toString();
		
		ShareOrder aSO = new ShareOrder(orderNumber, "TSX", "GOOG", ShareType.COMMON, 0, buyQuantity, buyPrice);
		
		assertTrue( biz.issueShares( aSO.toClientWS() ) );		
		
		return orderNumber;
	}

}
