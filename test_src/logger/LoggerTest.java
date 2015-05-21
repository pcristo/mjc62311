package logger;

import org.junit.Before;
import org.junit.Test;


/**
 * Created by Ross on 2015-05-19.
 */
public class LoggerTest {

    @Before
    public void setUp() {
    }

    @Test
    public void logTest() {
        LoggerClient.log("Howdy-5");

    }

}