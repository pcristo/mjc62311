package logger;

import util.Config;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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
        String ip = Config.getInstance().getAttr("logServerIP");

        int port = Integer.parseInt(Config.getInstance().getAttr("logServerPort"));
        try {
            Socket socket = new Socket(ip, port);

            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);

            String className = new Exception().getStackTrace()[1].getClassName();

            msg = className + " :: " + msg;

            toServer.println(msg);
            socket.close();

        } catch(UnknownHostException he){
            System.out.println("Exception: " + he.getMessage());
            return false;
        } catch (IOException ioe) {
            System.out.println("Exception: " + ioe.getMessage());
            return false;
        }

        return true;
    }
}