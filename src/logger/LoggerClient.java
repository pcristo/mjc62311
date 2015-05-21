package logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Ross on 2015-05-20.
 */
public class LoggerClient {

    public static boolean log(String msg) {
        String ip = "localhost";
        try {
            Socket socket = new Socket(ip, 9090);

            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);

            String className = new Exception().getStackTrace()[1].getClassName();

            msg = className + " :: " + msg;

            toServer.println(msg);
            socket.close();

        } catch(UnknownHostException he){
            System.out.println(he.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return true;
    }


}




