package business;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import common.logger.LoggerServer;
import common.share.ShareType;
import common.util.Config;
import exchangeServer.ExchangeServerIF;
import stockexchange.exchange.Exchange;

public class BusinessServer implements Runnable
{
	private Business business;
	private String businessSymbol = "";
	public static Exchange m_exchange = null;

	/**
	 * Starts a server thread for a new business. The business will be the one
	 * specified by the businessSymbol property.
	 */
	public void run()
	{
		Business business = new Business(Config.getInstance().getAttr(
				businessSymbol));
		Thread t = new Thread(business);

		// get the CORBA interface for exchange server
		// make sure exchange server has started in prior to this
		ExchangeServerIF m_server = m_exchange.getExchangeServiceIFace();
		float price = business.getShareInfo(ShareType.COMMON).getUnitPrice();
		m_server.registerBusiness(businessSymbol, price);
		t.start();
		// keep the server running forever...
		while (true)
		{
			try
			{
				Thread.sleep(200);
			} catch (InterruptedException e)
			{
				// TODO log something
			}
		}
	}

	/**
	 * Sets the business symbol handled by this server. Can only be set once.
	 * 
	 * @param businessSymbol
	 */
	public void setBusinessSymbol(String businessSymbol)
	{
		if (this.businessSymbol.equals(""))
		{
			this.businessSymbol = businessSymbol;
			System.out.println("Set symbol " + businessSymbol); // TODO delete
																// this line
		}

	}

	/**
	 * Launches a new business server within a new thread.
	 * 
	 * @param symbol
	 *            The symbol of the business to launch.
	 * @return The thread that has been started
	 */
	public static Thread launch(String symbol)
	{
		BusinessServer instance = new BusinessServer();
		instance.setBusinessSymbol(symbol);

		Thread thread = new Thread(instance);
		thread.start();

		return thread;
	}
}
