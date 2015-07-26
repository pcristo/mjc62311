package business;

import common.logger.LoggerClient;
import common.share.ShareType;
import common.util.Config;
import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;

import javax.xml.ws.Endpoint;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 * Convenience class that allows businesses to be launched easily and without 
 * replicating code. 
 */
public class BusinessWSPublisher {
	private static Hashtable<String, BusinessWSImpl> businessDirectory = new Hashtable<String, BusinessWSImpl>();
	private static List<Endpoint> endpoints = new ArrayList<Endpoint>();
	
	/**
	 * Adds a business to the server's directory of businesses. Once the list 
	 * is fully populated, call StartAllWebservices() followed by RegisterAllWithExchange() 
	 * to establish the business and accept orders.
	 * @param stockSymbol
	 */
	public static void createBusiness(String stockSymbol) {
		businessDirectory.put(stockSymbol, new BusinessWSImpl(stockSymbol));
	}
	
	/**
	 * Starts all web services listed in the business directory
	 * @throws Exception 
	 */
	public static void StartAllWebservices() throws Exception {
		String endpointPrefix = Config.getInstance().getAttr("BusinessEndpointPrefix");
		LoggerClient.log("Starting Business webservices...");	
		
		if (businessDirectory.size() == 0)
			throw new Exception("No businesses in the directory. Did you call createBuisness()?");
		
		for(String k : businessDirectory.keySet()) {
			String address = endpointPrefix + k;
			try {				
				endpoints.add(
					Endpoint.publish(endpointPrefix + k, businessDirectory.get(k))
				);
				LoggerClient.log("\tWebservice started at: " + endpointPrefix + k);
			}
			catch (Exception ex) {
				LoggerClient.log("\tFailed to start webservice at: " + address + "\n\t" + 
						ex.getMessage());
			}
		}
	}
	
	/**
	 * Registers all businesses in the directory with an exchange
	 * @throws Exception 
	 */
	public static void RegisterAllWithExchange() throws Exception {
		ExchangeWSImplService exchangews = new ExchangeWSImplService();
		IExchange exchange = exchangews.getExchangeWSImplPort();		
		
		if (businessDirectory.size() == 0)
			throw new Exception("No businesses in the directory. Did you call createBuisness()?");
		
		for(String stock : businessDirectory.keySet()) {
			LoggerClient.log("Registering with exchange...");	
			
			float price = businessDirectory.get(stock).getShareInfo(ShareType.COMMON).getUnitPrice();
					
			try {
				exchange.registerBusiness(stock, price);
			}
			catch (Exception ex) {
				LoggerClient.log("\tFailed to register " + stock + " with exchange.\n\t" + 
						ex.getMessage());
			}
			LoggerClient.log("Registered " + stock + " with exchange at " + price);
		}		
	}
	
	/**
	 * Closes the connections for all business web services	
	 */
	public synchronized static void unload() {
		ExchangeWSImplService exchangews = new ExchangeWSImplService();
		IExchange exchange = exchangews.getExchangeWSImplPort();	
				
		for(String stock : businessDirectory.keySet()) {
			try {
				exchange.unregister(stock);
				LoggerClient.log("Unregistered a stock.");
			}
			catch (Exception ex) {
				LoggerClient.log("Failed to unregister a stock: " + ex.getMessage());
			}
			
		}
		
		for(Endpoint e : endpoints) {
			try {
				e.stop();
				LoggerClient.log("Stopped a webservice.");
			}
			catch (Exception ex) {
				LoggerClient.log("Failed to stop a webservice.");
			}
		}
	}
}
