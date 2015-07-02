package stockexchange.exchange;

import exchange_domain.*;

import exchange_domain.iExchangePackage.*;
import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.*;

import common.util.Config;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import java.io.*;
import java.io.DataInputStream;
import java.util.Properties;

/**
 * Created by Gay on 6/29/2015.
 */
public class TestClient implements Runnable {

	public static Thread launch(){

		TestClient client = new TestClient();
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

			exchange.clientOrder(toSell, new customer("Gay","x","x","x","x","x","x"));


		} catch (Exception e) {
			System.out.println("Test Client exception: " + e);
			e.printStackTrace();
		}
	}
}