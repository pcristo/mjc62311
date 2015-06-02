package logger;

import org.junit.Before;
import org.junit.Test;

public class LoggerTest {

    @Before
    public void setUp() {
    }

    @Test
    public void logTest() {
        LoggerClient.log("Test 32");
        TimerLoggerClient timer = new TimerLoggerClient();
        timer.start();
        try {
            Thread.sleep(1000);
        }catch(Exception e){
        }
        timer.end();

    }

}