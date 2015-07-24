package FrontEnd;

import WebServices.Rest;
import common.Customer;
import common.logger.LoggerClient;
import common.util.Config;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Scanner;

public class FrontEnd {

	/** If you are running this...make sure all your server are a running
	 * BrokerServer...ExchangeServer...BusinessServer
	 * @param args
	 */
	public static void main(String[] args) {
		// Start servers
		Thread thread = new Thread() {
			public void run() {
				try {
					projectLauncher.setInteractive(false);
					projectLauncher.main(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();


		try {
			// Wait until servers are up and running
			Thread.sleep(5000);

			Scanner in = new Scanner(System.in);
			int menuIn = 0;
			while(menuIn != 9) {
				System.out.println("\n\n\n~~~WELCOME~~~");
				System.out.println("1 - Buy shares");
				System.out.println("9 - Quit");
				menuIn = in.nextInt();
				in.nextLine();
				if(menuIn == 1) {
					System.out.print("Enter customer name: ");
					String name = in.nextLine();
					Customer customer = new Customer(name);
					System.out.print("Enter stock to purchase: ");
					String ticker = in.nextLine();
					System.out.print("Enter stock type: ");
					String type = in.nextLine();
					System.out.print("Enter quantity: ");
					int qty = in.nextInt();
					in.nextLine();
					System.out.println("Contacting broker to make purchase...");
					// Can fill in address info if you wanted
					boolean response = sellShares(ticker, type, qty, customer);
					if(response) {
						System.out.println("Confirmation shares purchased");
					} else {
						System.out.println("Unable to purchase shares...blame DAS SERVER");
					}
					Thread.sleep(3000);
				}
			}
			System.out.println("~~~GOOD BYE~~~");
			thread.interrupt();
			System.exit(0);

		} catch (Exception e) {
			System.out.println("FrontEnd Exception: " + e.getMessage());
			e.printStackTrace();
		} finally {
			thread.interrupt();
		}
	}

	/**
	 *
	 * @param ticker String symbol
	 * @param type String type of stock
	 * @param qty int amount
	 * @param customer Customer object of buyer
	 * @return true if purchase went through | false on conenction failure or no sale
	 */
	public static boolean sellShares(String ticker, String type, int qty, Customer customer) {
		// Make RESTfull Call to Broker Rest
		String url = Config.getInstance().getAttr("BrokerRest", true);

		// Turn customer into JSON
		String custJson = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			custJson = mapper.writeValueAsString(customer);
		} catch (Exception e) {
			LoggerClient.log("FE excetion sending customer to rest");
			return false;
		}

		// Build param map
		HashMap<String, String> params = new HashMap<String, String>()
		{{
			put("ticker", ticker);
			put("type", type);
			put("qty", Integer.toString(qty));
		}};
		params.put("customer", custJson);

		// Make post call
		String result = Rest.getPost(url, params);
		if(result != null) {
			return true;
		} else {
			return false;
		}
	}


}