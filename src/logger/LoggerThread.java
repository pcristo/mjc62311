package logger;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerThread implements Runnable {

    private BufferedReader fromClient;
    private Socket socket;
    private String fileLocation = "C:\\Users\\Ross\\Dropbox\\Distributed Systems\\project\\resources\\log\\iteration_one.log";


    public LoggerThread(BufferedReader fromClient, Socket socket) {
        this.fromClient = fromClient;
        this.socket = socket;
    }


    public void run() {
        try {
            if(log(fromClient.readLine())) {
                socket.close();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


    private boolean log(String msg) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();


        String finalMsg = dateFormat.format(date) + ": " + msg;

        try {
            synchronized (this) {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileLocation, true)));
                out.println(finalMsg);
                out.close();
                return true;
            }
        } catch(IOException e){
            System.out.println("Unable to save logger");
            return false;
        }
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

