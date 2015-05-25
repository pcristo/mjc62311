package business;

import util.Config;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static java.rmi.registry.LocateRegistry.createRegistry;

/**
 * Stock exchange front end service provider, creates remote object in the registery.
 * put "-Djava.security.manager -Djava.security.policy=resources/settings/security.policy"
 * in VM option to run
 * Created by Sai on 2015/5/23.
 */
public class BusinessServerImpl implements BusinessServerInterface {

    private Business pri_business;
    private String pri_serviceName = "";

    /**
     * Constructor
     * @param sourceData A CSV file that contains stock information
     * @param serviceName service name used to bind into java RMI registery for service lookup
     */
    public BusinessServerImpl(String sourceData, String serviceName) {
        pri_business = new Business(sourceData);
        pri_serviceName = serviceName;
    }

    @Override
    public boolean issueShares(ShareOrder aSP) throws RemoteException {
        return pri_business.issueShares(aSP);
    }

    @Override
    public Share getShareInfo(String aShareType) throws RemoteException {
        return pri_business.getShareInfo(aShareType);
    }

    @Override
    public boolean receivePayment(String orderNum, float totalPrice) throws RemoteException {
        return pri_business.recievePayment(orderNum,totalPrice);
    }

    public static void main(String[] args)
    {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {

            String serviceName = "stockService";
            String csv = Config.getInstance().getAttr("google");
            BusinessServerInterface service = new BusinessServerImpl(csv,serviceName);
            //create local rmi registery
            createRegistry(1099);
            //bind service to default port 1099
            BusinessServerInterface stub =
                    (BusinessServerInterface) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(serviceName, stub);
            System.out.println(serviceName + " bound");
        } catch (Exception e) {
            System.err.println("business service creation exception:");
            e.printStackTrace();
        }
    }

}
