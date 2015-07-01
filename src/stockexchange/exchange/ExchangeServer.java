package stockexchange.exchange;

import common.util.Config;
import exchange_domain.iExchange;
import exchange_domain.iExchangeHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

/**
 * Created by Gay on 6/29/2015.
 */
public class ExchangeServer {

    static public Exchange servant;

    public static void main(String args[]) {
        try {
    		// Set up ORB properties
    		Properties p = new Properties();
            p.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
            p.put("org.omg.CORBA.ORBInitialHost", Config.getInstance().getAttr("namingServiceAddr"));
        	
        	// Create a new object request broker
            ORB orb = ORB.init(args, p);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Create a new address book ...
            servant = new Exchange();
            servant.setORB(orb);


            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(servant);
            iExchange href = iExchangeHelper.narrow(ref);

            org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent path[] = ncRef.to_name( "exchange" );
            ncRef.rebind(path, href);

            System.out.println("Exchange Server ready and waiting ...");

            // wait for invocations from clients
            for (;;){
                orb.run();
            }
        }

        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("Exchange server Exiting ...");
    }

}
