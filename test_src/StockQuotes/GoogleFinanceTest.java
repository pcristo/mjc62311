package stockQuotes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import stockQuotes.Company;
import stockQuotes.Exchange;
import stockQuotes.GoogleFinance;


public class GoogleFinanceTest {
    private GoogleFinance gf;

    @Before
    public void setUp() throws Exception {
        gf = new GoogleFinance();
    }

    @Test
    public void getStockTest() {
        try {
            String price = gf.getStock(new Company("APPL", new Exchange("NASDAQ")));
            System.out.println(price);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
    }
}