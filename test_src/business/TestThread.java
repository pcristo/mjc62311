package business;

import client.Customer;
import stockexchange.broker.BrokerInterface;

/**
 * Created by Ross on 2015-06-07.
 */
public class TestThread implements Runnable {

    public String ticker;
    public Customer customer;
    BrokerInterface broker;

    public TestThread(String ticker, Customer customer, BrokerInterface broker) {
        this.ticker = ticker;
        this.customer = customer;
        this.broker = broker;


    }


    public void run() {
//        tickers = new ArrayList<String>();
//        tickers.add("MSFT");
//        customer = new Customer("Spiderman");
//        assertTrue(broker.sellShares(tickers, ShareType.COMMON, 100, customer));

    }
}
