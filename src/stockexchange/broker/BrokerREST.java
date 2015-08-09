
package stockexchange.broker;

// Import required java libraries

import WebServices.ExchangeClientServices.ExchangeWSImplService;
import WebServices.ExchangeClientServices.IExchange;
import WebServices.ExchangeClientServices.ShareItem;
import common.Customer;
import common.UdpServer;
import common.share.ShareOrder;
import common.share.ShareType;
import common.util.Config;

import org.codehaus.jackson.map.ObjectMapper;

import replication.FrontEnd;
import replication.messageObjects.MessageEnvelope;
import replication.messageObjects.OrderMessage;
import stockQuotes.Company;
import stockQuotes.GoogleFinance;

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
        PrintWriter replyStream = response.getWriter();   
        
        // Get parameters
        String ticker = request.getParameter("ticker");
        String type = request.getParameter("type");
        String qty = request.getParameter("qty");
        String price = request.getParameter("price");
        String custJson = URLDecoder.decode(request.getParameter("customer"), "UTF-8");

        Customer customer = null;
        String data = null;
        // Try rebuild customer object
        try {
            ObjectMapper mapper = new ObjectMapper();
            customer = mapper.readValue(custJson, Customer.class);
            data = customer.getName();
            replyStream.println("  Customer name: " + customer.getName());
        } catch(Exception e) {
        	replyStream.println("ERROR: Couldn't understand customer information");
        	replyStream.close();
            return;
        }

        if(ticker == null || type == null || qty == null || price == null) {
        	replyStream.println("ERROR: Missing a parameter for the order");
        	replyStream.close();
            return;
        }

        ShareOrder toBuy = new ShareOrder("Use sequence number", "Undefined", 
        		ticker, ShareType.valueOf(type), -1, Integer.parseInt(qty), Float.parseFloat(price));
        
        if (sendOrderToFrontEnd(toBuy, customer))
        	replyStream.println("\n ORDER SUCCESS");
        else
        	replyStream.println("\n ORDER FAILED.");
        
        replyStream.close();
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

