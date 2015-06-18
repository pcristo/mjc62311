package client;

import java.util.Properties;

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
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import exchangeServer.ExchangeServerIF;
import exchangeServer.ExchangeServerIFHelper;

/**
 * Created by Sai on 2015/6/18.
 */
public class ExchangeServiceClient_CORBA
{

	private static ExchangeServerIF m_serverIF;
	private static NamingContextExt ncRef;
	private static ORB orb;
	private static ORB listenerOrb;
	private String m_server;
	private String m_company;

	public ExchangeServiceClient_CORBA(String hostname, String company)
			throws InvalidName, ServantAlreadyActive, WrongPolicy,
			ServantNotActive, NotFound, CannotProceed,
			org.omg.CosNaming.NamingContextPackage.InvalidName, AdapterInactive
	{
		Properties props = System.getProperties();
		props.put("org.omg.CORBA.ORBInitialPort", "9999");
		props.put("org.omg.CORBA.ORBInitialHost", "localhost");
		String[] args =
		{};
		orb = ORB.init(args, null);
		listenerOrb = ORB.init(args, null);
		// get the root naming context
		org.omg.CORBA.Object objRef = orb
				.resolve_initial_references("NameService");
		// Use NamingContextExt instead of NamingContext. This is
		// part of the Interoperable naming Service.
		ncRef = NamingContextExtHelper.narrow(objRef);
		m_company = company;
		m_server = hostname;
		try
		{
			if (m_serverIF != null)
				m_serverIF._release();
			m_serverIF = ExchangeServerIFHelper.narrow(ncRef
					.resolve_str(m_company));
		} catch (NotFound | CannotProceed
				| org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		POA rootPOA = POAHelper.narrow(orb
				.resolve_initial_references("RootPOA"));
		// Resolve MessageServer
		NameComponent[] nc =
		{ new NameComponent("MessageServer", "") };
		rootPOA.the_POAManager().activate();
		// orb.run();

	}

	public static void main(String[] args)
	{
        try {
            ExchangeServiceClient_CORBA server = new ExchangeServiceClient_CORBA("localhost","GOOGLE");
            System.out.println(m_serverIF.getBusiness("GOOGLE").businessSymbol);
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (ServantAlreadyActive servantAlreadyActive) {
            servantAlreadyActive.printStackTrace();
        } catch (WrongPolicy wrongPolicy) {
            wrongPolicy.printStackTrace();
        } catch (ServantNotActive servantNotActive) {
            servantNotActive.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (AdapterInactive adapterInactive) {
            adapterInactive.printStackTrace();
        }
    }
}
