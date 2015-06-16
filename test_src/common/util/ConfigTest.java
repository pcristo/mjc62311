package common.util;


import org.junit.Test;

public class ConfigTest {

    @Test
    public void configTest() {
        String logServerIP = Config.getInstance().getAttr("logServerIP");
    }

}