package client;

import common.Customer;
import common.share.ShareType;
import common.util.Config;
import exchangeServer.*;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

/**
 * A simple client for stock broker client
 * Created by sai sun on 5/25/2015.
 */
public class BrokerServiceClient {

    private static ArrayList<String> tickers;


    /**
     * RMI Call to retreive broker interface object
     * @return BrokerInterface which references remote broker object
     * @throws RemoteException
     * @throws NotBoundException
     */
    public BrokerInterface getBroker(){


        Properties props = System.getProperties();
        props.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
        props.put("org.omg.CORBA.ORBInitialHost", "localhost");
        String[] args =
                {};
        ORB orb = ORB.init(args, null);

        // get the root naming context
        org.omg.CORBA.Object objRef = null;
        try {
            objRef = orb
                    .resolve_initial_references("NameService");
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        }
        // Use NamingContextExt instead of NamingContext. This is
        // part of the Interoperable naming Service.
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        BrokerInterface m_serverIF = null;
        try
        {

            if (m_serverIF != null)
                m_serverIF._release();
            m_serverIF = BrokerInterfaceHelper.narrow(ncRef
                    .resolve_str(Config.getInstance().getAttr("brokerServerName")));
        } catch (NotFound | CannotProceed
                | org.omg.CosNaming.NamingContextPackage.InvalidName e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        POA rootPOA = null;
        try {
            rootPOA = POAHelper.narrow(orb
                    .resolve_initial_references("RootPOA"));
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        }
        // Resolve MessageServer
        NameComponent[] nc =
                { new NameComponent("MessageServer", "") };
        try {
            rootPOA.the_POAManager().activate();
        } catch (AdapterInactive adapterInactive) {
            adapterInactive.printStackTrace();
        }

        return m_serverIF;
    }

    private static CORBACustomer convertCustomer(Customer c)
    {
        CORBACustomer customer = new CORBACustomer(c.getCustomerReferenceNumber(),c.getName(),c.getStreet1(),c.getStreet2(),c.getCity(),c.getProvince(),c.getPostalCode(),c.getCountry());
        return customer;
    }

    private static CORBAShareType convertShareType(ShareType sharetype)
    {
        switch (sharetype)
        {
            case COMMON:
                return CORBAShareType.COMMON;
            case CONVERTIBLE:
                return CORBAShareType.CONVERTIBLE;
            case PREFERRED:
                return CORBAShareType.PREFERRED;
            default:
                return CORBAShareType.PREFERRED;
        }
    }

    public static void main(String args[]) {
        try {

            BrokerInterface service = new BrokerServiceClient().getBroker();

            tickers = new ArrayList<String>();

            boolean terminate = false;
            while (!terminate) {
                ServerDisplayMsgs.printWelcome();
                //operation switch
                Customer customer;
                ShareType tickerType;
                int tickerQuantity;
                boolean ret = false;
                Scanner scan = new Scanner(System.in);
                switch (ServerDisplayMsgs.printOps()) {
                    case 1:
                        customer = getCustomer(ServerDisplayMsgs.getCustomerInfo());
                        tickerType = ServerDisplayMsgs.enterTickerType();
                        tickerQuantity = ServerDisplayMsgs.enterTickerQuantity();
                        ret = service.sellShares((String[])ServerDisplayMsgs.getTickerList().toArray(),convertShareType(tickerType),tickerQuantity,convertCustomer(customer));
                        ServerDisplayMsgs.printResult(ret);
                        System.out.println("Press enter key to continue...");
                        scan.nextLine();
                        break;
                    case 2:
                        customer = getCustomer(ServerDisplayMsgs.getCustomerInfo());
                        tickerType = ServerDisplayMsgs.enterTickerType();
                        tickerQuantity = ServerDisplayMsgs.enterTickerQuantity();
                        ret = service.buyShares((String[])ServerDisplayMsgs.getTickerList().toArray(),convertShareType(tickerType),tickerQuantity,convertCustomer(customer));
                        ServerDisplayMsgs.printResult(ret);
                        System.out.println("Press enter key to continue...");
                        scan.nextLine();
                        break;
                    case 3:
                        tickers = new ArrayList(Arrays.asList(service.getTickerListing()));
                        ServerDisplayMsgs.printList("Ticker Listing Info:", tickers);

                        System.out.println("Press enter key to continue...");
                        scan.nextLine();
                        break;
                    case 4:
                        terminate = true;
                        break;
                    default:
                        terminate = true;
                        break;
                }
                ServerDisplayMsgs.flushConsole();

            }
/*            ShareList ShareList = new ShareList(createListofShares());
            Customer newCust = new Customer(1,"Gay Hazan","123 Money Ave","","Montreal","Quebec","H4W 1N3", "Canada" );*/
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }


    private static Customer getCustomer(String info) {
        return new Customer(info.split(";;d")[1], info.split(";;d")[2], info.split(";;d")[3], info.split(";;d")[4], info.split(";;d")[5], info.split(";;d")[6], info.split(";;d")[7]);
    }


}
