import common.Customer;
import common.share.ShareOrder;
import common.share.ShareType;
import org.junit.Test;
import replication.FrontEnd;

/**
 * All Integration tests are done here
 *
 * These test consist of operations done by the exchange
 * And operations done by the broker (The last test represents a complete purchase of stocks)
 *
 * TO RUN THIS TEST MAKE SURE THE RIGHT SERVER AND WEBSERVICE IS AVAILABLE
 * SEE INDIVIDUAL METHODS
 */
public class IntegrationTest {

//    Thread thread;
//
//    @Before
//    public void setUp() throws Exception {
//        startServers();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        stopServers();
//    }
//    /**
//     * Start servers
//     * @throws Exception
//     */
//    private void startServers() throws Exception {
//        thread = new Thread() {
//            public void run() {
//                try {
//                    ExchangeWSPublisher.main(null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//        try {
//            Thread.sleep(1000);
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//        BusinessWSPublisher.createBusiness("GOOG");
//		BusinessWSPublisher.createBusiness("YHOO");
//		BusinessWSPublisher.createBusiness("AAPL");
//		BusinessWSPublisher.createBusiness("MSFT");
//        BusinessWSPublisher.StartAllWebservices();
//        BusinessWSPublisher.RegisterAllWithExchange();
//    }
//
//    /**
//     * Stop the servers
//     * @throws Exception
//     */
//    private void stopServers() throws Exception {
//       // ExchangeWSPublisher.unload();
//        BusinessWSPublisher.unload();
//
//        thread.interrupt();
//    }
//
//    @Test
//    public void testBrokerRest() throws Exception {
//
//        // Make a bad rest call
//        String url = "http://example.com/";
//        String data = Rest.getPost(url, new HashMap<String, String>());
//        assertNull(data);
//
//        // Now make valid request bud with bad params
//        url = Config.getInstance().getAttr("BrokerRest", true);
//        data = Rest.getPost(url, new HashMap<String, String>());
//        assertNull(data);
//
//        // Now make valid request with all the params
//        String name = "Ross";
//        Customer ross = new Customer(name);
//        data = Rest.getPost(url, new HashMap<String, String>() {
//			private static final long serialVersionUID = 1L;
//		{
//            put("ticker", "GOOG");
//            put("type", "COMMON");
//            put("qty", "34");
//            put("customer", new ObjectMapper().writeValueAsString(ross));
//        }});
//
//        assertNotNull(data);
//        // BrokerRest returns customers name in data
//        assertEquals(name, data);
//
//    }
//
//
//
//    @Test
//    public void testFESellShares() {
//        boolean result = frontEnd.sellShares("GOOG", "COMMON", 500, new Customer("Ross"));
//        assertTrue(result);
//
//
//        result = frontEnd.sellShares("YHOO", "COMMON", 500, new Customer("Ross"));
//        assertTrue(result);
//    }
//
//
//    @Test
//    public void testExchangeSellShares() throws Exception {
//        ShareItem toBuy = new ShareItem();
//        toBuy.setBusinessSymbol("GOOG");
//        toBuy.setQuantity(100);
//        toBuy.setShareType(WebServices.ExchangeClientServices.ShareType.COMMON);
//        toBuy.setUnitPrice(500.00f);
//
//        WebServices.ExchangeClientServices.Customer newCust = new WebServices.ExchangeClientServices.Customer();
//        newCust.setName("Gay");
//
//        ExchangeWSImplService service = new ExchangeWSImplService("TSX");
//        IExchange iExchange = service.getExchangeWSImplPort();
//
//        assertTrue(iExchange.sellShareService(toBuy, newCust));
//    }


    @Test
    public void testFE() {

        ShareOrder toBuy = new ShareOrder("Use sequence number", "Undefined",
            "GOOG", ShareType.valueOf("COMMON"), -1, Integer.parseInt("10"), Float.parseFloat("600"));

        Customer cust = new Customer("Ross");

        FrontEnd fe = new FrontEnd();
        fe.sendOrderAndWaitForReply(toBuy, cust);


    }


}
