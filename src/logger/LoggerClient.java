package logger;

import util.Config;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

/**
 * This class contains the static method log used to write a message
 * to the log file specified in boostrap/config.json
 */
public class LoggerClient {

    /**
     * Classname is prefixed to the message being sent to the logging server
     * @param msg message to be recorded
     * @return true if message is logged, false otherwise
     */
    public static boolean log(String msg) {
        String className = new Exception().getStackTrace()[1].getClassName();
        return log(msg, className);
    }


    public static boolean log(String msg, String className) {

        msg = className + " :: " + msg;

        String ip = Config.getInstance().getAttr("logServerIP");

        int port = Integer.parseInt(Config.getInstance().getAttr("logServerPort"));
        try {

            DatagramSocket clientSocket = new DatagramSocket();
            byte[] sendData = new byte[1024];
            sendData = msg.getBytes();

            InetAddress host = InetAddress.getByName(ip);


            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,  host , port);
            clientSocket.send(sendPacket);
            clientSocket.close();

        } catch(UnknownHostException he){
            System.out.println("Host Exception in client: " + he.getMessage());
            return false;
        } catch (IOException ioe) {
            System.out.println("IO Exception in client: " + ioe.getMessage());
            return false;
        }

        return true;
    }

}