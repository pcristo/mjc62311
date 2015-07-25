package business;

import common.logger.LoggerClient;
import common.util.Config;

import javax.xml.ws.Endpoint;
import java.util.Hashtable;
import java.util.List;


/*
 * Launch the main() of this class to publish four businesses to the web at 
 * address specified by BusinessEndpointPrefix in the config.json file, followed 
 * by the stock symbol. For example, http://mywebsite.net/WS/YHOO
 */
public class BusinessWSPublisher {
	private static Hashtable<String, BusinessWSImpl> businessDirectory;
	private static List<Endpoint> endpoints;
	
	
	/*
	 * Creates webservice connection points for four businesses
	 */
	public static void main(String[] args) {		
		businessDirectory = new Hashtable<String, BusinessWSImpl>();
		
		businessDirectory.put("YHOO", new BusinessWSImpl("YHOO"));
		businessDirectory.put("MSFT", new BusinessWSImpl("MSFT"));
		businessDirectory.put("AAPL", new BusinessWSImpl("AAPL"));
		businessDirectory.put("GOOG", new BusinessWSImpl("GOOG"));
		
		String endpointPrefix = Config.getInstance().getAttr("BusinessEndpointPrefix");
		
		LoggerClient.log("Starting Business webservices...");	
		
		for(String k : businessDirectory.keySet()) {
			String address = endpointPrefix + k;
			try {
				Endpoint.publish(endpointPrefix + k, businessDirectory.get(k));
				LoggerClient.log("\tWebservice started at: " + endpointPrefix + k);	
			}
			catch (Exception ex) {
				LoggerClient.log("\tFailed to start webservice at: " + address);
			}			
		}
	}
	
	/*
	 * Closes the connections for all business webservices	
	 */
	public synchronized static void unload() {
		for(Endpoint e : endpoints) {
			try {
				endpoints.remove(e);
				e.stop();
			}
			catch (Exception ex) {
				LoggerClient.log("Failed to stop an endpoint.");
			}
		}
	}
}
