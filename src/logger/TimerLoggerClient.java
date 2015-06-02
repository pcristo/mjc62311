package logger;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class logs the execution time of a set of commands.
 */
public class TimerLoggerClient extends LoggerClient {

    // Store start time of the code being measured
    // Secure a random identifier of the process
    private long startTime;
    private SecureRandom random;
    private String id;

    /**
     * Start the timer
     */
    public void start() {
        random = new SecureRandom();
        id = new BigInteger(130, random).toString(32);
        String msg = "\t" + id + " - Time started!";
        String className = new Exception().getStackTrace()[1].getClassName();
        TimerLoggerClient.log(msg, className);
        startTime = System.currentTimeMillis();
    }

    /**
     * End the timer
     */
    public void end() {
        long stopTime = System.currentTimeMillis();
        String msg = "\t" + id + " - Time stopped!  Total time is: " + (stopTime - startTime) + " ms.";
        String className = new Exception().getStackTrace()[1].getClassName();
        TimerLoggerClient.log(msg, className);
    }



}
