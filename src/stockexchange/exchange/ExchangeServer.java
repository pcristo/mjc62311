package stockexchange.exchange;

import common.logger.LoggerClient;
import common.util.Config;
import corba.exchange_domain.iExchange;
import corba.exchange_domain.iExchangeHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

public class ExchangeServer implements Runnable {
	private Exchange exchange = new Exchange();

	public void run(){
		try {
			// Set up ORB properties
			Properties p = new Properties();
			p.put("org.omg.CORBA.ORBInitialPort",
					Config.getInstance().getAttr("namingServicePort"));
			p.put("org.omg.CORBA.ORBInitialHost",
					Config.getInstance().getAttr("namingServiceAddr"));

			// Create a new object request broker
			ORB orb = ORB.init(new String[0], p);
			POA rootpoa = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(exchange);
			iExchange href = iExchangeHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name("exchange");
			ncRef.rebind(path, href);

			// TODO: patrickc log the event
			System.out.println("Exchange Server ready and waiting...");

			while (true) {
				// loop forever keeping the server alive
			}
		} catch (Exception e) {
			LoggerClient.log("ExchangeServerException in run: " + e.getMessage());
		}
	}

	/**
	 * Launches a new exchange server within a new thread
	 *
	 * @return The thread that was launched
	 */
	public static Thread launch() {
		ExchangeServer es = new ExchangeServer();

		Thread thread = new Thread(() -> es.run());
		thread.start();

		return thread;
	}
}
