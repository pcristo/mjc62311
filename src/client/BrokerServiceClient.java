package client;

import exchange_domain.*;

import exchange_domain.iExchangePackage.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;

import common.util.Config;

import java.util.Properties;

/**
 * Created by Gay on 6/29/2015.
 */
public class BrokerServiceClient implements Runnable {

	public static Thread launch(){

		BrokerServiceClient client = new BrokerServiceClient();
		Thread thread = new Thread(() -> client.run());
		thread.start();

		return thread;
	}


	public void run() {
		try {

			corShareItem[] toSell = new corShareItem[]
					{
							new corShareItem("","GOOG",0,800,1000),
							new corShareItem("","MSFT",0,700,230),
							new corShareItem("","GOOG",0,800,430),
							new corShareItem("","APPL",0,900,400),
							new corShareItem("","YHOO",0,680,170),

					};


			// Set up ORB properties
			// Hi Gay! I had to add this to allow setting of port and IP
			// address. see Config.json. -patrick
			Properties p = new Properties();
			p.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
			p.put("org.omg.CORBA.ORBInitialHost", Config.getInstance().getAttr("namingServiceAddr"));

			ORB orb = ORB.init(new String[0], p);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			iExchange exchange = (iExchange) iExchangeHelper.narrow(ncRef.resolve_str("exchange"));

			exchange.clientOrder(toSell, new customer("Gay","","","","","",""));


		} catch (Exception e) {
			System.out.println("Test Client exception: " + e);
			e.printStackTrace();
		}
	}
}