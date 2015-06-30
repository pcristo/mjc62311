package stockexchange.exchange;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import exchange_domain.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

/**
 * Created by Gay on 6/29/2015.
 */
public class ExchangeServer {

    public static void main(String args[]) {
        try {
            // Create a new object request broker
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Create a new address book ...
            Exchange servant = new Exchange();
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
