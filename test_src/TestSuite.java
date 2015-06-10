import StockQuotes.GoogleFinanceTest;
import business.BusinessTest;
import client.BrokerServiceClientTest;
import logger.LoggerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import stockexchange.broker.BrokerTest;

@RunWith(Suite.class)
/**
TO RUN
        * 0) Update json.config
        * 1) Start rmiregistry from the out/ directory of project
        * 2) Compile entire project
        * 3) Run LoggerServer.java
        * 4) Run Business.java
        * 5) Run Broker.java
        * 6) Run this test
        * 7) Validate log file
**/
@Suite.SuiteClasses({BrokerServiceClientTest.class, LoggerTest.class, BusinessTest.class, BrokerTest.class, GoogleFinanceTest.class})
public class TestSuite {
    //nothing
}