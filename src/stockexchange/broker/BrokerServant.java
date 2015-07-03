package stockexchange.broker;

import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareType;
import corba.broker_domain.iBrokerPOA;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides CORBA servant implementation for the broker class.
 */
public class BrokerServant extends iBrokerPOA {
	private Broker broker;
	private List<Customer> customerList = new ArrayList<Customer>();

	/**
	 * Constructor
	 */
	public BrokerServant() {
		broker = new Broker();
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
		
		return broker.sellShares(tickerList, shareTypeEnum, quantity, customerList.get(custID));
	}

	@Override
	public int registerCustomer(String name, String street, String city,
			String province, String postalCode, String country) {
		Customer customer = new Customer(name, street, "", city, province, postalCode, country);
		customerList.add(customer);
		return customerList.indexOf(customer);
	}
}
