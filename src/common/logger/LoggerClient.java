package common.logger;

import common.util.Config;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains the static method log used to write a message
 * to the log file specified in boostrap/config.json
 */
public class LoggerClient {

    static boolean printToScreen = true;
    public static void setPrintToScreen(boolean b) {
        printToScreen = b;
    }

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
     * @param className String className that made the common.logger call
     * @return boolean of successful message is logging
     */
    public static boolean log(String msg, String className) {
    	msg = className + " :: " + msg;
        if(printToScreen) {
            System.out.println(msg);
        }
    	// handle the case where logging has been disabled
    	if (Config.getInstance().getAttr("loggingDisabled").equals("true")) {
            return true;
        }
    	// look up the logger server
    	Config c = Config.getInstance();
        String ip = c.getAttr("logServerIP");

        int port = Integer.parseInt(c.getAttr("logServerPort"));

        boolean logSuccess = sendMessage(msg, ip, port, 0);

        return logSuccess;
        // If you wish to have redundancy
        // Set up a backup logger server and enter the info in config.json and uncomment
        // below.
//        String backup_ip = Config.getInstance().getAttr("backup_logServerIP");
//        String backup_port_config = Config.getInstance().getAttr("backup_logServerPort");
//
//        if(backup_ip != null && backup_port_config != null) {
//            Integer backup_port = Integer.parseInt(backup_port_config);
//
//            boolean backup_logSuccess = sendMessage(msg, backup_ip, backup_port);
//            return logSuccess && backup_logSuccess;
//        } else {
//            return logSuccess;
//        }
    }

    /**
     * Send message through sockets to ip on port.
     * @param msg String to be sent to server
     * @param ip String of the server
     * @param port int of the port to send message on
     * @return
     */
    public static boolean sendMessage(String msg, String ip, int port, int attempts) {
    	try {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(2500);
            byte[] sendData = new byte[1024];
            sendData = msg.getBytes();

            InetAddress host = InetAddress.getByName(ip);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
            clientSocket.send(sendPacket);

            byte[] buffer2 = new byte[1024];
            DatagramPacket receivedPacket = new DatagramPacket(buffer2, sendData.length, host, port);
            clientSocket.receive(receivedPacket);
            clientSocket.close();
        }catch (InterruptedIOException iioe) {
            attempts++;
            System.out.println("Remote connection exception - resending attempts number " + attempts + ":");
            if(attempts > 3) {
                System.out.println("Failed to send message multiple times...giving up");
                return false;
            } else {
                return LoggerClient.sendMessage(msg, ip, port, attempts);
            }
        } catch(UnknownHostException he){
            System.out.println("Host Exception in common.logger client: " + he.getMessage());
            return false;
        } catch (IOException ioe) {
            System.out.println("IO Exception in common.logger client: " + ioe.getMessage());
            return false;
        } finally {
      //      clientSocket.close();
        }

        return true;

    }

}