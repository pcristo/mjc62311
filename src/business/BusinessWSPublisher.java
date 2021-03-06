package business;

import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;
import common.logger.LoggerClient;
import common.share.ShareType;

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
	public static void StartAllWebservices(String port) throws Exception {
		//String endpointPrefix = Config.getInstance().getAttr("BusinessEndpointPrefix", true);
		String endpointPrefix = "http://localhost:"+port+"/WS/";
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
				throw ex;
			}
		}
	}
	
	/**
	 * Registers all businesses in the directory with an exchange
	 * @throws Exception 
	 */
	public static void RegisterAllWithExchange(String businessPort, String exchangePort) throws Exception {
		if (businessDirectory.size() == 0)
			throw new Exception("No businesses in the directory. Did you call createBuisness()?");


		//Register to all three Exchanges
		String[] lstExchange = {"TSX", "TSXCOPY1","TSXCOPY2"};
		for (String exName : lstExchange) {
			for (String stock : businessDirectory.keySet()) {
				//String exchangeName = Config.getInstance().getAttr(stock + "sx");
				ExchangeWSImplService exchangews = new ExchangeWSImplService(exName, exchangePort);
				IExchange exchange = exchangews.getExchangeWSImplPort();

				LoggerClient.log("Registering " + stock + " with exchange " + exName + "...");

				float price = businessDirectory.get(stock).getShareInfo(ShareType.COMMON).getUnitPrice();

				try {
					exchange.registerBusiness(stock, price, businessPort);
				} catch (Exception ex) {
					System.out.println("\tFailed to register " + stock + " with exchange.\n\t" +
							ex.getMessage());
					LoggerClient.log("\tFailed to register " + stock + " with exchange.\n\t" +
							ex.getMessage());
				}
				LoggerClient.log("Registered " + stock + " with exchange at " + price);
			}
		}
	}
	
	/**
	 * Closes the connections for all business web services	
	 */
	public synchronized static void unload() throws Exception{
		// TODO need port passed in here
		ExchangeWSImplService exchangews = new ExchangeWSImplService("TSX", "fake");
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
		
		businessDirectory.clear();
		endpoints.clear();
	}

	public static void main(String[] args) throws Exception {
		try {
			BusinessWSPublisher.createBusiness("GOOG");
			BusinessWSPublisher.StartAllWebservices(args[0]);
			BusinessWSPublisher.RegisterAllWithExchange(args[0], args[1]);
		} catch (Exception e) {
			LoggerClient.log("Business Service Error " + e.getMessage());
			throw e;
		}
	}
}
