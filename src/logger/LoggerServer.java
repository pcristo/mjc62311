package logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoggerServer {

    public LoggerServer() {
        try {
            ServerSocket listener = new ServerSocket(9090);

            while (true) {
                Socket socket = listener.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                LoggerThread logThread = new LoggerThread(fromClient, socket);
                Thread logger = new Thread(logThread);
                logger.start();
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }


    public static void main(String args[]) {
        LoggerServer loggerServer = new LoggerServer();
    }

}
