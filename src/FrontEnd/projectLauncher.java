package FrontEnd;

import business.BusinessWSPublisher;
import common.logger.LoggerClient;
import common.logger.LoggerServer;
import stockexchange.exchange.ExchangeWSPublisher;

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
		ExchangeWSPublisher.main(null);
		BusinessWSPublisher.createBusiness("GOOG");
		BusinessWSPublisher.createBusiness("YHOO");
		BusinessWSPublisher.createBusiness("AAPL");
		BusinessWSPublisher.createBusiness("MSFT");
		BusinessWSPublisher.StartAllWebservices();
		BusinessWSPublisher.RegisterAllWithExchange();


		// if any arguments are sent, the do not wait for any key, just continue
		// only give this option if server is in interactive mode
		if ((args == null || args.length == 0) && interactive) {
			System.out.println("Press enter to kill everything...");
			System.in.read();

			/*// Stop all running threads
			broker.interrupt();*/
			logger.interrupt();
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
