package logger;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import util.Config;

import javax.annotation.processing.FilerException;

/**
 * Thread that actually handles writing to the log file
 */
public class LoggerThread implements Runnable {

    private BufferedReader fromClient;
    private Socket socket;
    private static final String fileLocation = Config.getInstance().getAttr("logServerFile");

    /**
     *
     * @param fromClient BufferedReader to get messages from clients
     * @param socket Socket to listen on, to be closed after logging the message
     */
    public LoggerThread(BufferedReader fromClient, Socket socket) {
        this.fromClient = fromClient;
        this.socket = socket;
    }

    /**
     * Take the message from the client and prefix it with the date
     * This message is then wrote to the log file (thread safe)
     *
     */
    public void run() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String finalMsg = dateFormat.format(date) + ": " + fromClient.readLine();

            // Make sure one thread writes to a file at a time
            synchronized (this) {
                log(finalMsg);
            }
            socket.close();
        } catch(IOException ioe){
            System.out.println("IO Exception in logger thread: " + ioe.getMessage());
        }
    }

    /**
     * Write message to log file
     * @param message
     */
    public static void log(String message) throws IOException{
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileLocation, true)));
        out.println(message);
        out.close();

    }

    /**
     *
     * @return boolean if file was deleted
     */
    private boolean delete() {
        File file = new File(fileLocation);
        return (file.delete());
    }
}

