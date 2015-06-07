package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import logger.LoggerClient;

import share.Share;
import share.ShareOrder;
import share.ShareType;
import util.Config;
import business.Business;
import business.BusinessInterface;

public class MockBusiness implements BusinessInterface
{
	private String m_ticker = "";

	public MockBusiness(String ticker)
	{
		m_ticker  = ticker;
	}
	
	public void startMock()
	{
		BusinessInterface business = new MockBusiness(m_ticker);

		try {
			// Reserver port 9095 - 9099 for business services
			Business.startRMIServer(business, m_ticker, 9095);
		} catch(RemoteException rme) {
			System.out.println(rme.getMessage());
			LoggerClient.log("Remote Exception in Business Server: " + rme.getMessage());
		}
	}

	@Override
	public boolean issueShares(ShareOrder aSO) throws RemoteException
	{
		
		return false;
	}

	@Override
	public Share getShareInfo(ShareType aShareType) throws RemoteException
	{
		
		return null;
	}

	@Override
	public List<Share> getSharesList() throws RemoteException
	{
		
		return null;
	}

	@Override
	public boolean recievePayment(String orderNum, float totalPrice)
			throws RemoteException
	{
		
		return false;
	}

	@Override
	public String getTicker() throws RemoteException
	{
		return m_ticker;
	}
	
	/**
	 *
	 * @param business interface to be bound
	 * @param businessName to bind to
	 * @param port to bind business to
	 * @throws RemoteException
	 */
	public static void startRMIServer(BusinessInterface business, String businessName, int port) throws RemoteException {

		//TODO remove this.  See updated Config class.
		//System.setProperty("java.security.policy", Config.getInstance().loadMacSecurityPolicy());

		System.setProperty("java.security.policy", Config.getInstance()
				.loadSecurityPolicy());

		// load security policy
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		// create local rmi registery
		LocateRegistry.createRegistry(port);

		// bind service to default port portNum
		BusinessInterface stub = (BusinessInterface) UnicastRemoteObject
					.exportObject(business, port);
		Registry registry = LocateRegistry.getRegistry(port);
		registry.rebind(businessName, stub);
		System.out.println(businessName + " bound on " + port);
		LoggerClient.log(businessName + " server bound on " + port);
	}

}
