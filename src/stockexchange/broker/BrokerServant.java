package stockexchange.broker;

import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareType;
import corba.broker_domain.iBrokerPOA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides CORBA servant implementation for the broker class.
 */
public class BrokerServant extends iBrokerPOA {
	private Broker broker;
	private Map<Integer, Customer> customerMap;

	/**
	 * Constructor
	 */
	public BrokerServant() {
		broker = new Broker();
		customerMap = new HashMap<Integer, Customer>();
	}

	/**
	 * IMPLEMENTATION OF THE BROKER STUB
	 */
	@Override
	public boolean sellShares(String ticker, String shareType, int quantity, int custID) {
		ArrayList<String> tickerList = new ArrayList<String>();
		tickerList.add(ticker);

		ShareType shareTypeEnum;
		try {
			shareTypeEnum = ShareType.valueOf(shareType);
		}
		catch (Exception e) {
			LoggerClient.log("Error in broker servant: " + e.getMessage());
			return false;
		}


		// We need to test for customer id's existent first
		// Without this check...the server and corba will complain
		if(customerMap.get(custID) == null) {
			LoggerClient.log("Error in broker servant: Invalid customer id");
			return false;
	}

		return broker.sellShares(tickerList, shareTypeEnum, quantity, customerMap.get(custID));
	}

	@Override
	public int registerCustomer(String name, String street, String city,
			String province, String postalCode, String country) {
		Customer customer = new Customer(name, street, "", city, province, postalCode, country);
		customerMap.put(customer.getCustomerReferenceNumber(), customer);
		return customer.getCustomerReferenceNumber();
	}
}
