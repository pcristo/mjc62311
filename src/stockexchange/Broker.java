package stockexchange;

//import com.sun.tools.classfile.Annotation;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import static java.rmi.registry.LocateRegistry.createRegistry;

/**
 * Created by Gay on 15-05-23.
 */
public class Broker implements BrokerInterface{

    private volatile ShareSalesStatusList pri_currentStatusList= null;

    // IS THIS THE EXCHANGE GUY?
    private synchronized static ShareSalesStatusList exchange;

    public static void main(String[] args){

        ShareList ShareList = new ShareList(createListofShares());

        //starting the RMI server
        startRMIServer("brokerService", 1099);
    }

    public Broker() {


    }



    /**
     *
     * @return list of available shares
     */
    public ArrayList<ShareItem> getShares() {

        ArrayList<ShareItem> listShares = new ArrayList<ShareItem>();
        listShares.add(new ShareItem("MSFT.B.B", "convertible", 523.32f, 100));
        listShares.add(new ShareItem("MSFT.C","preferred",541.28f,200));
        listShares.add(new ShareItem("GOOG","common",540.11f,100));
        return listShares;
    }


    @Override
    public ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) throws RemoteException {
        ShareSalesStatusList stockService = new ShareSalesStatusList().sellShares(shareItemList,info);
        pri_currentStatusList = stockService;
        return stockService;
    }

    /**
     * Start RMI server with given service name and port number
     * @param serviceName
     * @param portNum
     */
    private static void startRMIServer(String serviceName, int portNum)
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
