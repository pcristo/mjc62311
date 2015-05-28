import client.BrokerServiceClientTest;
import org.junit.runners.Suite;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
/**
TO RUN
        * 0) Update json.config
        * 1) Start rmiregistry from the out/ directory of project
        * 2) Compile entire project
        * 3) Run Business.java
        * 4) Run Broker.java
        * 5) Run this test
**/
@Suite.SuiteClasses({BrokerServiceClientTest.class})
public class TestSuite {
    //nothing
}