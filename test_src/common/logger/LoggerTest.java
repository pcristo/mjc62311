package common.logger;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class LoggerTest {



    @Test
    public void logTest() {
        boolean response = LoggerClient.log("Test 65");
        assertTrue(response);


        // Manual test - would need to check log file
        TimerLoggerClient timer = new TimerLoggerClient();
        timer.start();
        try {
            Thread.sleep(1000);
        }catch(Exception e){
        }
        timer.end();
    }




}