package clientFrontEnd;

import business.BusinessWSPublisher;
import common.logger.LoggerClient;
import common.logger.LoggerServer;
import replication.ReplicaManager;
import replication.Sequencer;
import stockexchange.exchange.ExchangeWSPublisher;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Run this class either in debug or regular mode to set up all your servers. Works on
 * all platforms.
 * @author patrick
 */
public class projectLauncher {
	final static int PAUSE_NOTIFICATION_TIME = 250;
	final static int WAIT_BETWEEN_LAUNCH_TIME = 750;

	static boolean interactive = true;

	/**
	 * Will launch all the servers
	 * @param args Send no arguments and the launcher will pause and wait for a key before returning
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		// Launch threads
		Thread logger = new Thread(()->LoggerServer.main(null));
		logger.start();
		pause("Launching common.logger and waiting ", WAIT_BETWEEN_LAUNCH_TIME);



		String url = "http://localhost:8080/project/buyShares";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");


		int responseCode = con.getResponseCode();
		if(responseCode != 200) {
			LoggerClient.log("Could not start front end for this request - request will hang");
		}
		con.disconnect();

		pause("Launching Front end for this request ", WAIT_BETWEEN_LAUNCH_TIME);

		Thread seq = new Thread(() -> Sequencer.main(null));
		seq.start();
		pause("Launching Sequencer and waiting ", WAIT_BETWEEN_LAUNCH_TIME);
		Thread rm = new Thread(() -> ReplicaManager.main(null));
		rm.start();
		pause("Launching Replica Manager and waiting ", WAIT_BETWEEN_LAUNCH_TIME);
		System.out.println("Started logger, frontend, seq, rm");


//		ExchangeWSPublisher.main(null);
//		BusinessWSPublisher.createBusiness("GOOG");
//		BusinessWSPublisher.createBusiness("YHOO");
//		BusinessWSPublisher.createBusiness("AAPL");
//		BusinessWSPublisher.createBusiness("MSFT");
//		BusinessWSPublisher.StartAllWebservices();
//		BusinessWSPublisher.RegisterAllWithExchange();


		// if any arguments are sent, the do not wait for any key, just continue
		// only give this option if server is in interactive mode
		if ((args == null || args.length == 0) && interactive) {
			System.out.println("Press enter to kill everything...");
			System.in.read();

			/*// Stop all running threads
			broker.interrupt();*/
			logger.interrupt();
			seq.interrupt();
			rm.interrupt();
			ExchangeWSPublisher.unload();
			BusinessWSPublisher.unload();
				
			System.out.println("Okay, everyone is dead.");
			System.exit(0);	
		}		
	}


	public static void setInteractive(boolean b) {
		interactive = b;
	}

	private static void pause(String msg, int ms) throws InterruptedException {		
		LoggerClient.log(msg);
		for (int t = ms; t > 0; t -= PAUSE_NOTIFICATION_TIME) {
			Thread.sleep(PAUSE_NOTIFICATION_TIME);
		}
	}
}
