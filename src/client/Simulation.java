package client;

import logger.LoggerClient;
import share.ShareType;
import stockexchange.broker.BrokerInterface;

import java.util.ArrayList;

/**
 * Created by Ross on 2015-06-07.
 */
public class Simulation implements Runnable{

    BrokerInterface broker;
    ShareType type;
    ArrayList<String> tickers;
    Customer customer;

    public Simulation(BrokerInterface broker, ShareType type, String ticker, String customerName) {
        this.broker = broker;
        this.type = type;
        this.tickers = new ArrayList<>();
        tickers.add(ticker);
        this.customer = new Customer(customerName);
    }

    public void run() {
        LoggerClient.log("SIMULATION THREAD STARTED - CUSTOMER NAME IS " + customer.getName());
        try {
            boolean sell = broker.sellShares(tickers, type, 100, customer);
            if(!sell) {
                LoggerClient.log("SIMULATION THREAD - " + customer.getName() + " SELLING " + tickers.get(0) + ": FAILED!");
            } else {
                LoggerClient.log("SIMULATION THREAD - " + customer.getName() + " SELLING " + tickers.get(0) + ": SUCCEEDED!");
            }
            LoggerClient.log("SIMULATION THREAD ENDED");
        }catch(Exception e) {
            System.out.println("Exception in thread: " + customer.getName() + " selling " + tickers.get(0));
        }
    }


}
