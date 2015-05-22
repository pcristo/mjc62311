package util;


import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest {

    @Test
    public void configTest() {
        String logServerIP = Config.getInstance().getAttr("logServerIP");
    }

}