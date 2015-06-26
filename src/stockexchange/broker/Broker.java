package stockexchange.broker;

import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareType;
import exchangeServer.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import stockexchange.exchange.Exchange;
import stockexchange.exchange.ShareItem;
import stockexchange.exchange.ShareList;
import stockexchange.exchange.ShareSalesStatusList;
import common.util.Config;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static exchangeServer.CORBAShareType.COMMON;

/**
 * Broker class takes customer request and validates it and sends it over
 * to stock exchange
 */
public class Broker extends BrokerInterfacePOA implements Serializable{

    // Require for serialization.  Ensures object deserialized and serialized are the same.
    private static final long serialVersionUID = 1467890432560789065L;

    // TODO multiple exchanges
    protected static Exchange exchange;
    private ORB m_orb;

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

    }

    public void setORB(ORB orb_val) {
        m_orb = orb_val;
    }

    public static void startService(String serviceName)
    {
        try
        {
            Properties props = System.getProperties();
            props.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
            props.put("org.omg.CORBA.ORBInitialHost", "localhost");
            // Create and initialize the ORB
            ORB orb = ORB.init(new String[]{}, null);
            POA rootpoa = POAHelper.narrow(orb
                    .resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            String csv = Config.getInstance().getAttr(serviceName);
            Broker m_server = new Broker();
            m_server.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(m_server);

            BrokerInterface srf =BrokerInterfaceHelper.narrow(ref);

            // get the root naming context
            org.omg.CORBA.Object objRef = orb
                    .resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            //bind references to names in naming service

            NameComponent path[] = ncRef.to_name(serviceName);
            ncRef.rebind(path, srf);
            System.out.println("Server is ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        } catch (Exception e)
        {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
    }


    /**
     * Create broker class, point him to the exhcnage he trades on
     * TODO use multiple exchanges
     */
    public Broker() throws RemoteException, NotBoundException{
        exchange = getExchange();
    }


    /**
     * This is a seperate method than consturctor to enable
     * sub classes to override (see MockBroker)
     * @return Exchange object
     */
    protected Exchange getExchange() throws RemoteException, NotBoundException {
        return new Exchange();
    }

    @Override
    public boolean buyShares(String[] list, CORBAShareType type, int quantity, CORBACustomer customer) {
        for(String ticker : list) {
            validateClientHasShare(ticker, convertCustomer(customer));
        }
        ShareList sharesToBuy = prepareTrade(new ArrayList(Arrays.asList(list)), backConvertShareType(type), quantity);
        if (sharesToBuy != null) {
            exchange.buyShares(sharesToBuy,convertCustomer( customer));


            return true;
        } else {
            return false;
        }
    }

    private ShareType backConvertShareType(CORBAShareType sharetype)
    {
        switch (sharetype.toString())
        {
            case "COMMON":
                return ShareType.COMMON;
            case "CONVERTIBLE":
                return ShareType.CONVERTIBLE;
            case "PREFERRED":
                return ShareType.PREFERRED;
            default:
                return ShareType.PREFERRED;
        }
    }

    private Customer convertCustomer(CORBACustomer c)
    {
        Customer customer = new Customer(c.name,c.street1,c.street2,c.city,c.province,c.postalCode,c.country);
        return customer;
    }

    @Override
    public boolean sellShares(String[] list, CORBAShareType type, int quantity, CORBACustomer customer) {
        ShareList sharesToSell = prepareTrade(new ArrayList(Arrays.asList(list)), backConvertShareType(type), quantity);

        if (sharesToSell != null) {
            ShareSalesStatusList shareSatusList = exchange.sellShares(sharesToSell, convertCustomer(customer));
            if(shareSatusList.getShares(convertCustomer(customer)) == null || shareSatusList.getShares(convertCustomer(customer)).isEmpty()){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * @return list of company tickers on the stock exchange
     * TODO multiple exchanges
     */
    @Override
    public String[] getTickerListing(){
        return (String[]) exchange.getListing().toArray();
    }

    public String getBusinessTicker(String businessName){
        return exchange.getBusinessTicker(businessName);
    }

    @Override
    public boolean sellShareList(CORBAShareItem[] lstShares, CORBACustomer customer) {
        ShareList customerShares = new ShareList(new ArrayList(Arrays.asList(lstShares)));

        ShareSalesStatusList shareSalesStatusList = exchange.sellShares(customerShares,convertCustomer(customer));

        if (shareSalesStatusList != null)
            return true;

        return false;
    }

    /**
     * Broker sells shares
     * Customer Buys shares
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
     * Broker buys shares
     * Customer sells shares
     *
     * @param tickers  arraylist that need to be bought
     * @param type     type that the tickers belong to
     * @param quantity that wants to be bought
     * @param customer customer who made the request
     * @return
     */
    public boolean buyShares(ArrayList<String> tickers, ShareType type, int quantity, Customer customer) throws RemoteException {
        for(String ticker : tickers) {
            validateClientHasShare(ticker, customer);
        }
        ShareList sharesToBuy = prepareTrade(tickers, type, quantity);
        if (sharesToBuy != null) {
            exchange.buyShares(sharesToBuy, customer);


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
        // Prepare shares to action - honestly this should be done a common.share at a time
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
     * Make sure this customer owns the common.share they are trying to sell
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
