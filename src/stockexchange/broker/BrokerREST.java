
package stockexchange.broker;

// Import required java libraries

import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;
import WebServices.ExchangeClientServices.ShareItem;
import common.Customer;
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

        // Set header
        response.setContentType("text/html");
        // Start response build
        Boolean success = true;

        // Get parameters
        String ticker = request.getParameter("ticker");
        String type = request.getParameter("type");
        String qty = request.getParameter("qty");
        String custJson = URLDecoder.decode(request.getParameter("customer"), "UTF-8");

        PrintWriter out = response.getWriter();
        Customer customer = null;
        String data = null;
        // Try rebuild customer object
        try {
            ObjectMapper mapper = new ObjectMapper();
            customer = mapper.readValue(custJson, Customer.class);
            data = customer.getName();
        } catch(Exception e) {
            success = false;
        }

        if(ticker == null || type == null || qty == null) {
            success = false;
        }

        // Pass on data to exchange
        // TODO routing


        if(success) {
            ShareItem toBuy = new ShareItem();
            toBuy.setBusinessSymbol(ticker);
            toBuy.setQuantity(Integer.parseInt(qty));
            WebServices.ExchangeClientServices.ShareType shareType =
                    WebServices.ExchangeClientServices.ShareType.fromValue(type);

            String price = new GoogleFinance().getStock(new Company(ticker, new stockQuotes.Exchange("NASDAQ")));

            toBuy.setUnitPrice(Float.parseFloat(price));

            WebServices.ExchangeClientServices.Customer newCust =
                    new WebServices.ExchangeClientServices.Customer(customer);

            ExchangeWSImplService service = new ExchangeWSImplService();
            IExchange iExchange = service.getExchangeWSImplPort();
            success = iExchange.sellShareService(toBuy, newCust);
        }
        // Return response
        out.println("{'success':'"+success.toString() +"', 'data': '"+ data+"'}");

    }

}

