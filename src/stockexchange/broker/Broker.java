package stockexchange.broker;

import client.Customer;
import stockexchange.*;
import util.Config;

import java.rmi.RemoteException;
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

    private static Exchange exchange;
    private static ArrayList<String> tickers;

    public static void main(String[] args) {
        Broker broker = new Broker();
        broker.startRMIServer("brokerService", 1097);
    }
    /**
     * Create broker class, point him to the exhcnage he trades on
     * TODO use multiple exchanges
     */
    public Broker() {
        exchange = new Exchange();
    }

    /**
     * @return list of company tickers on the stock exchange
     * TODO multiple exchanges
     */
    @Override
    public ArrayList<String> getTickerListing() throws RemoteException {
        return exchange.getListing();
    }


    /**
     * Sell Shares
     *
     * @param tickers  arraylist that need to be sold
     * @param type     type that the tickers belong to
     * @param quantity that wants to be sold
     * @param customer customer who made the request
     * @return
     */
    @Override
    public boolean sellShares(ArrayList<String> tickers, String type, int quantity, Customer customer) throws RemoteException {
        for (String ticker : tickers) {
            if (validateClientHasShare(ticker, customer)) {
                // We cant sell what we dont have
                return false;
            }
        }
        ShareList sharesToSell = prepareTrade(tickers, type, quantity);
        if (sharesToSell != null) {
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
     * @param tickers  arraylist that need to be bought
     * @param type     type that the tickers belong to
     * @param quantity that wants to be bought
     * @param customer customer who made the request
     * @return
     */
    @Override
    public boolean buyShares(ArrayList<String> tickers, String type, int quantity, Customer customer) throws RemoteException {
        ShareList sharesToBuy = prepareTrade(tickers, type, quantity);
        if (sharesToBuy != null) {
            // WTF do i do with this?
            ShareSalesStatusList boughtShares = exchange.buyShares(sharesToBuy, customer);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Prepare the trade to go to the exchange
     *
     * @param tickers  involved in transaction
     * @param type     of stocks being traded
     * @param quantity amount of stocks
     * @return a shareList used by exchange or null if validation fail
     */
    private ShareList prepareTrade(ArrayList<String> tickers, String type, int quantity) {
        // Prepare shares to action - honestly this should be done a share at a time
        ArrayList<ShareItem> sharesToAction = new ArrayList<ShareItem>();
        for (String ticker : tickers) {
            if (!validateTicker(ticker)) {
                // We don't trade anything unless all tickers are valid
                return null;
            } else {
                // TODO price needs to come from the exchange...thats what it is..an exchange
                // for now we fake the price at 50
                // TODO add on broker comission for customer
                float price = 50;
                String orderNumber = "";
                sharesToAction.add(new ShareItem(orderNumber, ticker, type, price, quantity));
            }
        }

        // Another list...why not just arrayList?
        ShareList sharesToSellObj = new ShareList(sharesToAction);
        return sharesToSellObj;


    }

    /**
     * Make sure this customer owns the share they are trying to sell
     *
     * @param ticker
     * @return
     */
    private boolean validateClientHasShare(String ticker, Customer customer) {
        ArrayList<ShareItem> customerShares = exchange.getShares(customer);
        for (ShareItem share : customerShares) {
            if (share.getBusinessSymbol() == ticker) {
                return true;
            }
        }
        return false;

    }

    /**
     * Ensure the ticker is a valid ticker listed on the exchange
     *
     * @param ticker
     * @return
     */
    private boolean validateTicker(String ticker) {
        ArrayList<String> tickerListing = exchange.getListing();
        return tickerListing.contains(ticker);
    }

    /**
     * Start RMI server with given service name and port number
     *
     * @param serviceName
     * @param portNum
     */
    public void startRMIServer(String serviceName, int portNum) {

        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());


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
            System.out.println(serviceName + " bound on " + portNum);
        } catch (Exception e) {
            System.err.println("broker service creation exception:");
            e.printStackTrace();
        }
    }
}
