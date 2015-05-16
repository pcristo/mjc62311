package HelloWorld;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ross on 2015-05-16.
 * Test hello world
 * TODO remove this!
 */
public class HelloWorldTest {

    private HelloWorld hw;

    @Before
    public void setUp() {
        hw = new HelloWorld();
    }

    @Test
    public void printTest() {
        assertEquals(hw.run(), "Hello World");
        assertNotEquals(hw.run(), "dlroW olleH");
    }


}