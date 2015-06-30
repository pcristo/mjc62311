package business;

import org.omg.CORBA.*;
import org.omg.CORBA.Object;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

/**
 * This class creates a new instance of a business, creates the ORB, 
 * registers with the CORBA Naming Service, and runs the server in 
 * a background thread
 *  
 * @see http://www.javacoffeebreak.com/articles/javaidl/javaidl.html
 * @author patrick
 */
public class BusinessServer implements Runnable {
	private BusinessServant business;
	private String businessSymbol = "";
	
	/**
	 * Initializes the ORB, connects to naming service, keeps 
	 * the server alive. You should call the static method launch(String symbol)
	 * instead of calling run directly
	 */
	@Override
	public void run() {
		try {
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
			NameComponent list[] = { new NameComponent("business-" + businessSymbol, "") };

			// Now notify naming service of our new interface
			namingContext.rebind(list, (Object) business);

		} catch (Exception e) {
			System.err.println("ORB Error - " + e);
			e.printStackTrace(System.out);
			System.exit(0);
		}
		
		// loop forever
		while (true) {}
	}
	
	/**
	 * Sets the business symbol handled by this server. Can only be set once.
	 * 
	 * @param businessSymbol
	 */
	protected void setBusinessSymbol(String businessSymbol) {
		if (this.businessSymbol.equals("")) {
			this.businessSymbol = businessSymbol;
		}
	}
		
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
}