import business.Business;
import business.BusinessServant;
import business.BusinessServer;
import common.logger.LoggerServer;
import stockexchange.broker.Broker;

import java.io.IOException;

/**
 * Run this class either in debug or regular mode to set up all your servers. Works on
 * all platforms.
 * @author patrick
 */
public class projectLauncher {
	final static int PAUSE_NOTIFICATION_TIME = 250;
	final static int WAIT_BETWEEN_LAUNCH_TIME = 3000;
	
	/**
	 * Will launch all the servers
	 * @param args Send no arguments and the launcher will pause and wait for a key before returning
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException,	IOException {
		
		// Launch threads
		Thread logger = new Thread(()->LoggerServer.main(null));
		logger.start();
		pause("Launching common.logger and waiting ", WAIT_BETWEEN_LAUNCH_TIME);

		Thread[] businesses = { BusinessServer.launch("GOOG"),
				BusinessServer.launch("APPL"), BusinessServer.launch("YHOO"),
				BusinessServer.launch("MSFT") };
		pause("Launching business and waiting ", WAIT_BETWEEN_LAUNCH_TIME);

		Thread broker = new Thread(()->Broker.main(null));
		broker.start();
		pause("Launching broker and waiting ", WAIT_BETWEEN_LAUNCH_TIME);
		
		// if any arguments are sent, the do not wait for any key, just continue
		if (args == null || args.length == 0) {
			System.out.println("Press enter to kill everything uncleanly :D");
			System.in.read();

			// Stop all running threads
			broker.interrupt();
			for(Thread t : businesses)
				t.interrupt();
			logger.interrupt();
				
			System.out.println("Okay, everyone is dead.");
			System.exit(0);	
		}		
	}

	private static void pause(String msg, int ms) throws InterruptedException {		
		for (int t = ms; t > 0; t -= PAUSE_NOTIFICATION_TIME) {
			Thread.sleep(PAUSE_NOTIFICATION_TIME);
		}
	}
}
