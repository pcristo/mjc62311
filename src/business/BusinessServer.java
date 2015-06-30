package business;

import org.omg.CORBA.*;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import exchange_domain.iExchange;
import exchange_domain.iExchangeHelper;

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
	 * Launches a new business server within a new thread.
	 * 
	 * @param symbol
	 *            The symbol of the business to launch.
	 * @return The thread that has been started
	 */
	public static Thread launch(String symbol) {
		BusinessServer business = new BusinessServer();
		business.setBusinessSymbol(symbol);

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
			if (!RegisterExchange()) throw new Exception("Could not register with exchange");
		} catch (Exception e) {
			// TODO patrickc log this in the logger
			System.err.println("Business Server Error - " + e);
			e.printStackTrace(System.out);
			System.exit(0);
		}

		// loop forever
		while (true) {
			} // TODO patrickc catch interrupt to deregister server
		}

	/**
	 * Sets the business symbol handled by this server. Can only be set once.
	 * 
	 * @param businessSymbol
	 */
	protected void setBusinessSymbol(String businessSymbol) {
		if (business.getTicker().equals(""))
			business.setBusinessSymbol(businessSymbol);
	}

	/**
	 * Initializes ORB and registers to naming service
	 * 
	 * @throws InvalidName
	 * @throws NotFound
	 * @throws CannotProceed
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName
	 */
	private void InitORB() throws InvalidName, NotFound, CannotProceed,
			org.omg.CosNaming.NamingContextPackage.InvalidName {
		// Create ORB
		ORB orb = ORB.init(new String[0], null);

		// Connect object to ORB
		orb.connect((Object) business);

		// TODO: patrickc log the event

		// Get nameservice reference
		org.omg.CORBA.Object object = orb
				.resolve_initial_references("NameService");

		// Cast to naming context
		NamingContext namingContext = NamingContextHelper.narrow(object);

		// Add a new naming component for our interface
		NameComponent list[] = { new NameComponent("business-"
				+ business.getTicker(), "") };

		// Now notify naming service of our new interface
		namingContext.rebind(list, (Object) business);
	}


	/**
	 * @return true if successful, false if not
	 * @throws NotFound
	 * @throws CannotProceed
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName
	 * @throws InvalidName
	 */
	private boolean RegisterExchange() throws NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, InvalidName {
		// Get the exchange object
		ORB orb = ORB.init(new String[0], null);

		org.omg.CORBA.Object object = orb
				.resolve_initial_references("NameService");

		NamingContext namingContext = NamingContextHelper.narrow(object);

		NameComponent nc_array[] = { new NameComponent("exchange", "") };

		org.omg.CORBA.Object objectReference = namingContext.resolve(nc_array);

		iExchange exchange = iExchangeHelper.narrow(objectReference);

		// Register to it
		return exchange.registerBusiness(business.getTicker(), business.getUnitPrice());
	}

}