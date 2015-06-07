import java.rmi.RemoteException;

import client.BrokerServiceClient;
import stockexchange.broker.Broker;
import business.Business;
import logger.LoggerServer;

public class projectLauncher {
	final static int PAUSE_NOTIFICATION_TIME = 250;
	final static int WAIT_BETWEEN_LAUNCH_TIME = 2000;
	final static String PREFACE_STRING = "******\t";
	
	public static void main(String[] args) throws InterruptedException,
			RemoteException {
		
		// Launch threads
		Thread logger = new Thread(new Runnable() {
			@Override
			public void run() {
				LoggerServer.main(null);
			}
		});
		logger.start();
		pause("Launching logger and waiting ", WAIT_BETWEEN_LAUNCH_TIME);

		Thread business = new Thread(new Runnable() {
			@Override
			public void run() {
				Business.main(null);
			}
		});
		business.start();
		pause("Launching business and waiting ", WAIT_BETWEEN_LAUNCH_TIME);

		Thread broker = new Thread(new Runnable() {
			@Override
			public void run() {
				Broker.main(null);
			}
		});
		broker.start();
		pause("Launching broker and waiting ", WAIT_BETWEEN_LAUNCH_TIME);

		if (args.length == 0 ){		
			// let everything get rolling before starting the client
			System.out.println(PREFACE_STRING + "Client starting... ");

			BrokerServiceClient.main(null);
		}
	}

	private static void pause(String msg, int ms) throws InterruptedException {		
		for (int t = ms; t > 0; t -= PAUSE_NOTIFICATION_TIME) {
			System.out.println(PREFACE_STRING + msg + t + " ms");
			Thread.sleep(PAUSE_NOTIFICATION_TIME);
		}
	}
}
