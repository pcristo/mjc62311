package FrontEnd;

import common.util.Config;
import corba.broker_domain.iBroker;
import corba.broker_domain.iBrokerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import java.util.Properties;
import java.util.Scanner;

public class FrontEnd {

	/** If you are running this...make sure all your server are a running
	 * BrokerServer...ExchangeServer...BusinessServer
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//Set up ORB properties

			iBroker broker = getBroker();
			Scanner in = new Scanner(System.in);
			int menuIn = 0;
			while(menuIn != 9) {
				System.out.println("~~~WELCOME~~~");
				System.out.println("1 - Buy shares");
				System.out.println("9 - Quit");
				menuIn = in.nextInt();
				in.nextLine();
				if(menuIn == 1) {
					System.out.print("Enter customer name: ");
					String name = in.nextLine();
					System.out.print("Enter stock to purchase: ");
					String ticker = in.nextLine();
					System.out.print("Enter stock type: ");
					String type = in.nextLine();
					System.out.print("Enter quantity: ");
					int qty = in.nextInt();
					in.nextLine();
					System.out.println("Contacting broker to make purchase...");
					// Can fill in address info if you wanted
					int customerID = broker.registerCustomer(name, "", "", "", "", "");
					System.out.println("Broker contacted...customer registered...purchasing shares...");
					boolean response = broker.sellShares(ticker, type, qty, customerID);
					if(response) {
						System.out.println("Confirmation shares purchased");
					} else {
						System.out.println("Unable to purchase shares...blame CORBA");
					}
					Thread.sleep(3000);
				}
				System.out.println("~~~GOOD BYE~~~");
			}

		} catch (Exception e) {
			System.out.println("FrontEnd Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static iBroker getBroker() throws Exception {
		Properties p = new Properties();
		p.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
		p.put("org.omg.CORBA.ORBInitialHost", Config.getInstance().getAttr("namingServiceAddr"));
		ORB orb = ORB.init(new String[0], p);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		iBroker broker = (iBroker) iBrokerHelper.narrow(ncRef.resolve_str("broker"));
		return broker;
	}

}