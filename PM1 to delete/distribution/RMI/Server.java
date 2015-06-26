package distribution.RMI;


import common.logger.LoggerClient;
import common.util.Config;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server<T extends Remote> {

    /**
     *
     * @param objectInterface Generic interface of server wishing to start
     * @param serviceName String name of service to bind to
     * @param port int to bind on
     * @throws RemoteException
     */
    public void start(T objectInterface, String serviceName, int port) throws RemoteException {

        System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

        //load security policy
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        //create local rmi registry
        LocateRegistry.createRegistry(port);

        //bind service to default port portNum
        T stub =
                (T) UnicastRemoteObject.exportObject(objectInterface, port);
        Registry registry = LocateRegistry.getRegistry(port);
        registry.rebind(serviceName, stub);
        LoggerClient.log(serviceName + " bound on " + port);
    }

}
