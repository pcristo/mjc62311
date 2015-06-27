import business.Business;
import business.BusinessServer;
import common.logger.LoggerServer;
import common.util.Config;
import stockexchange.broker.Broker;
import stockexchange.exchange.Exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Run this class either in debug or regular mode to set up all your servers. Works on
 * all platforms.
 * @author patrick
 */
public class projectLauncher {
	final static int PAUSE_NOTIFICATION_TIME = 250;
	final static int WAIT_BETWEEN_LAUNCH_TIME = 3000;
	private serverThread m_namingService;

	/**
	 * Will launch all the servers
	 * @param args Send no arguments and the launcher will pause and wait for a key before returning
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException,	IOException {

		//launch naming service is the VERY first step
		projectLauncher launcher = new projectLauncher();
		launcher.startNamingService(Integer.parseInt(Config.getInstance().getAttr("namingServicePort")));
        Exchange m_exchangeServer = new Exchange();
        BusinessServer.m_exchange = m_exchangeServer;
        System.out.println("exchange server is up and running");
        Thread exchangeThread = new Thread(m_exchangeServer);
        exchangeThread.start();
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

	public void stopNamingService()
	{
		m_namingService.cleanup();
		try
		{
			m_namingService.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * start CORBA naming service at given port number
	 *
	 * @param port
	 */
	private void startNamingService(int port)
	{
		String line = System.getProperty("java.home");
		String startCmd = "\"" + line.replace("\n", "").replace("\r", "")
				+ "\\bin\\tnameserv\" -ORBInitialPort " + port + "&";
		m_namingService = new serverThread(startCmd);
		m_namingService.run();
	}

	private class serverThread extends Thread
	{

		private String m_cmd = "";
		private Process m_process = null;

		public serverThread(String cmd)
		{
			m_cmd = cmd;
		}

		@Override
		public void run()
		{
			runCmd(m_cmd);
		}

		/**
		 * Cleanup the running process, VERY important to release used resources
		 */
		public void cleanup()
		{
			m_process.destroy();
		}

		/**
		 * Execute command, mainly designed to use under Windows, NEED to verify
		 * under Linux or OSX
		 *
		 * @param cmd
		 * @return
		 */
		private String runCmd(String cmd)
		{
			StringBuilder builder = new StringBuilder();

			try
			{
				System.out.println("cmd /c " + cmd);
				m_process = Runtime.getRuntime().exec("cmd /c " + cmd);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(m_process.getInputStream()));
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			return builder.toString();
		}
	}
}
