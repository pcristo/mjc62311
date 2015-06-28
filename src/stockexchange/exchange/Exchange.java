package stockexchange.exchange;

import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;
import exchangeServer.*;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * The exchange class acts as an intermediary between businesses and stock
 * brokers. Brokers make requests to purchase stock from the exchange, which
 * then either sells existing shares to the broker or requests new shares be
 * issued from the business. Please note that the exchange assumes that all
 * share types within a business have the same ticker symbol and price.
 */
public class Exchange extends ExchangeServerIFPOA implements Runnable
{
	private static final int COMMISSION_MARKUP = 10;
	private static final int RESTOCK_THRESHOLD = 100;
	private static int orderInt = 1100;
	protected static ShareSalesStatusList shareStatusSaleList;

	/**
	 * Business directory that maps stock symbols to remote interfaces
	 */
	protected Map<String, BusinessInterface> businessDirectory = new HashMap<String, BusinessInterface>();

	/**
	 * Directory that maps stock symbols to stock prices
	 */
	protected Map<String, Float> priceDirectory = new HashMap<String, Float>();
	private org.omg.CORBA.ORB m_orb;
	private static Exchange m_instance;

	/**
	 * Create exchange object by preparing the local list of available shares
	 */
	public Exchange()
	{
		shareStatusSaleList = new ShareSalesStatusList();
		initializeShares();
	}

	@Override
	public BusinessInfo getBusiness(String businessName)
	{
		// TODO: implement real logic here
		return new BusinessInfo("google", CORBAShareType.COMMON, 1);
	}

	@Override
	public boolean updateSharePrice(String businessSymbol, float unitPrice)
	{
		// TODO: implement real logic here
		return false;
	}

	private void writeBusinessToFile()
	{
		try
		{
			// always clear the file content before writing
			new PrintWriter(Config.getInstance().getAttr("businessRecord"))
					.close();
			File file = new File(Config.getInstance().getAttr("businessRecord"));

			// if file doesnt exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (String key : businessDirectory.keySet())
			{
				// use ;;d as a separator
				bw.write(key + ";;d" + businessDirectory.get(key) + "\n");
			}

			bw.close();

			System.out.println("Updated business databse");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Registers a new business with the exchange, providing an initial price.
	 * 
	 * @param symbol
	 *            to enlist
	 * @param price
	 *            to make shares available at
	 * @throws NotBoundException
	 * @throws RemoteException
	 */
	@Override
	public boolean registerBusiness(String symbol, float price)
	{
		synchronized (businessDirectory)
		{
			businessDirectory.put(symbol, getBusinessIFace(symbol));
			priceDirectory.put(symbol, price);
			writeBusinessToFile();
		}

		return true;
	}

	public BusinessInterface getBusinessIFace(String symbol)
	{
		Properties props = System.getProperties();
		props.put("org.omg.CORBA.ORBInitialPort",
				Config.getInstance().getAttr("namingServicePort"));
		props.put("org.omg.CORBA.ORBInitialHost", "localhost");
		String[] args =
		{};
		ORB orb = ORB.init(args, null);

		// get the root naming context
		org.omg.CORBA.Object objRef = null;
		try
		{
			objRef = orb.resolve_initial_references("NameService");
		} catch (InvalidName invalidName)
		{
			invalidName.printStackTrace();
		}
		// Use NamingContextExt instead of NamingContext. This is
		// part of the Interoperable naming Service.
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		BusinessInterface m_serverIF = null;
		try
		{

			if (m_serverIF != null)
				m_serverIF._release();
			m_serverIF = BusinessInterfaceHelper.narrow(ncRef
					.resolve_str(symbol));
		} catch (NotFound | CannotProceed
				| org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		POA rootPOA = null;
		try
		{
			rootPOA = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
		} catch (InvalidName invalidName)
		{
			invalidName.printStackTrace();
		}
		// Resolve MessageServer
		NameComponent[] nc =
		{
			new NameComponent("MessageServer", "")
		};
		try
		{
			rootPOA.the_POAManager().activate();
		} catch (AdapterInactive adapterInactive)
		{
			adapterInactive.printStackTrace();
		}

		return m_serverIF;
	}

	public ExchangeServerIF getExchangeServiceIFace()
	{
		Properties props = System.getProperties();
		props.put("org.omg.CORBA.ORBInitialPort",
				Config.getInstance().getAttr("namingServicePort"));
		props.put("org.omg.CORBA.ORBInitialHost", "localhost");
		String[] args =
		{};
		ORB orb = ORB.init(args, null);

		// get the root naming context
		org.omg.CORBA.Object objRef = null;
		try
		{
			objRef = orb.resolve_initial_references("NameService");
		} catch (InvalidName invalidName)
		{
			invalidName.printStackTrace();
		}
		// Use NamingContextExt instead of NamingContext. This is
		// part of the Interoperable naming Service.
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		ExchangeServerIF m_serverIF = null;
		try
		{

			if (m_serverIF != null)
				m_serverIF._release();
			m_serverIF = ExchangeServerIFHelper.narrow(ncRef.resolve_str(Config
					.getInstance().getAttr("exchangeServiceName")));
		} catch (NotFound | CannotProceed
				| org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		POA rootPOA = null;
		try
		{
			rootPOA = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
		} catch (InvalidName invalidName)
		{
			invalidName.printStackTrace();
		}
		// Resolve MessageServer
		NameComponent[] nc =
		{
			new NameComponent("MessageServer", "")
		};
		try
		{
			rootPOA.the_POAManager().activate();
		} catch (AdapterInactive adapterInactive)
		{
			adapterInactive.printStackTrace();
		}

		return m_serverIF;
	}

	/**
	 * Used by CORBA
	 */
	public void setInstance(Exchange e)
	{
		m_instance = e;
	}

	public static void startService()
	{
		try
		{
			Properties props = System.getProperties();
			props.put("org.omg.CORBA.ORBInitialPort", Config.getInstance()
					.getAttr("namingServicePort"));
			props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			// Create and initialize the ORB
			ORB orb = ORB.init(new String[]
			{}, null);
			POA rootpoa = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			m_instance.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(m_instance);

			ExchangeServerIF srf = ExchangeServerIFHelper.narrow(ref);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			// bind references to names in naming service

			NameComponent path[] = ncRef.to_name(Config.getInstance().getAttr(
					"exchangeServiceName"));
			ncRef.rebind(path, srf);
			System.out.println("Server is ready and waiting ...");

			// wait for invocations from clients
			orb.run();
		} catch (Exception e)
		{
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
	}

	/**
	 * Delists a business from the exchange
	 * 
	 * @param symbol
	 *            to delist
	 * @throws Exception
	 *             when the symbol is not listed
	 */
	@Override
	public boolean unregisterBusiness(String symbol)
	{
		// try to remove the stock from the business and price registers. If the
		// symbol
		// is not found, throw an exception
		BusinessInterface bi = businessDirectory.remove(symbol);
		if ((bi == null) || (priceDirectory.remove(symbol) == null))
		{
			return false;
		}
		writeBusinessToFile();
		return true;
		// TODO: business must be unbound from the client?
		// TODO: since business will be running as a separate thread, so
		// stopping the thread is enough
	}

	/**
	 * Getter : Business Directory
	 * 
	 * @return Map of all business in exchange
	 * 
	 *         public Map<String, String> getBusinessDirectory() { return
	 *         businessDirectory; }
	 */

	/**
	 * NOT IMPLEMENTED Buy shares from a customer (ie Customer is SELLING
	 * shares)
	 * 
	 * @param shareItemList
	 *            ShareList of share to purchase from customer
	 * @param info
	 *            Customer selling shares
	 * @return
	 */
	public ShareSalesStatusList buyShares(ShareList shareItemList, Customer info)
	{
		// TODO implement, not yet required by the specifications
		return shareStatusSaleList;
	}

	/**
	 * Sell Shares to a Customer (ie Customer is BUYING shares)
	 * 
	 * @param shareItemList
	 *            ShareList of shares to transact
	 * @param info
	 *            Customer object making the transaction
	 * @return ShareSalesStatusList - Can access sold shares and available
	 *         shares lists
	 */
	public ShareSalesStatusList sellShares(ShareList shareItemList,
			Customer info)
	{
		ShareItem soldShare;

		for (ShareItem s : shareItemList.getLstShareItems())
		{
			soldShare = shareStatusSaleList.isShareAvailable(s);

			if (soldShare != null)
			{

				synchronized (soldShare)
				{

					shareStatusSaleList.addToSoldShares(s, info);

					if (payBusiness(soldShare))
					{
						System.out.println(" \n " + "Shares paid for "
								+ soldShare.getBusinessSymbol());
					} else
					{
						System.out.println(" \n " + "Shares not paid: "
								+ soldShare.printShareInfo());
					}
				}
			}
		}

		// Restock Share Lists
		this.restock();
		return shareStatusSaleList;
	}

	/**
	 * Given a customer, determine all of that customers' stocks
	 * 
	 * @param customer
	 *            wanting stock information
	 * @return list of customers' stocks
	 */
	public List<ShareItem> getShares(Customer customer)
	{
		return shareStatusSaleList.getShares(customer);
	}

	/**
	 * @return arrayList of all tickers listed on exchange
	 */
	public ArrayList<String> getListing()
	{
		ArrayList<String> tickerList = new ArrayList<String>();

		for (String ticker : businessDirectory.keySet())
		{
			tickerList.add(ticker);
		}

		return tickerList;
	}

	/**
	 * @param businessName
	 *            string of business TODO enum
	 * @return the ticker commonly used to identify a company | Null if not
	 *         found
	 * @deprecated Should refer to businesses exclusively by their ticker symbol
	 */
	public String getBusinessTicker(String businessName)
	{
		switch (businessName)
		{
		case "google":
			return "GOOG"; // google.getTicker();
		case "microsoft":
			return "MSFT"; // microsoft.getTicker();
		case "yahoo":
			return "YHOO"; // yahoo.getTicker();
		default:
			return null;
		}
	}

	/**
	 * Used to issue common.share on Ecxhange start up
	 */
	protected void initializeShares()
	{

		List<ShareItem> lstShares = new ArrayList<ShareItem>();

		// For Testing
		lstShares
				.add(new ShareItem("", "MSFT", ShareType.COMMON, 540.11f, 100));
		lstShares.add(new ShareItem("", "MSFT.B", ShareType.CONVERTIBLE,
				523.32f, 100));
		lstShares.add(new ShareItem("", "MSFT.C", ShareType.PREFERRED, 541.28f,
				100));
		lstShares
				.add(new ShareItem("", "GOOG", ShareType.COMMON, 540.11f, 100));
		lstShares.add(new ShareItem("", "GOOG.B", ShareType.CONVERTIBLE,
				532.23f, 100));
		lstShares.add(new ShareItem("", "GOOG.C", ShareType.PREFERRED, 541.28f,
				100));
		lstShares
				.add(new ShareItem("", "GOOG", ShareType.COMMON, 540.11f, 100));

		for (ShareItem shareItem : lstShares)
		{

			ShareItem addShareItem = this.issueSharesRequest(shareItem);

			if (addShareItem != null)
			{
				shareStatusSaleList.addToAvailableShares(addShareItem);
				shareStatusSaleList.addToNewAvShares(addShareItem);

			}
		}

	}

	/**
	 * Method to restock any available common.share that is below the threshold
	 */
	private void restock()
	{

		System.out.println(" \n " + "...... Restocking Shares .......");

		// Check Available stock amount
		for (ShareItem sItem : shareStatusSaleList.getAvailableShares())
		{

			synchronized (sItem)
			{

				if (sItem.getQuantity() < RESTOCK_THRESHOLD)
				{

					ShareItem newShares = this.issueSharesRequest(sItem);

					if (newShares != null)
					{
						sItem.setOrderNum(newShares.getOrderNum());
						sItem.setQuantity(newShares.getQuantity());

						shareStatusSaleList.addToNewAvShares(sItem);

					}
				}
			}
		}
	}

	/**
	 * Pays a business for shares that were previously issued but not paid for.
	 * 
	 * @param soldShare
	 *            ShareItem requiring business payment
	 * @return true if payment is processed
	 */
	private boolean payBusiness(ShareItem soldShare)
	{
		// if the business is not registered, there is no interface, and null is
		// returned
		BusinessInterface bi = businessDirectory.get(soldShare
				.getBusinessSymbol());
		if (bi == null)
			return false;

		try
		{
			return bi.recievePayment(soldShare.getOrderNum(),
					soldShare.getUnitPrice() * soldShare.getQuantity());
		} catch (Exception e)
		{
			System.out.println(" \n " + e.getMessage());
		}

		return false;
	}

	protected ShareType backConvertShareType(CORBAShareType sharetype)
	{
		switch (sharetype.toString())
		{
		case "COMMON":
			return ShareType.COMMON;
		case "CONVERTIBLE":
			return ShareType.CONVERTIBLE;
		case "PREFERRED":
			return ShareType.PREFERRED;
		default:
			return ShareType.PREFERRED;
		}
	}

	protected CORBAShareType convertShareType(ShareType sharetype)
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

	/**
	 * Request a business to issue shares
	 * 
	 * @param sItem
	 *            ShareItem to be issued
	 * @return ShareItem (null will be returned if the transaction fails)
	 */
	private ShareItem issueSharesRequest(ShareItem sItem)
	{
		Boolean sharesIssued = false;

		BusinessInterface bi = businessDirectory.get(sItem.getBusinessSymbol());
		if (bi == null)
			return null;

		String orderNum = generateOrderNumber();

		synchronized (orderNum)
		{
			try
			{
				sharesIssued = bi.issueShares(new CORBAShareOrder(orderNum,
						"not applicable", sItem.getQuantity(), sItem
								.getUnitPrice(), sItem.getUnitPrice(),
						convertShareType(sItem.getShareType()), sItem
								.getBusinessSymbol()));
				/*
				 * sharesIssued = bi.issueShares(new ShareOrder(orderNum,
				 * "not applicable", sItem.getBusinessSymbol(),
				 * sItem.getShareType(), sItem.getUnitPrice(),
				 * RESTOCK_THRESHOLD, sItem.getUnitPrice()));
				 */
			} catch (Exception e)
			{
				System.out.println(" \n " + e.getMessage());
			}
		}

		if (sharesIssued)
		{
			ShareItem newShareItem = new ShareItem(orderNum,
					sItem.getBusinessSymbol(), sItem.getShareType(),
					sItem.getUnitPrice(), RESTOCK_THRESHOLD);
			return newShareItem;
		}

		return null;
	}

	/**
	 * Method to generate unique sequential order number for issue common.share
	 */
	protected synchronized String generateOrderNumber()
	{
		orderInt = orderInt + 1;
		String orderNumber = Integer.toString(orderInt);
		return orderNumber;
	}

	public void setORB(ORB ORB)
	{
		m_orb = ORB;
	}

	@Override
	public void run()
	{
		startService();
		while (true)
		{
			try
			{
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}