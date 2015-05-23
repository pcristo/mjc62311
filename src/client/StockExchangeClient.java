package client;

/**
 * A simple client
 * Created by Sai on 2015/5/23.
 */

import business.Share;
import service.StockExchangeServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class StockExchangeClient {

    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "stockService";
            Registry registry = LocateRegistry.getRegistry(1099);
            StockExchangeServerInterface service = (StockExchangeServerInterface) registry.lookup(name);
            Share info = service.getShareInfo("common");
            System.out.println(info);
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }
}
