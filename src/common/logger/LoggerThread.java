package common.logger;

import common.util.Config;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread that actually handles writing to the log file
 */
public class LoggerThread implements Runnable {

	//Removed parameters are not used:
    //private BufferedReader fromClient;
    //private Socket socket;

    private String msg;
    private static Object fileLock = new Object();
    

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
            System.out.println("IO Exception in common.logger thread: " + ioe.getMessage());
        }
    }

    /**
     * Write message to log file
     * @param message
     */
    public static void log(String message) throws IOException {
        // Get log file location
        String projectHome = Config.getInstance().getAttr("projectHome");
        String relativeLogFile = Config.getInstance().getAttr("logServerFile");
        String fileLocation = projectHome + relativeLogFile;
        synchronized (fileLock) {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileLocation, true)));
            out.println(message);
            out.close();
        }
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

