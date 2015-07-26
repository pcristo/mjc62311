package business;

import common.logger.LoggerClient;
import common.share.ShareType;
import common.util.Config;
import stockexchange.exchange.ExchangeWSImpl;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/*
 * Launch the main() of this class to publish four businesses to the web at 
 * address specified by BusinessEndpointPrefix in the config.json file, followed 
 * by the stock symbol. For example, http://mywebsite.net/WS/YHOO
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
	 */
	public static void StartAllWebservices() {
		String endpointPrefix = Config.getInstance().getAttr("BusinessEndpointPrefix");
		LoggerClient.log("Starting Business webservices...");	
		
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
	 */
	public static void RegisterAllWithExchange() {
		ExchangeWSImpl exchange = new ExchangeWSImpl();
		
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
		ExchangeWSImpl exchange = new ExchangeWSImpl();
				
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
