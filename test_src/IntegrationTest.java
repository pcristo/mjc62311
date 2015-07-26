import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;
import WebServices.ExchangeClientServices.ShareItem;
import stockexchange.exchange.ShareList;

import business.BusinessWSPublisher;
import common.Customer;
import common.share.ShareType;
import common.util.Config;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import java.util.HashMap;
import WebServices.Rest;
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
	
    private void startServers(boolean exchange, boolean business, boolean broker) {
        if(business) {
          BusinessWSPublisher.StartAllWebservices();
        }
       /* if(broker){
            threads.add(BrokerServer.launch());
        }*/
    }

    /**
     *
     * @param ArrayList of threads currently running and to be stopped
     */
    private void stopServers() {
        BusinessWSPublisher.unload();
    }

    @Test
    public void testBrokerRest() throws Exception{
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
    public void testSellShares() {

        ShareItem toBuy = new ShareItem();
        toBuy.setBusinessSymbol("GOOG");
        toBuy.setQuantity(100);
        toBuy.setShareType(WebServices.ExchangeClientServices.ShareType.COMMON);
        toBuy.setUnitPrice(500.00f);


        WebServices.ExchangeClientServices.Customer newCust = new WebServices.ExchangeClientServices.Customer();
        newCust.setName("Gay");


        ExchangeWSImplService service = new ExchangeWSImplService();
        IExchange iExchange = service.getExchangeWSImplPort();

        iExchange.sellShareService(toBuy,newCust);
    }


}
