package stockexchange;
import stockexchange.exchange.Exchange;

import javax.xml.ws.Endpoint;

/**
 * Created by Gay on 7/20/2015.
 */
public class ExchangePublisher {

    public static void main(String[] args) {

        Endpoint.publish("http://localhost:8888/WS/Exchange", new Exchange());
    }

}
