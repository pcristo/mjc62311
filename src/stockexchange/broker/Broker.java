package stockexchange.broker;

import client.Customer;
import stockexchange.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import static java.rmi.registry.LocateRegistry.createRegistry;

/**
 * Broker class takes customer request and validates it and sends it over
 * to stock exchange
 */
public class Broker implements BrokerInterface {


    // TODO multiple exchanges
    private static ShareSalesStatusList exchange;

    public static void main(String[] args){
        Broker broker = new Broker();
        broker.startRMIServser("brokerService", 1099);
    }

    /**
     * Create broker class, point him to the exhcnage he trades on
     * TODO use multiple exchanges
     */
    public Broker() {
        exchange = new ShareSalesStatusList();
    }

    /**
     *
     * @return list of company tickers on the stock exchange
     * TODO multiple exchanges
     */
    public ArrayList<String> getTickerListing() {
         return exchange.getListing();
    }


    /**
     * Sell Shares
     *
     * @param tickers arraylist that need to be sold
     * @param type type that the tickers belong to
     * @param quantity that wants to be sold
     * @param customer customer who made the request
     * @return
     */
    public boolean sellShares(ArrayList<String> tickers, String type, int quantity, Customer customer) {
        for(String ticker : tickers) {
            if(validateClientHasShare(ticker)) {
                // We cant sell what we dont have
                return false;
            }
        }
        ShareList sharesToSell = prepareTrade(tickers, type, quantity);
        if(sharesToSell != null ){
            // WTF do i do with this?
            ShareSalesStatusList shareSatusList = exchange.sellShares(sharesToSell, customer);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Buy Shares
     *
     * @param tickers arraylist that need to be bought
     * @param type type that the tickers belong to
     * @param quantity that wants to be bought
     * @param customer customer who made the request
     * @return
     */
    public boolean buyShares(ArrayList<String> tickers, String type, int quantity, Customer customer) {
        ShareList sharesToBuy = prepareTrade(tickers, type, quantity);
        if(sharesToBuy != null) {
            // WTF do i do with this?
            ShareSalesStatusList boughtShares = exchange.buyShares(sharesToBuy, customer);
            return true;
        } else {
            return false;
        }

    }

    /**
     *
     * Prepare the trade to go to the exchange
     *
     * @param tickers involved in transaction
     * @param type of stocks being traded
     * @param quantity amount of stocks
     * @return a shareList used by exchange or null if validation fail
     */
    private ShareList prepareTrade(ArrayList<String> tickers, String type, int quantity) {
        // Prepare shares to action - honestly this should be done a share at a time
        ArrayList<ShareItem> sharesToAction = new ArrayList<ShareItem>();
        for(String ticker : tickers) {
            if(!validateTicker(ticker)) {
                // We don't trade anything unless all tickers are valid
                return null;
            } else {
                // TODO price needs to come from the exchange...thats what it is..an exchange
                // for now we fake the price at 50
                // TODO add on broker comission for customer
                float price = 50;
                sharesToAction.add(new ShareItem(ticker, type, price, quantity));
            }
        }

        // Another list...why not just arrayList?
        ShareList sharesToSellObj = new ShareList(sharesToAction);
        return sharesToSellObj;


    }

    /**
     * Make sure this customer owns the share they are trying to sell
     * @param ticker
     * @return
     */
    private boolean validateClientHasShare(String ticker) {
        // TODO implement
        return true;
    }

    /**
     * Ensure the ticker is a valid ticker listed on the exchange
     * @param ticker
     * @return
     */
    private boolean validateTicker(String ticker) {
        // TODO Call stock exchange and validate that the ticker exists
        return true;
    }

    /**
     * Start RMI server with given service name and port number
     * @param serviceName
     * @param portNum
     */
    public void startRMIServser(String serviceName, int portNum)
    {
        //load security policy
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            BrokerInterface service = new Broker();
            //create local rmi registery
            createRegistry(portNum);
            //bind service to default port portNum
            BrokerInterface stub =
                    (BrokerInterface) UnicastRemoteObject.exportObject(service, portNum);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(serviceName, stub);
            System.out.println(serviceName + " bound");
        } catch (Exception e) {
            System.err.println("broker service creation exception:");
            e.printStackTrace();
        }
    }
}