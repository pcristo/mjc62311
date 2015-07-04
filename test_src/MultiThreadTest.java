import corba.broker_domain.iBroker;
import corba.broker_domain.iBrokerHelper;
import common.logger.LoggerClient;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import common.share.ShareType;
import common.util.Config;
import stockexchange.exchange.ShareItem;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MultiThreadTest {
	final static int NUMBER_OF_TEST_THREADS = 15;
	final static int SLEEP_TIME_BETWEEN_TRIES = 0;
	final static int NUMBER_OF_TRANSACTIONS_PER_THREAD = 90000;
	iBroker broker;
	
	@BeforeClass
	public void setUp() throws Exception {
		// start up everything
		projectLauncher.main(new String[1]);
		
		// connect to the broker
		// Set up ORB properties
		Properties p = new Properties();
        p.put("org.omg.CORBA.ORBInitialPort", Config.getInstance().getAttr("namingServicePort"));
        p.put("org.omg.CORBA.ORBInitialHost", Config.getInstance().getAttr("namingServiceAddr"));
        
        // Get the exchange object
		ORB orb = ORB.init(new String[0], p);

		org.omg.CORBA.Object object = orb
				.resolve_initial_references("NameService");

		NamingContext namingContext = NamingContextHelper.narrow(object);

		NameComponent nc_array[] = { new NameComponent("broker", "") };

		org.omg.CORBA.Object objectReference = namingContext.resolve(nc_array);

		broker = iBrokerHelper.narrow(objectReference);
	}

	@Test
	public void MultipleClientOrders() throws InterruptedException {
		Thread[] clientThread = new Thread[NUMBER_OF_TEST_THREADS];

		for (int i = 0; i < NUMBER_OF_TEST_THREADS; i++)  {
			clientThread[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						DummyClient(RandomString(6));
					} catch (RemoteException | NotBoundException e) {
						e.printStackTrace();
					}
				}
			});

			// set an uncaught exception handler (Source:
			// http://stackoverflow.com/questions/6546193/how-to-catch-an-exception-from-a-thread)
			clientThread[i]
					.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
						public void uncaughtException(Thread th, Throwable ex) {
							// stack trace to string, source: http://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							ex.printStackTrace(pw);
							sw.toString(); // stack trace as a string
							
							log("***** Uncaught exception - Trace: " + sw.toString());
						}
					});
		} 
		
		// start all the threads
		for (int i = 0; i < NUMBER_OF_TEST_THREADS; i++) { 
			clientThread[i].start();
			log("Client " + i + " starting to spam");
		}			
		
		// wait till all threads are finished
		for (int i = 0; i < NUMBER_OF_TEST_THREADS; i++) {
			clientThread[i].join();
			log("Client " + i + " finished spamming");
		}
			
	}

	private void DummyClient(String customer) throws RemoteException, NotBoundException {
		log("Starting dummy client " + customer);
		
		List<ShareItem> lstShares = new ArrayList<ShareItem>();
		// "good" orders:
		lstShares.add(new ShareItem("", "MSFT", ShareType.COMMON, 301f, 100));			// should succeed
		lstShares.add(new ShareItem("", "GOOG", ShareType.PREFERRED, 301f, 100));		// should succeed
		lstShares.add(new ShareItem("", "AAPL", ShareType.COMMON, 301f,	100));			// should succeed
		lstShares.add(new ShareItem("", "YHOO", ShareType.CONVERTIBLE, 301f, 100));		// should succeed
		lstShares.add(new ShareItem("", "MSFT", ShareType.CONVERTIBLE, 301f, 50));		// test 50 shares issued
		lstShares.add(new ShareItem("", "MSFT", ShareType.PREFERRED, 301f, 200));		// test 200 shares issued
		// intentional problem orders
		lstShares.add(new ShareItem("", "GOOG", ShareType.CONVERTIBLE, 23.32f, 100));	// should fail due to price
		lstShares.add(new ShareItem("", "BADD", ShareType.COMMON, 543.67f, 100));		// bad symbol
		
		
		// Create a customer
		int customerNumber = broker.registerCustomer("", "", "", "", "", "");
		log("Broker service found for test customer " + customer + " with ID " + customerNumber);

		for (int i = 0; i < NUMBER_OF_TRANSACTIONS_PER_THREAD; i++) {
			int shareIndex = (int) Math.floor(Math.random()
					* lstShares.size());

			// Make a transaction
					
			if (!broker.sellShares(lstShares.get(shareIndex)
					.getBusinessSymbol(), lstShares.get(shareIndex)
					.getShareType().toString(), lstShares.get(shareIndex)
					.getQuantity(), customerNumber)) {
				log("Client " + customer + " failed to purchase "
						+ lstShares.get(shareIndex).getQuantity() + " "
						+ lstShares.get(shareIndex).getShareType()
						+ " shares of "
						+ lstShares.get(shareIndex).getBusinessSymbol()
						+ " on thread " + Thread.currentThread().getId());
			} else {
				log("Client " + customer + " SUCCESSFULLY purchased "
						+ lstShares.get(shareIndex).getQuantity() + " "
						+ lstShares.get(shareIndex).getShareType()
						+ " shares of "
						+ lstShares.get(shareIndex).getBusinessSymbol()
						+ " on thread " + Thread.currentThread().getId());
			}

			try {
				Thread.sleep(SLEEP_TIME_BETWEEN_TRIES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String RandomString(int size) {
		String returnString = "";

		for (int i = 0; i < size; i++) {
			char character = (char) (Math.floor(Math.random() * 26) + 97);
			returnString += character;
		}
		
		return returnString.trim();
	}
	/**
	 * Logs a message to both the console and the logging server
	 * @param msg
	 */
	private void log(String msg) {
		// System.out.println(msg);
		LoggerClient.log(msg, this.getClass().getName());
	}
	
}
