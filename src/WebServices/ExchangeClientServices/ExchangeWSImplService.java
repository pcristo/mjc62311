
package WebServices.ExchangeClientServices;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "", targetNamespace = "", wsdlLocation = "")
public class ExchangeWSImplService extends Service
{

    /**
     * This is the only constructor supported for the Business implementation. You must specify a stock
     * symbol when creating your interface object.
     * @param exchangeName
     * @throws MalformedURLException
     */
    public ExchangeWSImplService(String exchangeName, String port) throws MalformedURLException {
        super(new URL("http://localhost:"+port+"/WS/" + exchangeName + "?wsdl"),
                new QName("http://exchange.stockexchange/", "ExchangeWSImplService"));
    }

    /**
     * 
     * @return
     *     returns IExchange
     */
    @WebEndpoint(name = "ExchangeWSImplPort")
    public IExchange getExchangeWSImplPort() {
        return super.getPort(new QName("http://exchange.stockexchange/", "ExchangeWSImplPort"), IExchange.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IExchange
     */
    @WebEndpoint(name = "ExchangeWSImplPort")
    public IExchange getExchangeWSImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://exchange.stockexchange/", "ExchangeWSImplPort"), IExchange.class, features);
    }
}
