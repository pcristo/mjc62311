package common.util;


import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class ConfigTest {

    @Test
    public void configTest() {
        assertNotNull(Config.getInstance());
        String logServerIP = Config.getInstance().getAttr("logServerIP");

    }



}