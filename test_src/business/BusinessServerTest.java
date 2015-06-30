package business;

import static junit.framework.TestCase.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import common.logger.LoggerServer;

public class BusinessServerTest {
	static Thread logger;

	@BeforeClass
	public static void setUp() throws InterruptedException {
		// start up the logger
		logger = new Thread(() -> LoggerServer.main(null));
		logger.start();
		Thread.sleep(4000);

		if (!logger.isAlive())
			throw new InterruptedException();

		System.out.println("Logger started OK");
	}

	@Test
	public void TestLaunchFourBusinessServers() throws InterruptedException {
		Thread[] servers = { BusinessServer.launch("GOOG"),
				BusinessServer.launch("APPL"), BusinessServer.launch("YHOO"),
				BusinessServer.launch("MSFT") };

		Thread.sleep(5000); // give the threads plenty of time to launch

		// test passes if servers are still running
		for (Thread s : servers) {
			assertTrue(s.isAlive());
			s.interrupt();
			System.out.println("Killed a thread.");
		}
	}
}
