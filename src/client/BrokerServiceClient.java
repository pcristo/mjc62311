package client;

import business.BusinessServerInterface;
import business.Share;
import stockexchange.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Created by sai sun on 5/25/2015.
 */
public class BrokerServiceClient {

    private final String pri_serviceName;
    private final int pri_port;

    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            BrokerServiceClient client = new BrokerServiceClient("stockService",1099);
            Registry registry = LocateRegistry.getRegistry(client.getPort());
            BrokerInterface service = (BrokerInterface) registry.lookup(client.getServiceName());
            ShareList ShareList = new ShareList(createListofShares());
            Customer newCust = new Customer(1,"Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );
            ShareSalesStatusList list = service.sellShares(ShareList,newCust);
            System.out.println(list);
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }

    private static ArrayList<ShareItem> createListofShares() {

        //ShareItemList
        ArrayList<ShareItem> lstShares = new ArrayList<ShareItem>();

        lstShares.add(new ShareItem("MSFT.B.B", "convertible", 523.32f, 100));
        lstShares.add(new ShareItem("MSFT.C","preferred",541.28f,200));
        lstShares.add(new ShareItem("GOOG","common",540.11f,100));

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
