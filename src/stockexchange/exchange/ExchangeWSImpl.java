package stockexchange.exchange;

import javax.jws.WebService;
/**
 * This class is an implementation of Exchange to include webservice endpoints
 */

@WebService(endpointInterface= "stockexchange.exchange.IExchange")
public class ExchangeWSImpl  extends Exchange implements IExchange {

    private static final long serialVersionUID = 1L;

    public ExchangeWSImpl() {super ();}
}
