import StockQuotes.GoogleFinanceTest;
import business.BusinessTest;
import client.BrokerServiceClientTest;
import common.logger.LoggerServer;
import common.logger.LoggerTest;
import common.util.ConfigTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import stockexchange.exchange.ShareSalesStatusListTest;

@RunWith(Suite.class)
/**
 * NOTES:
 *      All integration tests should be put in IntegrationTest.java
 *      This class handles anything that requires the business, broker, exchange server
 *      This test takes 10 seconds to allow time for the servers to be up and running.
 *
 *      ALL OTHER TESTS should be runnable without needing the business, broker exchange server
 *      running.  If you need functionality from one of these servers, create it locally using a Mock class.
 *      (See Mock Package in test_src).
 *
 *      Pass this class before committing code to github.
**/
@Suite.SuiteClasses({
                    IntegrationTest.class,

                    BrokerServiceClientTest.class,
                    LoggerTest.class,
                    BusinessTest.class,
//                    BrokerTest.class,
                    ConfigTest.class,
                    GoogleFinanceTest.class,
                    ShareSalesStatusListTest.class,
 //                   ExchangeTest.class
              })

public class TestSuite {
    static Thread thread;

    @BeforeClass
    public static void setUpClass()  {
        thread = new Thread() {
            public void run() {
                try {
                    // Make sure ORBD is running on 9999
                    LoggerServer.main(null);
          //          projectLauncher.main(null);
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