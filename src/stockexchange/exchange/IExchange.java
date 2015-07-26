package stockexchange.exchange;
import common.Customer;


import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;

/**
 * Created by Gay on 7/20/2015.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IExchange {

    // update share price
    @WebMethod
    boolean updateSharePrice(
            @WebParam(name = "item") String symbol,
            @WebParam(name = "quantity") float price
    );

    @WebMethod
    boolean registerBusiness(
            @WebParam (name = "symbol")String symbol,
            @WebParam (name = "price") float price
    );

    @WebMethod
    boolean unregister(
            @WebParam (name = "symbol") String symbol
    );

    @WebMethod
    boolean sellShareService(
            @WebParam(name = "share") ShareItem shareItemList,
            @WebParam(name = "info") Customer info
    );
}
