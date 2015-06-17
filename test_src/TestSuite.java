import StockQuotes.GoogleFinanceTest;
import business.BusinessTest;
import client.BrokerServiceClientTest;
import common.logger.LoggerTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import stockexchange.broker.BrokerTest;
import stockexchange.exchange.ExchangeTest;
import stockexchange.exchange.ShareSalesStatusListTest;

@RunWith(Suite.class)
/**
TO RUN
        * 0) Update json.config
        * 2) Compile entire project
        * 3) Run LoggerServer.java
        * 4) Run Business.java
        * 5) Run Broker.java
        * 6) Run this test
        * 7) Validate log file
**/
@Suite.SuiteClasses({BrokerServiceClientTest.class,
                    LoggerTest.class,
                    BusinessTest.class,
                    BrokerTest.class,
                    GoogleFinanceTest.class,
                    BusinessTest.class,
                    ShareSalesStatusListTest.class,
                    ExchangeTest.class})

public class TestSuite {
    static Thread thread;

    @BeforeClass
    public static void setUpClass()  {
        thread = new Thread() {
            public void run() {
                try {
                    projectLauncher.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            Thread.sleep(1000);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    @AfterClass
    public static void tearDownClass() {
        thread.interrupt();

    }
}