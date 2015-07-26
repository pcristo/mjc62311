import FrontEnd.FrontEnd;
import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;
import WebServices.ExchangeClientServices.ShareItem;
import WebServices.Rest;
import business.BusinessWSPublisher;
import common.Customer;
import common.util.Config;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import stockexchange.exchange.ExchangeWSPublisher;

import java.util.HashMap;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertEquals;


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


    @Before
    public void setUp() throws Exception{
        startServers();
    }

    @After
    public void tearDown() {
        stopServers();
    }
    /**
     * Start servers
     * @throws Exception 
     */
    private void startServers() throws Exception {
        ExchangeWSPublisher.main(null);
        
		BusinessWSPublisher.createBusiness("GOOG");
		BusinessWSPublisher.createBusiness("YHOO");
		BusinessWSPublisher.createBusiness("AAPL");
		BusinessWSPublisher.createBusiness("MSFT");
        BusinessWSPublisher.StartAllWebservices();
        BusinessWSPublisher.RegisterAllWithExchange();
    }

    /**
     * Stop the servers
     */
    private void stopServers() {
        ExchangeWSPublisher.unload();
        BusinessWSPublisher.unload();
    }

    @Test
    public void testBrokerRest() throws Exception {

        // Make a bad rest call
        String url = "http://example.com/";
        String data = Rest.getPost(url, new HashMap<String, String>());
        assertNull(data);

        // Now make valid request bud with bad params
        url = Config.getInstance().getAttr("BrokerRest", true);
        data = Rest.getPost(url, new HashMap<String, String>());
        assertNull(data);

        // Now make valid request with all the params
        String name = "Ross";
        Customer ross = new Customer(name);
        data = Rest.getPost(url, new HashMap<String, String>() {{
            put("ticker", "GOOG");
            put("type", "COMMON");
            put("qty", "34");
            put("customer", new ObjectMapper().writeValueAsString(ross));
        }});

        assertNotNull(data);
        // BrokerRest returns customers name in data
        assertEquals(name, data);

    }



    @Test
    public void testFESellShares() {
        boolean result = FrontEnd.sellShares("GOOG", "COMMON", 500, new Customer("Ross"));
        assertTrue(result);
    }


    @Test
    public void testExchangeSellShares() {
        ShareItem toBuy = new ShareItem();
        toBuy.setBusinessSymbol("GOOG");
        toBuy.setQuantity(100);
        toBuy.setShareType(WebServices.ExchangeClientServices.ShareType.COMMON);
        toBuy.setUnitPrice(500.00f);

        WebServices.ExchangeClientServices.Customer newCust = new WebServices.ExchangeClientServices.Customer();
        newCust.setName("Gay");

        ExchangeWSImplService service = new ExchangeWSImplService();
        IExchange iExchange = service.getExchangeWSImplPort();

        assertTrue(iExchange.sellShareService(toBuy, newCust));
    }

}
