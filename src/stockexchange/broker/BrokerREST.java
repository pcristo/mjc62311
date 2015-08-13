
package stockexchange.broker;

// Import required java libraries

import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareOrder;
import common.share.ShareType;
import org.codehaus.jackson.map.ObjectMapper;
import replication.FrontEnd;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

// Servlet class for a restful broker
public class BrokerREST extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final FrontEnd frontEnd = new FrontEnd();
	Broker broker = new Broker();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        LoggerClient.log("doGet Called");

        // Set header
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Creating FrontEnd");
    }


    // Method to handle POST method request.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Set header
        response.setContentType("text/html");
        
        // Get parameters
        String ticker = request.getParameter("ticker");
        String type = request.getParameter("type");
        String qty = request.getParameter("qty");
        String price = request.getParameter("price");
        String custJson = URLDecoder.decode(request.getParameter("customer"), "UTF-8");

        Customer customer = null;
        String data = null;

        Boolean success = true;
        // Try rebuild customer object
        try {
            ObjectMapper mapper = new ObjectMapper();
            customer = mapper.readValue(custJson, Customer.class);
            data = customer.getName();
        } catch(Exception e) {
            success = false;
        }

        if(ticker == null || type == null || qty == null || price == null) {
            success = false;
        }



        ShareOrder toBuy = new ShareOrder("Use sequence number", "Undefined",
                ticker, ShareType.valueOf(type), -1, Integer.parseInt(qty), Float.parseFloat(price));

        PrintWriter out = response.getWriter();
        if(success) {
            if(sendOrderToFrontEnd(toBuy, customer)) {
                data = customer.getName();
                out.println("{'success':'" + success.toString() + "', 'data': '" + data + "'}");
            } else {
                out.println("{'success':'" + success.toString() + "'");
            }
        } else {
            out.println("{'success':'"+success.toString()+"'");
        }

    }

    /**
     * Passes the order to the Front End and waits for the system to reply. 
     * @param sO
     * @param cust
     * @return true if transaction succeeded, false otherwise
     */
    private boolean sendOrderToFrontEnd(ShareOrder so, Customer cust) {
    	return frontEnd.sendOrderAndWaitForReply(so, cust);
    }
    
    
}

