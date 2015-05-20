package StockQuotes;

/**
 * Company information (stock symbole and exchange it is traded on)
 */
public enum Company {
    Bombardier("bbd.b", Exchange.TSE),
    Google("GOOG", Exchange.NASDAQ),
    Microsoft("MSFT", Exchange.NASDAQ),
    APPLE("AAPL", Exchange.NASDAQ);

    private String ticker;
    private Exchange exchange;

    /**
     *
     * @param ticker code used to reference stock
     * @param exchange Exchange stock is listed on
     */
    Company(String ticker, Exchange exchange) {
        this.ticker = ticker;
        this.exchange = exchange;
    }

    /**
     *
     * @return ticker code
     */
    public String getTicker() {
        return ticker;
    }

    /**
     *
     * @return exchange code used in google finance
     */
    public String getExchangeCode() {
        return exchange.getCode();
    }
}
