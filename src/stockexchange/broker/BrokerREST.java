package stockexchange.broker;

// Import required java libraries

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// Extend HttpServlet class
public class BrokerREST extends HttpServlet {

    private void doSomething(String type, HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println(type + ": " + request.getParameter("test"));
    }

    @Override
    protected void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        // DO SOMETHING GETTY
        doSomething("GET", request, response);


    }



    // Method to handle POST method request.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // DO SOMETHING POSTY
        doSomething("POST", request, response);

    }


    // Method to handle POST method request.
    @Override
    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // DO SOMETHING PUTTY
        doSomething("PUT", request, response);
    }

    // Method to handle POST method request.
    @Override
    protected void doDelete(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // DO SOMETHING DELETE
        doSomething("DELETE", request, response);
    }

}