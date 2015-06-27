package client.simulation;

import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareType;
import exchangeServer.BrokerInterface;
import exchangeServer.CORBACustomer;
import exchangeServer.CORBAShareType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class Simulation implements Runnable
{

	BrokerInterface broker;
	ShareType type;
	ArrayList<String> tickers;
	Customer customer;

	private boolean status;

	public Simulation(BrokerInterface broker, ShareType type, String ticker,
			String customerName)
	{
		this.broker = broker;
		this.type = type;
		this.tickers = new ArrayList<>();
		tickers.add(ticker);
		this.customer = new Customer(customerName);
	}

	public void run()
	{
		LoggerClient.log("SIMULATION THREAD STARTED - CUSTOMER NAME IS "
				+ customer.getName());
		try
		{
			ArrayList<String> tickers = new ArrayList(Arrays.asList(broker
					.getTickerListing()));
			System.out.println(tickers);
			if (tickers.size() != 9 || !tickers.contains("MSFT")
					|| !tickers.contains("GOOG") || !tickers.contains("YHOO"))
			{
				LoggerClient.log("FAIL!!!INCORRECT TICKERS RECIEVED!!!");
			} else
			{
				LoggerClient.log("TICKER RECIEVED SUCCESS!");
			}
			boolean sell = broker.sellShares(
					tickers.toArray(new String[tickers.size()]),
					convertShareType(type), 100, convertCustomer(customer));
			status = sell;
			if (!sell)
			{
				LoggerClient.log("FAIL!!!IMULATION THREAD - "
						+ customer.getName() + " SELLING " + tickers.get(0)
						+ ": FAILED!");
			} else
			{
				LoggerClient.log("SIMULATION THREAD - " + customer.getName()
						+ " SELLING " + tickers.get(0) + ": SUCCEEDED!");
			}
			LoggerClient.log("SIMULATION THREAD ENDED FOR "
					+ customer.getName());
		} catch (ConcurrentModificationException me)
		{
			me.printStackTrace();
			System.exit(-1);
		} catch (Exception e)
		{
			System.out.println("Exception in thread: " + customer.getName()
					+ " selling " + tickers.get(0));
			e.printStackTrace();
			// System.exit(-1);
		}
	}

	private CORBACustomer convertCustomer(Customer c)
	{
		CORBACustomer customer = new CORBACustomer(
				c.getCustomerReferenceNumber(), c.getName(), c.getStreet1(),
				c.getStreet2(), c.getCity(), c.getProvince(),
				c.getPostalCode(), c.getCountry());
		return customer;
	}

	private CORBAShareType convertShareType(ShareType sharetype)
	{
		switch (sharetype)
		{
		case COMMON:
			return CORBAShareType.COMMON;
		case CONVERTIBLE:
			return CORBAShareType.CONVERTIBLE;
		case PREFERRED:
			return CORBAShareType.PREFERRED;
		default:
			return CORBAShareType.PREFERRED;
		}
	}

	public boolean getStatus()
	{
		return status;
	}

}
