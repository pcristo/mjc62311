package client;

import share.ShareType;
import stockexchange.*;
import stockexchange.broker.BrokerInterface;
import util.Config;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A simple client for stock broker client
 * Created by sai sun on 5/25/2015.
 */
public class BrokerServiceClient {

    private static ArrayList<String> tickers;
    private final String pri_serviceName;
    private final int pri_port;

    public static void main(String args[]) {

        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            BrokerServiceClient client = new BrokerServiceClient("brokerService",1099);
            Registry registry = LocateRegistry.getRegistry(client.getPort());
            BrokerInterface service = (BrokerInterface) registry.lookup(client.getServiceName());
            tickers = new ArrayList<String>();

            boolean terminate = false;
            while (!terminate) {
                ServerDisplayMsgs.printWelcome();
                //operation switch
                Customer customer;
                ShareType tickerType;
                int tickerQuantity;
                boolean ret = false;
                Scanner scan = new Scanner(System.in);
                switch (ServerDisplayMsgs.printOps()) {
                    case 1:
                        customer = getCustomer(ServerDisplayMsgs.getCustomerInfo());
                        tickerType = ServerDisplayMsgs.enterTickerType();
                        tickerQuantity = ServerDisplayMsgs.enterTickerQuantity();
                        ret = service.sellShares(ServerDisplayMsgs.getTickerList(),tickerType,tickerQuantity,customer);
                        ServerDisplayMsgs.printResult(ret);
                        System.out.println("Press enter key to continue...");
                        scan.nextLine();
                        break;
                    case 2:
                        customer = getCustomer(ServerDisplayMsgs.getCustomerInfo());
                        tickerType = ServerDisplayMsgs.enterTickerType();
                        tickerQuantity = ServerDisplayMsgs.enterTickerQuantity();
                        ret = service.buyShares(ServerDisplayMsgs.getTickerList(), tickerType, tickerQuantity, customer);
                        ServerDisplayMsgs.printResult(ret);
                        System.out.println("Press enter key to continue...");
                        scan.nextLine();
                        break;
                    case 3:
                        tickers = service.getTickerListing();
                        ServerDisplayMsgs.printList("Ticker Listing Info:", tickers);

                        System.out.println("Press enter key to continue...");
                        scan.nextLine();
                        break;
                    case 4:
                        terminate = true;
                        break;
                    default:
                        terminate = true;
                        break;
                }
                ServerDisplayMsgs.flushConsole();

            }
/*            ShareList ShareList = new ShareList(createListofShares());
            Customer newCust = new Customer(1,"Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );*/
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }


    private static Customer getCustomer(String info) {
        return new Customer(Integer.parseInt(info.split(";;d")[0]), info.split(";;d")[1], info.split(";;d")[2], info.split(";;d")[3], info.split(";;d")[4], info.split(";;d")[5], info.split(";;d")[6], info.split(";;d")[7]);
    }

    private static ArrayList<ShareItem> createListofShares() {

        //ShareItemList
        ArrayList<ShareItem> lstShares = new ArrayList<ShareItem>();

//        lstShares.add(new ShareItem("MSFT.B.B", "convertible", 523.32f, 100));
//        lstShares.add(new ShareItem("MSFT.C","preferred",541.28f,200));
//        lstShares.add(new ShareItem("GOOG","common",540.11f,100));

        return lstShares;

    }

    public BrokerServiceClient(String service, int port)
    {
        pri_serviceName = service;
        pri_port = port;
    }

    public String getServiceName()
    {
        return pri_serviceName;
    }

    public int getPort()
    {
        return pri_port;
    }
}
