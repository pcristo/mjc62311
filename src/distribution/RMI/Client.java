package distribution.RMI;

import common.logger.LoggerClient;
import common.util.Config;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client<T extends Remote> {

    /**
     *
     * @param ip String of RMI server
     * @param port int of port service is bound on
     * @param serviceName String name of service
     * @return Generic object found at the server
     * @throws RemoteException
     * @throws NotBoundException
     */
    public T getService(String ip, int port, String serviceName) throws RemoteException, NotBoundException{

        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Registry registry = LocateRegistry.getRegistry(ip, port);
        T server = (T) registry.lookup(serviceName);

        LoggerClient.log("Interface " + serviceName + " found on " + port);
        return server;
    }


}