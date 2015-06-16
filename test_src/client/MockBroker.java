package client;

import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareType;
import stockexchange.exchange.ShareItem;
import stockexchange.broker.BrokerInterface;
import common.util.Config;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MockBroker implements BrokerInterface
{

    private static Registry m_registry = null;
    private static BrokerInterface m_stub = null;

    @Override
	public boolean buyShares(ArrayList<String> ticker, ShareType type,
			int quantity, Customer customer) throws RemoteException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sellShares(ArrayList<String> ticker, ShareType type,
			int quantity, Customer customer) throws RemoteException
	{
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public boolean sellShares(ArrayList<ShareItem> customShares, Customer customer) throws RemoteException{

        return false;
    }

	@Override
	public ArrayList<String> getTickerListing() throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBusinessTicker(String businessName) throws RemoteException
	{
		String ret = "";
		switch(businessName)
        {
            case "google":
                ret = "GOOG";
                break;
            case "yahoo":
                ret = "YHOO";
                break;
            case "microsoft":
                ret ="MSFT";
                break;
        }
        return ret;
	}
	
	public void startMock()
	{
			MockBroker broker = new MockBroker();
            try {
                // Start Broker server.
            	MockBroker.startRMIServer(broker);
            } catch(RemoteException rme) {
                LoggerClient.log("Remote Exception in Broker server: " + rme.getMessage());
            }
	}
	
	public static void startRMIServer(BrokerInterface broker) throws RemoteException {
        /** Start RMI Server **/

       System.setProperty("java.security.policy", Config.getInstance().loadSecurityPolicy());

       //load security policy
       if (System.getSecurityManager() == null) {
           System.setSecurityManager(new SecurityManager());
       }

       Integer port = Integer.parseInt(Config.getInstance().getAttr("brokerPort"));

       //create local rmi registery
       LocateRegistry.createRegistry(port);

       //bind service to default port portNum
       m_stub = (BrokerInterface) UnicastRemoteObject.exportObject(broker, port);
       m_registry = LocateRegistry.getRegistry(port);
       m_registry.rebind("broker", m_stub);
       LoggerClient.log("broker" + " bound on " + port);
       LoggerClient.log("All systems ready to go!");
   }

    public static void stopRegistry()
    {
        try {
            UnicastRemoteObject.unexportObject(m_stub, true);
        } catch (NoSuchObjectException e) {

        }
    }

    public static BrokerInterface getInterface()
    {
        return m_stub;
    }

}
