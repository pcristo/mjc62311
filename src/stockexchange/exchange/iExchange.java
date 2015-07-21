package stockexchange.exchange;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 * Created by Gay on 7/20/2015.
 */
@WebService
public interface iExchange {

    // update share price
    @WebMethod
    boolean updateSharePrice(String symbol, float price);
    @WebMethod
    boolean registerBusiness(String symbol, float price);
    @WebMethod
    boolean unregister(String symbol);
}
