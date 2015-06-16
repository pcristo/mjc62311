package client;

import business.BusinessInterface;
import common.logger.LoggerClient;
import common.share.Share;
import common.share.ShareOrder;
import common.share.ShareType;
import distribution.RMI.Server;

import java.rmi.RemoteException;
import java.util.List;

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
			Server<BusinessInterface> server = new Server<BusinessInterface>();
			server.start(business, m_ticker, 9095);
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


}
