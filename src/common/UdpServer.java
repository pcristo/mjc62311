package common;

import java.util.function.Consumer;

public abstract class UdpServer {
	/**
	 * Launches the server and listens for messages. You must implement incomingMessageHandler to 
	 * receive and process messages
	 */
	public void launch(int port) {
        UDP<Object> server = new UDP<>(port);

        Consumer<Object> callableFunction = (Object o) -> incomingMessageHandler(o);

        server.startServer(callableFunction);
	}
	
	/**
	 * Is called each time a message is received.
	 * @param o The object received inside the UDP message.
	 */
	protected abstract void incomingMessageHandler(Object o);
}
