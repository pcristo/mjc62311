package client;

/**
 * A simple client for business service
 * Created by Sai on 2015/5/23.
 */

import share.Share;
import business.BusinessServerInterface;
import util.Config;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * A simple client for business server,
 * put "-Djava.security.manager -Djava.security.policy=resources/settings/security.policy"
 * in VM option to run
 */
public class BusinessServiceClient {

    private final String pri_serviceName;
    private final int pri_port;

    public static void main(String args[]) {

        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());


        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            BusinessServiceClient client = new BusinessServiceClient("stockService", 1099);
            Registry registry = LocateRegistry.getRegistry(client.getPort());
            BusinessServerInterface service = (BusinessServerInterface) registry.lookup(client.getServiceName());
            Share info = service.getShareInfo("common");
            System.out.println(info);
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }
    
    public BusinessServiceClient(String service, int port)
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
