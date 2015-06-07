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
	final static int NUMBER_OF_TEST_THREADS = 2;
	final static int SLEEP_TIME_BETWEEN_TRIES = 250;

	@Before
	public void setUp() throws Exception {
		// start up everything
		projectLauncher.main(new String[1]);
	}

	@Test
	public void MultipleClientOrders() {
		for (int i = 1; i <= NUMBER_OF_TEST_THREADS; i++)  {
			Thread clientThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						DummyClient(RandomString(6));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			clientThread.start();
		}
	}

	private void DummyClient(String customer) throws RemoteException, NotBoundException {
		List<ShareItem> lstShares = new ArrayList<ShareItem>();
		lstShares.add(new ShareItem("", "MSFT", ShareType.COMMON, 540.11f, 100));
		lstShares.add(new ShareItem("", "MSFT.B", ShareType.CONVERTIBLE, 523.32f, 100));
		lstShares.add(new ShareItem("", "MSFT.C", ShareType.PREFERRED, 541.28f,	100));
		lstShares.add(new ShareItem("", "GOOG", ShareType.COMMON, 540.11f, 100));
		lstShares.add(new ShareItem("", "GOOG.B", ShareType.CONVERTIBLE, 523.32f, 100));
		lstShares.add(new ShareItem("", "GOOG.C", ShareType.PREFERRED, 541.28f,	100));
		lstShares.add(new ShareItem("", "GOOG", ShareType.COMMON, 540.11f, 100));

		BrokerInterface service = new BrokerServiceClient().getBroker();

		for (int i = 0; i < 200; i++) {
			int shareIndex = (int) Math.floor(Math.random()
					* (lstShares.size() + 1));

			// TODO: Need a method to call that orders shares
			service.sellShares(lstShares.get(shareIndex).getBusinessSymbol(),
					100, new Customer(customer));

			Thread.sleep(SLEEP_TIME_BETWEEN_TRIES);
		}
	}
	
	private String RandomString(int size) {
		String returnString = "";

		for (int i = 0; i < size; i++) {
			char character = (char) (Math.floor(Math.random() * 26) + 97);
			returnString += character;
		}
		
		return returnString;
	}
	
}
