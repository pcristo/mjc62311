package stockexchange.exchange;

import common.logger.LoggerClient;

import javax.xml.ws.Endpoint;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

/*
 * Launch the main() of this class to publish exchange to the web at
 * address specified by ExangeEndpoint in the config.json file.
  * For example, http://mywebsite.net/WS/Exchange
 */
public class ExchangeWSPublisher {

    private static List<String> endpoints;
    private static List<Endpoint> publishedEndpoints;

    public static void main(String[] args) throws BindException {

        endpoints = new ArrayList<>();
        publishedEndpoints = new ArrayList<>();
        // Should add all sorts of checks for this
        String port = args[0];

        endpoints.add("http://localhost:"+port+"/WS/TSX");
        LoggerClient.log("Starting Exchange webservices...");

        for(String endpoint : endpoints) {
            try {
                publishedEndpoints.add(Endpoint.publish(endpoint, new ExchangeWSImpl()));

                LoggerClient.log("\tWebservice started at: " + endpoint);
            } catch (Exception ex) {
                LoggerClient.log("\tFailed to start webservice at: " + endpoint + ex);
                throw new BindException();
            }
        }
    }

    /*
     * Closes the connections for all exchange webservices
     */
    public synchronized static void unload() {
        for(Endpoint e : publishedEndpoints) {
            try {
                System.out.println("Stopping " + e.toString());
                endpoints.remove(e);
                e.stop();
            }
            catch (Exception ex) {
                LoggerClient.log("Failed to stop an endpoint.");
            }
        }
        
        endpoints.clear();
        publishedEndpoints.clear();
    }
}