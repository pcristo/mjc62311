package stockexchange.broker;

import common.logger.LoggerClient;
import common.util.Config;
import corba.broker_domain.iBroker;
import corba.broker_domain.iBrokerHelper;
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
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import java.util.Properties;

/**
 * This class creates a new instance of a broker, creates the ORB, registers
 * with the CORBA Naming Service, and runs the server in a background thread
 * 
 * @see http://www.javacoffeebreak.com/articles/javaidl/javaidl.html
 */
public class BrokerServer implements Runnable {
		private BrokerServant broker; 

		/**
		 * Constructor
		 */
		public BrokerServer() {
			broker = new BrokerServant();
		}
		
		/**
		 * Launches a new broker server within a new thread
		 *
		 * @return The thread that was launched
		 */
		public static Thread launch() {		
			BrokerServer brokerServ = new BrokerServer();

			Thread thread = new Thread(() -> brokerServ.run());
			thread.start();

			return thread;
		}

		/**
		 * Initializes the ORB, connects to naming service, keeps the server alive.
		 */
		@Override
		public void run() {
			try {
				InitORB();
			} catch (Exception e) {
				// TODO patrickc log this in the logger
				System.err.println("Broker Server Error - " + e.getMessage());
				e.printStackTrace(System.out);
				System.exit(0);
			}

			// loop forever
			while (true) {
			}
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
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			
			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(broker);
			iBroker href = iBrokerHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name("broker");
			ncRef.rebind(path, href);

			LoggerClient.log("Broker Server ready and waiting...");
		}
}
