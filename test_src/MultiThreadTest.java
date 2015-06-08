import static org.junit.Assert.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
	final static int NUMBER_OF_TEST_THREADS = 4;
	final static int SLEEP_TIME_BETWEEN_TRIES = 1;
	final static int NUMBER_OF_TRANSACTIONS_PER_THREAD = 100;

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
							System.out.println("Uncaught exception: " + ex.getMessage()
									+ "in " + th.getName() + "\n"
									+ "Trace: " + ex.getStackTrace());
						}
					});
			clientThread[i].start();
		}
		
		// wait till all threads are finished
		for (int i = 0; i < NUMBER_OF_TEST_THREADS; i++)
			clientThread[i].join();
	}

	private void DummyClient(String customer) throws RemoteException, NotBoundException {
		System.out.println("Starting dummy client " + customer);
		
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
		System.out.println("Service found for test customer " + customer);

		for (int i = 0; i < NUMBER_OF_TRANSACTIONS_PER_THREAD; i++) {
			int shareIndex = (int) Math.floor(Math.random()
					* lstShares.size());

			// Make a transaction
			ArrayList<String> sellist = new ArrayList<String>();
			sellist.add(lstShares.get(shareIndex).getBusinessSymbol());
			service.sellShares(sellist,lstShares.get(shareIndex).getShareType(),
					100, new Customer(customer));

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
	
}
