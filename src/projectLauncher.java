import business.Business;
import logger.LoggerServer;
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
	final static String PREFACE_STRING = "******\t";
	
	/**
	 * Will launch all the servers
	 * @param args Send no arguments and the launcher will pause and wait for a key before returning
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException,	IOException {
		
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
		
		// if any arguments are sent, the do not wait for any key, just continue
		if (args.length == 0) {
			System.out.println("Press enter to kill everything uncleanly :D");
			System.in.read();
			
			broker.stop();
			business.stop();
			logger.stop();
				
			System.out.println("Okay, everyone is dead.");
			System.exit(0);	
		}		
	}

	private static void pause(String msg, int ms) throws InterruptedException {		
		for (int t = ms; t > 0; t -= PAUSE_NOTIFICATION_TIME) {
			// System.out.println(PREFACE_STRING + msg + t + " ms");
			Thread.sleep(PAUSE_NOTIFICATION_TIME);
		}
	}
}
