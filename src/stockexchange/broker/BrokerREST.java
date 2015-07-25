
package stockexchange.broker;

// Import required java libraries

import common.Customer;
import org.codehaus.jackson.map.ObjectMapper;

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
        String success = "true";

        // Get parameters
        String ticker = request.getParameter("ticker");
        String type = request.getParameter("type");
        String qty = request.getParameter("qty");
        String custJson = URLDecoder.decode(request.getParameter("customer"), "UTF-8");

        PrintWriter out = response.getWriter();
        Customer customer;
        String data = null;
        // Try rebuild customer object
        try {
            ObjectMapper mapper = new ObjectMapper();
            customer = mapper.readValue(custJson, Customer.class);
            data = customer.getName();
        } catch(Exception e) {
            success = "false";
        }

        if(ticker == null || type == null || qty == null) {
            success = "false";
        }

        // Return response
        out.println("{'success':'"+success+"', 'data': '"+ data+"'}");

    }

}

