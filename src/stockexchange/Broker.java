package stockexchange;

<<<<<<< HEAD
=======
//import com.sun.tools.classfile.Annotation;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
>>>>>>> origin/PM1

import static java.rmi.registry.LocateRegistry.createRegistry;

/**
 * Created by Gay on 15-05-23.
 */
public class Broker implements BrokerInterface{

    private volatile ShareSalesStatusList pri_currentStatusList= null;

    public static void main(String[] args){

<<<<<<< HEAD


=======
        ShareList ShareList = new ShareList(createListofShares());

        //starting the RMI server
        startRMIServer("brokerService", 1099);
>>>>>>> origin/PM1
    }


<<<<<<< HEAD
=======
        lstShares.add(new ShareItem("MSFT.B.B", "convertible", 523.32f, 100));
        lstShares.add(new ShareItem("MSFT.C","preferred",541.28f,200));
        lstShares.add(new ShareItem("GOOG","common",540.11f,100));

        return lstShares;

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
>>>>>>> origin/PM1
}
