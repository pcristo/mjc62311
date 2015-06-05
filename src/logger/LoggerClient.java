package logger;

import util.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains the static method log used to write a message
 * to the log file specified in boostrap/config.json
 */
public class LoggerClient {

    /**
     * Classname is prefixed to the message being sent to the logging server
     * Log the message to the log server
     *
     * @param msg String message to be recorded
     * @return boolean of successful message is logging
     */
    public static boolean log(String msg) {
        String className = new Exception().getStackTrace()[1].getClassName();
        return log(msg, className);
    }

    /**
     * Log the message to the log server
     *
     * @param msg String message to be logged
     * @param className String className that made the logger call
     * @return boolean of successful message is logging
     */
    public static boolean log(String msg, String className) {
        // Check to see if we have backup server ready to go
        boolean faultTolerance;
        try {
            Config.getInstance().getAttr("backup_logServerIP");
            faultTolerance = true;
        } catch(org.json.JSONException e){
            faultTolerance = false;
        }

        msg = className + " :: " + msg;

        String ip = Config.getInstance().getAttr("logServerIP");
        int port = Integer.parseInt(Config.getInstance().getAttr("logServerPort"));

        boolean logSuccess = sendMessage(msg, ip, port);

        String backup_ip = null;
        Integer backup_port = null;

        if(faultTolerance) {
            backup_ip = Config.getInstance().getAttr("backup_logServerIP");
            backup_port = Integer.parseInt(Config.getInstance().getAttr("backup_logServerPort"));

            boolean backup_logSuccess = sendMessage(msg, backup_ip, backup_port);
            return logSuccess && backup_logSuccess;
        } else {
            return logSuccess;
        }




    }

    /**
     * Send message through sockets to ip on port.
     * @param msg String to be sent to server
     * @param ip String of the server
     * @param port int of the port to send message on
     * @return
     */
    private static boolean sendMessage(String msg, String ip, int port) {

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(1000);
            byte[] sendData = new byte[1024];
            sendData = msg.getBytes();


            InetAddress host = InetAddress.getByName(ip);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,  host , port);
            clientSocket.send(sendPacket);

            byte[] buffer2 = new byte[1024];
            DatagramPacket receivedPacket = new DatagramPacket(buffer2, sendData.length, host, port);
            clientSocket.receive(receivedPacket);

            clientSocket.close();

        } catch(UnknownHostException he){
            System.err.println("Host Exception in client: " + he.getMessage());
            return false;
        } catch (IOException ioe) {
            System.err.println("IO Exception in client: " + ioe.getMessage());
            return false;
        }

        return true;

    }

}