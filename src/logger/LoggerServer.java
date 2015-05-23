package logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import util.Config;

/**
 * Servver used to receive log message
 * Threads are created each time a message is sreceived
 * Thread handles writing to the log file
 */
public class LoggerServer {

    /**
     * Constructor
     * Listens for any incoming log request and delegates it to a new thread
     */
    public LoggerServer() {
        try {
            int port = Integer.parseInt(Config.getInstance().getAttr("logServerPort"));

            ServerSocket listener = new ServerSocket(port);

            while (true) {
                Socket socket = listener.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                LoggerThread logThread = new LoggerThread(fromClient, socket);
                Thread logger = new Thread(logThread);
                logger.start();
            }
        } catch(IOException ioe) {
            System.out.println("Exception: " + ioe.getMessage());
        }
    }


    public static void main(String args[]) {
        LoggerServer loggerServer = new LoggerServer();
    }

}
