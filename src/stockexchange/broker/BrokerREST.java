
package stockexchange.broker;

// Import required java libraries

import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;
import WebServices.ExchangeClientServices.ShareItem;
import common.Customer;
import common.util.Config;
import org.codehaus.jackson.map.ObjectMapper;
import stockQuotes.Company;
import stockQuotes.GoogleFinance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

// Servlet class for a restfull broker
public class BrokerREST extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Broker broker = new Broker();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException
    {
        // DO SOMETHING GETTY
        // TODO implement
    }


    // Method to handle POST method request.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter writer = new PrintWriter(Config.getInstance().getAttr("restDebugFile"), "UTF-8");


        // Set header
        response.setContentType("text/html");
        // Start response build
        Boolean success = true;

        writer.println("~~~~ DEBUG OF BROKER REST SERVLET ~~~~");
        writer.println("URL: " + request.getRequestURL());

        // Get parameters
        String ticker = request.getParameter("ticker");
        String type = request.getParameter("type");
        String qty = request.getParameter("qty");
        String custJson = URLDecoder.decode(request.getParameter("customer"), "UTF-8");

        writer.println("Ticker param: " + ticker);
        writer.println("Type param: " + type);
        writer.println("Qty param: " + qty);
        writer.println("Customer json object: " + custJson);



        PrintWriter out = response.getWriter();
        Customer customer = null;
        String data = null;
        // Try rebuild customer object
        try {
            ObjectMapper mapper = new ObjectMapper();
            customer = mapper.readValue(custJson, Customer.class);
            data = customer.getName();
            writer.println("Built Customer Object from json.  Customer name: " + customer.getName());
        } catch(Exception e) {
            writer.println("ERROR");
            writer.close();
            success = false;
        }

        if(ticker == null || type == null || qty == null) {
            writer.println("ERROR");
            writer.close();
            success = false;
        }



        if(success) {
            writer.println("Building SOAP params for Exchange service...");
            ShareItem toBuy = new ShareItem();
            toBuy.setBusinessSymbol(ticker);
            toBuy.setQuantity(Integer.parseInt(qty));
            writer.println("Attempting to get stock price from google...");
            String price = new GoogleFinance().getStock(new Company(ticker, new stockQuotes.Exchange("NASDAQ")));
            if(price == null || price.isEmpty()) {
                price = "500f";
                writer.println("Unable to reach google.  Price defaulted to 500");
            } else {
                writer.println("Success! Google price set at: " + price);
            }
            toBuy.setUnitPrice(Float.parseFloat(price));


            writer.println("Converting Customer to SoapCustomer");
            WebServices.ExchangeClientServices.Customer newCust =
                    new WebServices.ExchangeClientServices.Customer(customer);

            // Routing
            writer.println("Determing which exchange to connect to...");
            ExchangeWSImplService service = null;
            if(ticker.equals("AAPL") || ticker.equals("GOOG")) {
                service = new ExchangeWSImplService("TSX");
                writer.println("Connected to TSX");
            } else if(ticker.equals("MSFT") || ticker.equals("YHOO")) {
                service = new ExchangeWSImplService("NASDAQ");
                writer.println("Connected to NASDAQ");
            } else {
                writer.println("ERROR");
                writer.close();
                success = false;
            }

            if(success) {
                writer.println("Making soap exchange call.");
                IExchange iExchange = service.getExchangeWSImplPort();
                success = iExchange.sellShareService(toBuy, newCust);
                writer.println("Exchange call was success? " + success);
            }
        }
        // Return response
        writer.println("Outputing JSON response.  Success: " + success + " data: " + data);
        writer.println(" ~~~~ END OF BROKER SERVELT ~~~~ ");
        writer.close();
        out.println("{'success':'"+success.toString() +"', 'data': '"+ data+"'}");

    }

}

