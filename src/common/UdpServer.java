package common;

import replication.FrontEnd;
import replication.messageObjects.MessageEnvelope;

import java.util.function.Consumer;

public abstract class UdpServer {
	protected Thread serverThread;
	private UDP<Object> server;
	private Consumer<Object> callableFunction;
	
	/**
	 * Launches the server and listens for messages. You must implement incomingMessageHandler to 
	 * receive and process messages
	 */
	public void launch(int port) {
		if (serverThread != null)
			return;
		
		serverThread = new Thread() {
			public void run() {
				server = new UDP<>(port);
				callableFunction = (Object o) -> incomingMessageConverter(o);
				server.startServer(callableFunction);
			}
		};
		serverThread.start();
	}
	
	/**
	 * Sends an object to a remote UDP server
	 * @param t The object
	 * @param ip
	 * @param port
	 * @return
	 */
	public boolean send(MessageEnvelope m, String ip, int port) {
		if (server == null) return false;
		return server.send(m, ip, port);
	}

	/**
	 * Sends an object to a remote UDP server
	 * @param m
	 * @param ip
	 * @param port
	 * @return
	 */
	public boolean send(MessageEnvelope m, String ip, String port) {
		return send(m, ip, Integer.parseInt(port));
	}
	
	private void incomingMessageConverter(Object o) {
		try {
			incomingMessageHandler((MessageEnvelope) o);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Malformed message received");
			// ignore weird message
		}
	}	
	
	/**
	 * Is called each time a message is received.
	 * @param o The object received inside the UDP message.
	 */
	protected abstract void incomingMessageHandler(MessageEnvelope me);


}
