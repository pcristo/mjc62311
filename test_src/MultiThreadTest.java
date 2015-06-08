import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import logger.LoggerClient;

import org.junit.Before;
import org.junit.Test;

import client.BrokerServiceClient;
import client.Customer;
import client.ServerDisplayMsgs;
import share.ShareType;
import stockexchange.ShareItem;
import stockexchange.broker.Broker;
import stockexchange.broker.BrokerInterface;

public class MultiThreadTest {
	final static int NUMBER_OF_TEST_THREADS = 15;
	final static int SLEEP_TIME_BETWEEN_TRIES = 0;
	final static int NUMBER_OF_TRANSACTIONS_PER_THREAD = 30000;

	@Before
	public void setUp() throws Exception {
		// start up everything
		projectLauncher.main(new String[1]);
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
		lstShares.add(new ShareItem("", "MSFT", ShareType.COMMON, 50.11f, 100));				// should succeed
		lstShares.add(new ShareItem("", "GOOG", ShareType.COMMON, 540.11f, 100));				// should succeed
		lstShares.add(new ShareItem("", "GOOG.C", ShareType.PREFERRED, 541.28f,	100));			// should succeed
		lstShares.add(new ShareItem("", "YHOO.C", ShareType.PREFERRED, 48.22f, 100));			// should succeed
		// problem orders:
		lstShares.add(new ShareItem("", "MSFT.B", ShareType.CONVERTIBLE, 5.32f, 50));			// test 50 shares issued
		lstShares.add(new ShareItem("", "MSFT.C", ShareType.PREFERRED, 49.42f, 200));			// test 200 shares issued
		lstShares.add(new ShareItem("", "GOOG.B", ShareType.CONVERTIBLE, 523.32f, 100));		// should fail due to price
		lstShares.add(new ShareItem("", "YHOX", ShareType.COMMON, 43.67f, 100));				// bad symbol
		lstShares.add(new ShareItem("", "YHOO.B", ShareType.COMMON, 47.42f, 100));				// invalid share type (symbol indicates convertible)
		
		BrokerInterface service = new BrokerServiceClient().getBroker();
		log("Broker service found for test customer " + customer);

		for (int i = 0; i < NUMBER_OF_TRANSACTIONS_PER_THREAD; i++) {
			int shareIndex = (int) Math.floor(Math.random()
					* lstShares.size());

			// Make a transaction
			ArrayList<String> sellist = new ArrayList<String>();
			sellist.add(lstShares.get(shareIndex).getBusinessSymbol());
			if (!service.sellShares(sellist,lstShares.get(shareIndex).getShareType(),
					lstShares.get(shareIndex).getQuantity(), new Customer(customer))) 
				log("Client " + customer + " failed to purchase " + lstShares.get(shareIndex).getQuantity() + " " +
						lstShares.get(shareIndex).getShareType() + " shares of " + lstShares.get(shareIndex).getBusinessSymbol());

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
