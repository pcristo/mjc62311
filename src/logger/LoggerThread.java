package logger;

import util.Config;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread that actually handles writing to the log file
 */
public class LoggerThread implements Runnable {

    private BufferedReader fromClient;
    private Socket socket;

    private String msg;


    public LoggerThread(String msg) {
        this.msg = msg;
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

            String finalMsg = dateFormat.format(date) + ": " + msg;
            log(finalMsg);

        } catch(IOException ioe){
            System.out.println("IO Exception in logger thread: " + ioe.getMessage());
        }
    }

    /**
     * Write message to log file
     * @param message
     */
    public static synchronized void log(String message) throws IOException {
        // Get log file location
        String projectHome = Config.getInstance().getAttr("projectHome");
        String relativeLogFile = Config.getInstance().getAttr("logServerFile");
        String fileLocation = projectHome + relativeLogFile;
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileLocation, true)));
        out.println(message);
        out.close();
    }

    /**
     *
     * @return boolean if file was deleted
     */
//    private boolean delete() {
//        File file = new File(fileLocation);
//        return (file.delete());
//    }
}

