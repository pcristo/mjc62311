package client;

/**
 * A simple client
 * Created by Sai on 2015/5/23.
 */

import business.Share;
import business.BusinessServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * A simple client for business server,
 * put "-Djava.security.manager -Djava.security.policy=resources/settings/security.policy"
 * in VM option to run
 */
public class BusinessServerClient {

    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "stockService";
            Registry registry = LocateRegistry.getRegistry(1099);
            BusinessServerInterface service = (BusinessServerInterface) registry.lookup(name);
            Share info = service.getShareInfo("common");
            System.out.println(info);
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }
}
