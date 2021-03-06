
package WebServices.ExchangeClientServices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IExchange", targetNamespace = "http://exchange.stockexchange/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IExchange {


    /**
     * 
     * @param symbol
     * @param price
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://exchange.stockexchange/IExchange/registerBusinessRequest", output = "http://exchange.stockexchange/IExchange/registerBusinessResponse")
    public boolean registerBusiness(
            @WebParam(name = "symbol", partName = "symbol")
            String symbol,
            @WebParam(name = "price", partName = "price")
            float price,
            @WebParam(name = "port", partName = "port")
            String port);

    /**
     * 
     * @param item
     * @param quantity
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://exchange.stockexchange/IExchange/updateSharePriceRequest", output = "http://exchange.stockexchange/IExchange/updateSharePriceResponse")
    public boolean updateSharePrice(
            @WebParam(name = "item", partName = "item")
            String item,
            @WebParam(name = "quantity", partName = "quantity")
            float quantity);

    /**
     * 
     * @param symbol
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://exchange.stockexchange/IExchange/unregisterRequest", output = "http://exchange.stockexchange/IExchange/unregisterResponse")
    public boolean unregister(
            @WebParam(name = "symbol", partName = "symbol")
            String symbol);

    /**
     * 
     * @param share
     * @param info
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://exchange.stockexchange/IExchange/sellShareServiceRequest", output = "http://exchange.stockexchange/IExchange/sellShareServiceResponse")
    public boolean sellShareService(
            @WebParam(name = "share", partName = "share")
            ShareItem share,
            @WebParam(name = "info", partName = "info")
            Customer info);

}
