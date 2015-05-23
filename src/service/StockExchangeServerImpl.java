package service;

import business.Share;
import business.ShareOrder;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static java.rmi.registry.LocateRegistry.createRegistry;

/**
 * Stock exchange front end service provider, creates remote object in the registery.
 * Created by Sai on 2015/5/23.
 */
public class StockExchangeServerImpl implements StockExchangeServerInterface{

    public StockExchangeServerImpl() {

    }

    @Override
    public boolean issueShares(ShareOrder aSP) throws RemoteException {
        return false;
    }

    @Override
    public Share getShareInfo(String aShareType) throws RemoteException {
        return null;
    }

    @Override
    public boolean receivePayment(String orderNum, float totalPrice) throws RemoteException {
        return false;
    }

    public static void main(String[] args)
    {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "stockService";
            StockExchangeServerInterface service = new StockExchangeServerImpl();
            //create local rmi registery
            createRegistry(1099);
            //bind service to default port 1099
            StockExchangeServerInterface stub =
                    (StockExchangeServerInterface) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("stockService bound");
        } catch (Exception e) {
            System.err.println("stockService exception:");
            e.printStackTrace();
        }
    }

}
