package business;

import common.logger.LoggerClient;
import common.util.Config;
import corba.business_domain.interface_business;
import corba.business_domain.interface_businessHelper;
import corba.exchange_domain.iExchange;
import corba.exchange_domain.iExchangeHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import java.util.Properties;

/**
 * This class creates a new instance of a business, creates the ORB, registers
 * with the CORBA Naming Service, and runs the server in a background thread
 * 
 * @see http://www.javacoffeebreak.com/articles/javaidl/javaidl.html
 * @author patrick
 */
public class BusinessServer implements Runnable {
	private BusinessServant business; 

	/**
	 * Constructor
	 * @param businessSymbol the symbol for the business to create
	 */
	public BusinessServer(String businessSymbol) {
		business = new BusinessServant(businessSymbol);
	}
	
	/**
	 * Launches a new business server within a new thread.
	 * 
	 * @param symbol
	 *            The symbol of the business to launch.
	 * @return The thread that has been started
	 */
	public static Thread launch(String symbol) {		
		BusinessServer business = new BusinessServer(symbol);

		Thread thread = new Thread(() -> business.run());
		thread.start();

		return thread;
	}

	/**
	 * Initializes the ORB, connects to naming service, keeps the server alive.
	 * Registers the business with the exchange. You should call the static
	 * method launch(String symbol) instead of calling run directly
	 */
	@Override
	public void run() {
		try {
			InitORB();
			if (!RegisterExchange())
				throw new Exception("Could not register with exchange");
		} catch (Exception e) {
			LoggerClient.log("Business Server Error - " + e.getMessage());
			System.exit(0);
		}

		// Keep server thread alive
		while (true) {}
	}

	/**
	 * Initializes ORB and registers to naming service
	 * 
	 * @throws InvalidName
	 * @throws NotFound
	 * @throws CannotProceed
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName
	 * @throws WrongPolicy
	 * @throws ServantNotActive
	 * @throws AdapterInactive
	 */
	private void InitORB() throws InvalidName, NotFound, CannotProceed,
			org.omg.CosNaming.NamingContextPackage.InvalidName,
			ServantNotActive, WrongPolicy, AdapterInactive {
		// Set up ORB properties
		Properties p = new Properties();
        p.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
        p.put("org.omg.CORBA.ORBInitialHost", Config.getInstance().getAttr("namingServiceAddr"));
        
		// Create a new object request broker
		ORB orb = ORB.init(new String[0], p);
		POA rootpoa = POAHelper.narrow(orb
				.resolve_initial_references("RootPOA"));
		rootpoa.the_POAManager().activate();
		
		// get object reference from the servant
		org.omg.CORBA.Object ref = rootpoa.servant_to_reference(business);
		interface_business href = interface_businessHelper.narrow(ref);

		org.omg.CORBA.Object objRef = orb
				.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

		NameComponent path[] = ncRef
				.to_name("business-" + business.getTicker());
		ncRef.rebind(path, href);

		LoggerClient.log("Business Server " + business.getTicker()
				+ " ready and waiting ...");

	}

	/**
	 * @return true if successful, false if not
	 * @throws NotFound
	 * @throws CannotProceed
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName
	 * @throws InvalidName
	 */
	private boolean RegisterExchange() throws NotFound, CannotProceed,
			org.omg.CosNaming.NamingContextPackage.InvalidName, InvalidName {
		// Set up ORB properties
		Properties p = new Properties();
        p.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
        p.put("org.omg.CORBA.ORBInitialHost", Config.getInstance().getAttr("namingServiceAddr"));
        
        // Get the exchange object
		ORB orb = ORB.init(new String[0], p);

		org.omg.CORBA.Object object = orb
				.resolve_initial_references("NameService");

		NamingContext namingContext = NamingContextHelper.narrow(object);

		NameComponent nc_array[] = { new NameComponent("exchange", "") };

		org.omg.CORBA.Object objectReference = namingContext.resolve(nc_array);

		iExchange exchange = iExchangeHelper.narrow(objectReference);

		// Register to it
		return exchange.registerBusiness(business.getTicker(),
				business.getUnitPrice());
	}

}