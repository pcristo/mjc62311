package business;

import common.logger.LoggerClient;
import common.share.Share;
import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;

import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import exchangeServer.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import stockQuotes.Company;
import stockQuotes.GoogleFinance;
import stockexchange.exchange.Exchange;

/**
 * A class for businesses to process transactions/manage shares
 *
 * @author patrick
 */
public class Business extends BusinessInterfacePOA implements Serializable,
		Runnable
{
	private static final long serialVersionUID = 1L;
	private static final String ORDER_RECORD_FILENAME = Config.getInstance()
			.getAttr("businessXmlLog");
	private List<Share> sharesList = new ArrayList<Share>();
	private List<OrderRecord> orderRecords = new ArrayList<OrderRecord>();
	private Object recordLock = new Object();
	private String m_identifier = "";

	private ORB m_orb;

	/**
	 * Constructor to create a business
	 *
	 * @param identifier
	 *            The name of the company to create a business object for
	 */
	public Business(String identifier)
	{
		try
		{
			// Dynamically load the file
			String filePath = Config.getInstance().getAttr("files") + "\\";

			InputStream inputStream = new FileInputStream(filePath + identifier);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));

			// reset the shares list
			this.sharesList = new ArrayList<Share>();

			// Process each line of the csv file
			String row;
			String[] column;
			while ((row = bufferedReader.readLine()) != null)
			{
				// split the line
				column = row.split(",");

				// confirm there are at least 3 values in the array
				if (column.length < 3)
				{
					bufferedReader.close();
					throw new IOException(
							"The CSV file is not correctly formatted.");
				}

				// capture the company information, get price from Google
				// webservice
				GoogleFinance tickers = new GoogleFinance();
				Company company = new Company(column[0],
						new stockQuotes.Exchange(column[2]));
				String price = tickers.getStock(company);

				if (price.equals(""))
				{
					price = "0";
				}
				// extract the common.share information and create a
				// common.share object
				Share s = new Share(column[0], ShareType.valueOf(column[1]),
						Float.parseFloat(price));

				// add the common.share to the list
				this.sharesList.add(s);
			}

			// Close the file
			bufferedReader.close();
			m_identifier = identifier;

			// log the activity
			LoggerClient.log("Business " + identifier + " object created.");

		} catch (IOException ioe)
		{
			LoggerClient.log("Failed to create business object " + identifier
					+ ": " + ioe.getMessage());
		}
	}

	/**
	 * @return the ticker commonly used to identify a company
	 */
	public String getTicker()
	{
		// all shares must have the same symbol, but may have different
		// 'extensions'
		// return the common part of the symbol here:

		// Some common.share types don't have extension in that case return
		// whole symbol
		String shareSymbol = sharesList.get(0).getBusinessSymbol();
		if (shareSymbol.contains("."))
		{
			return sharesList.get(0).getBusinessSymbol().split(".")[0];
		} else
		{
			return shareSymbol;
		}
	}

	/**
	 * Checks if an order is valid, and if so, issues the requested number of
	 * shares
	 *
	 * @param aSO
	 *            A ShareOrder to process
	 * @return true if successful, false if failed
	 * @throws RemoteException
	 */
	public boolean issueShares(ShareOrder aSO) throws RemoteException
	{
		// fetch the common.share that is relevant to this order
		Share listedShare = getShareInfo(aSO.getShareType());

		// if no valid listed common.share was found, return false
		if (listedShare == null)
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.getBusinessSymbol() + " " + aSO.getShareType()
					+ ": No valid share found for " + aSO.getShareType()
					+ " (order #" + aSO.getOrderNum() + ")");
			return false;
		}

		// if the order price lower than the current value, return false
		if (aSO.getUnitPriceOrder() < listedShare.getUnitPrice())
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.getBusinessSymbol() + " " + aSO.getShareType()
					+ ": Order price " + aSO.getUnitPriceOrder()
					+ " is less than minimum issue price " + aSO.getUnitPrice()
					+ " (order #" + aSO.getOrderNum() + ")");
			return false;
		}

		// validate the order is for at least 1 common.share, otherwise return
		// false
		if (aSO.getQuantity() <= 0)
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.getBusinessSymbol() + " " + aSO.getShareType()
					+ ": Invalid number of shares requested (order #"
					+ aSO.getOrderNum() + ")");
			return false;
		}

		// validate the order number is unique
		if (!validateOrderNumber(aSO.getOrderNum()))
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.getBusinessSymbol() + " " + aSO.getShareType()
					+ ": The order number " + aSO.getOrderNum()
					+ " already exists");
			return false;
		}

		// call authorizeShare as required
		int authorizations = (int) Math.floor(aSO.getQuantity() / 100);
		int remainder = aSO.getQuantity() % 100;
		for (int i = 0; i <= authorizations; i++)
			authorizeShare(aSO.getShareType(), 100);
		authorizeShare(aSO.getShareType(), remainder);

		// record to local memory file
		saveRecordToList(aSO);

		// return true
		LoggerClient.log(getTicker() + " successfully issued "
				+ aSO.getQuantity() + " shares of " + aSO.getBusinessSymbol()
				+ " " + aSO.getShareType() + " (order #" + aSO.getOrderNum()
				+ ")");
		return true;
	}

	/**
	 * Checks if an order number is unique, returns true if so
	 * 
	 * @param orderNum
	 *            The order number to check
	 * @return true if unique, false if not
	 */
	private boolean validateOrderNumber(String orderNum)
	{
		for (OrderRecord o : orderRecords)
			if (o.getOrderNum().equals(orderNum))
				return false;

		return true;
	}

	/**
	 * Checks if a common.share type exists for this business, and returns the
	 * common.share info
	 *
	 * @return a common.share corresponding to the type requested, or null if
	 *         not available
	 */
	public Share getShareInfo(ShareType shareType)
	{
		// flip through the registry searching for a common.share type that
		// matches the
		// request

		for (Share s : sharesList)
			if (s.getShareType().equals(shareType))
				return s;

		// nothing found... return null
		return null;
	}

	/**
	 * Authorizes shares if they are the an acceptable type and valid quantity
	 *
	 * @param shareType
	 *            The type of common.share
	 * @param quantity
	 *            The number of shares
	 * @return true if authorized
	 */
	private boolean authorizeShare(ShareType shareType, int quantity)
	{
		if (quantity > 100 || quantity <= 0)
			return false;

		boolean typeOK = false;
		for (ShareType s : ShareType.values())
		{
			if (s.equals(shareType))
				typeOK = true;
		}

		return typeOK;
	}

	@Override
	public boolean issueShares(CORBAShareOrder aSO)
	{
		Share listedShare = getShareInfo(backConvertShareType(aSO.shareType));

		// if no valid listed common.share was found, return false
		if (listedShare == null)
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.businessSymbol + " "
					+ backConvertShareType(aSO.shareType)
					+ ": No valid share found for "
					+ backConvertShareType(aSO.shareType) + " (order #"
					+ aSO.orderNum + ")");
			return false;
		}

		// if the order price lower than the current value, return false
		if (aSO.unitPriceOrder < listedShare.getUnitPrice())
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.businessSymbol + " "
					+ backConvertShareType(aSO.shareType) + ": Order price "
					+ aSO.unitPriceOrder + " is less than minimum issue price "
					+ aSO.unitPriceOrder + " (order #" + aSO.orderNum + ")");
			return false;
		}

		// validate the order is for at least 1 common.share, otherwise return
		// false
		if (aSO.quantity <= 0)
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.businessSymbol + " "
					+ backConvertShareType(aSO.shareType)
					+ ": Invalid number of shares requested (order #"
					+ aSO.orderNum + ")");
			return false;
		}

		// validate the order number is unique
		if (!validateOrderNumber(aSO.orderNum))
		{
			LoggerClient.log(getTicker() + " failed to issue shares of "
					+ aSO.businessSymbol + " "
					+ backConvertShareType(aSO.shareType)
					+ ": The order number " + aSO.orderNum + " already exists");
			return false;
		}

		// call authorizeShare as required
		int authorizations = (int) Math.floor(aSO.quantity / 100);
		int remainder = (int) aSO.quantity % 100;
		for (int i = 0; i <= authorizations; i++)
			authorizeShare(backConvertShareType(aSO.shareType), 100);
		authorizeShare(backConvertShareType(aSO.shareType), remainder);

		// record to local memory file
		saveRecordToList(new ShareOrder(aSO.orderNum, aSO.brokerRef,
				aSO.businessSymbol, backConvertShareType(aSO.shareType),
				aSO.unitPrice, (int) aSO.quantity, aSO.unitPriceOrder));

		// return true
		LoggerClient.log(getTicker() + " successfully issued " + aSO.quantity
				+ " shares of " + aSO.businessSymbol + " " + aSO.shareType
				+ " (order #" + aSO.orderNum + ")");
		return true;
	}

	@Override
	public BusinessInfo getShareInfo(CORBAShareType aShareType)
	{
		// flip through the registry searching for a common.share type that
		// matches the
		// request

		for (Share s : sharesList)
			if (s.getShareType().equals(backConvertShareType(aShareType)))
				return new BusinessInfo(s.getBusinessSymbol(),
						convertShareType(s.getShareType()), s.getUnitPrice());

		// nothing found... return null
		return null;
	}

	private ShareType backConvertShareType(CORBAShareType sharetype)
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

	/**
	 * Getter to return an array of all the common.share types available for
	 * this business
	 *
	 * @return An array of shares
	 */
	public BusinessInfo[] getSharesList()
	{
		return convertShareToBusinessInfo();
	}

	private BusinessInfo[] convertShareToBusinessInfo()
	{
		ArrayList<BusinessInfo> list = new ArrayList<>();
		for (Share share : sharesList)
		{
			BusinessInfo info = new BusinessInfo(share.getBusinessSymbol(),
					convertShareType(share.getShareType()),
					share.getUnitPrice());
			list.add(info);
		}

		return (BusinessInfo[]) list.toArray();
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

	/**
	 * Saves an order of issued shares to an XML record
	 *
	 * @param order
	 *            The order to write to file
	 * @throws FileNotFoundException
	 *             If the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason then a FileNotFoundException is
	 *             thrown.
	 * @deprecated Replaced by saveRecordToList()
	 */
	private void saveRecord(ShareOrder order) throws FileNotFoundException
	{
		// create the order record
		OrderRecord orderRecord = new OrderRecord(order, false);

		synchronized (recordLock)
		{
			// write to file
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(ORDER_RECORD_FILENAME, true)));
			e.writeObject(orderRecord); // append the new order to the record
			e.close();
		}
	}

	/**
	 * Saves an order of issued shares to local memory
	 *
	 * @param order
	 *            The order to save
	 */
	private void saveRecordToList(ShareOrder order)
	{
		synchronized (orderRecords)
		{
			orderRecords.add(new OrderRecord(order, false));
		}
	}

	/**
	 * Checks the order record for an exact match, and if found, updates the
	 * record to indicate the order is paid and returns true. Otherwise returns
	 * false.
	 *
	 * @param orderNum
	 *            The order number to look for
	 * @param totalPrice
	 *            The total price of the transaction (quantity * order unit
	 *            price)
	 * @return true if a match found, false if not or if the match has already
	 *         been paid
	 */
	public boolean recievePayment(String orderNum, float totalPrice)
	{
		boolean orderExists = false;
		boolean totalPriceOK = false;
		boolean isNotPaid = false;
		OrderRecord orderRecord = null;

		// check to see if there is a match that is not already paid
		synchronized (orderRecords)
		{
			for (OrderRecord o : orderRecords)
			{
				if (o.getOrderNum().equals(orderNum))
				{
					orderExists = true;

					if ((o.getQuantity() * o.getUnitPriceOrder()) == totalPrice)
						totalPriceOK = true;

					if (!o.isPaid())
						isNotPaid = true;

					orderRecord = o;

					break; // order was found, stop searching
				}
			}

			// handle success case
			if (isNotPaid && totalPriceOK && orderExists)
			{
				orderRecord.setPaid(true);
				LoggerClient.log("Payment for order " + orderNum
						+ " successful.");
				return true; // return
			}

		}

		// handle error cases
		if (!orderExists)
		{
			LoggerClient.log("Payment for order " + orderNum
					+ " failed. The order does not exist.");
			return false;
		}

		if (!totalPriceOK)
		{
			LoggerClient.log("Payment " + totalPrice + " for order " + orderNum
					+ " failed. The recorded "
					+ "total order price does not match: "
					+ orderRecord.getQuantity() + " units sold at "
					+ orderRecord.getUnitPriceOrder());
			return false;
		}

		if (!isNotPaid)
		{
			LoggerClient.log("Payment for order " + orderNum
					+ " failed. The order has already been paid.");
			return false;
		}

		LoggerClient.log("Payment for order " + orderNum
				+ " failed and no reason is known.");
		return false;

	}

	public void setORB(ORB orb_val)
	{
		m_orb = orb_val;
	}

	public static void startService(String serviceName)
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
			String csv = Config.getInstance().getAttr(serviceName);
			Business m_server = new Business(csv);
			m_server.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(m_server);

			BusinessInterface srf = BusinessInterfaceHelper.narrow(ref);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			// bind references to names in naming service

			NameComponent path[] = ncRef.to_name(serviceName);
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
	 * Create business interfaces and bind them to ports 9095 - 9099
	 * 
	 * @param args
	 */
	public static void main(String args[])
	{
		startService("google");
		startService("microsoft");
		startService("yahoo");
	}

	@Override
	public void run()
	{
		if (m_identifier.contains(".csv"))
		{
			m_identifier = Config.getInstance().getAttr(m_identifier);
		}
		startService(m_identifier);
	}

}
