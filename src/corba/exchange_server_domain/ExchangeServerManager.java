package corba.exchange_server_domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

/**
 * This is the server thread manager, it will contain the methods to start
 * different server threads: * Start CORBA naming service * register services to
 * CORBA naming service Created by Sai on 2015/6/18.
 */
public class ExchangeServerManager
{
	protected static serverThread m_namingService = null;
	private static ExchangeServerImpl m_server = null;
	private static ExchangeServerImpl m_server2 = null;
	private static ExchangeServerImpl m_server3 = null;
    private static final int NAMING_SERVICE_PORT = 9999;

	public static void main(String[] args) throws InterruptedException
	{
		ExchangeServerManager manager = new ExchangeServerManager();
		manager.startNamingService(NAMING_SERVICE_PORT);
        manager.startServices();
        while(true)
        {
            //keep looping, so main thread won't exit
        }
	}

	public void startServices()
	{
		try
		{
			Properties props = System.getProperties();
			props.put("org.omg.CORBA.ORBInitialPort", Integer.toString(NAMING_SERVICE_PORT));
			props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			// Create and initialize the ORB
			ORB orb = ORB.init(new String[]{}, null);
			POA rootpoa = POAHelper.narrow(orb
                    .resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			m_server = new ExchangeServerImpl("Microsoft");
			m_server.setORB(orb);
			m_server2 = new ExchangeServerImpl("YAHOO");
			m_server2.setORB(orb);
			m_server3 = new ExchangeServerImpl("GOOGLE");
			m_server3.setORB(orb);
			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(m_server);
			org.omg.CORBA.Object ref2 = rootpoa.servant_to_reference(m_server2);
			org.omg.CORBA.Object ref3 = rootpoa.servant_to_reference(m_server3);
            ExchangeServerIF srf =ExchangeServerIFHelper.narrow(ref);
            ExchangeServerIF srf2 = ExchangeServerIFHelper.narrow(ref2);
            ExchangeServerIF srf3 = ExchangeServerIFHelper.narrow(ref3);
			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			String name = "Microsoft";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, srf);
			String name2 = "YAHOO";
			NameComponent path2[] = ncRef.to_name(name2);
			ncRef.rebind(path2, srf2);
			String name3 = "GOOGLE";
			NameComponent path3[] = ncRef.to_name(name3);
			ncRef.rebind(path3, srf3);
			System.out.println("Servers are ready and waiting ...");

			// wait for invocations from clients
			orb.run();
		} catch (Exception e)
		{
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
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
