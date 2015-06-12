package stockexchange.broker;

import client.Customer;
import logger.LoggerClient;
import share.ShareType;
import stockexchange.Exchange;
import stockexchange.ShareItem;
import stockexchange.ShareList;
import stockexchange.ShareSalesStatusList;
import util.Config;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Broker class takes customer request and validates it and sends it over
 * to stock exchange
 */
public class Broker implements BrokerInterface, Serializable{

    // Require for serialization.  Ensures object deserialized and serialized are the same.
    private static final long serialVersionUID = 1467890432560789065L;

    // TODO multiple exchanges
    private static Exchange exchange;
    private static ArrayList<String> tickers;

    /**
     * Start up Broker server
     *
     * Requires Business Server running
     */
    public static void main(String[] args) {
        /*
            Broker acts as a server and also calls Exchange which connects to
            Business server. Broker handles exceptions in creating Broker server and
            Exchange not connecting to Business.
        */
        try {
            Broker broker = new Broker();
            try {
                // Start Broker server.
                Broker.startRMIServer(broker);
            } catch(RemoteException rme) {
                LoggerClient.log("Remote Exception in Broker server: " + rme.getMessage());
            }
        // Exceptions for not being able to reach Business server through exchange.
        } catch (AccessException ae) {
            LoggerClient.log("Access Exception in creating Broker / Exchange.  " +
                        "Ensure Business server is running :: " + ae.getMessage());
        } catch (RemoteException rme) {
            LoggerClient.log("Remote Exception in creating Broker / Exchange." +
                    "Ensure Business server is running :: " + rme.getMessage());
        } catch (NotBoundException nbe) {
            LoggerClient.log("NotBound Exception in creating Broker / Exchange." +
                    "Ensure Business server is running :: " + nbe.getMessage());
        }
    }

    public static void startRMIServer(BrokerInterface broker) throws RemoteException {
         /** Start RMI Server **/

        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

        //load security policy
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Integer port = Integer.parseInt(Config.getInstance().getAttr("brokerPort"));

        //create local rmi registery
        LocateRegistry.createRegistry(port);

        //bind service to default port portNum
        BrokerInterface stub =
                    (BrokerInterface) UnicastRemoteObject.exportObject(broker, port);
        Registry registry = LocateRegistry.getRegistry(port);
        registry.rebind("broker", stub);
        LoggerClient.log("broker" + " bound on " + port);
        LoggerClient.log("All systems ready to go!");
    }

    /**
     * Create broker class, point him to the exhcnage he trades on
     * TODO use multiple exchanges
     */
    public Broker() throws AccessException, RemoteException, NotBoundException{
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

    public String getBusinessTicker(String businessName) throws RemoteException {
        return exchange.getBusinessTicker(businessName);
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
    public boolean sellShares(ArrayList<String> tickers, ShareType type, int quantity, Customer customer) throws RemoteException {
//        for (String ticker : tickers) {
//            if (validateClientHasShare(ticker, customer)) {
//                // We cant sell what we dont have
//                return false;
//            }
//        }
        ShareList sharesToSell = prepareTrade(tickers, type, quantity);

        if (sharesToSell != null) {
            ShareSalesStatusList shareSatusList = exchange.sellShares(sharesToSell, customer);
            if(shareSatusList.getShares(customer) == null || shareSatusList.getShares(customer).isEmpty()){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     *
     * @param shareItems
     * @param customer
     * @return
     * @throws RemoteException
     */
    public boolean sellShares(ArrayList<ShareItem> shareItems, Customer customer) throws RemoteException {

        ShareList customerShares = new ShareList(shareItems);

        ShareSalesStatusList shareSalesStatusList = exchange.sellShares(customerShares,customer);

        if (shareSalesStatusList != null)
            return true;

        return false;


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
    public boolean buyShares(ArrayList<String> tickers, ShareType type, int quantity, Customer customer) throws RemoteException {
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
    private ShareList prepareTrade(ArrayList<String> tickers, ShareType type, int quantity) {



        // Prepare shares to action - honestly this should be done a share at a time
        ArrayList<ShareItem> sharesToAction = new ArrayList<ShareItem>();
        for (String ticker : tickers) {
            if (!validateTicker(ticker)) {
                // We don't trade anything unless all tickers are valid
                return null;
            } else {

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
        List<ShareItem> customerShares = exchange.getShares(customer);
        synchronized (customerShares) {
            for (ShareItem share : customerShares) {
                if (share.getBusinessSymbol() == ticker) {
                    return true;
                }
            }
            return false;
        }
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


}
