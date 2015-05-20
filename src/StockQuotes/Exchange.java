package StockQuotes;

/**
 * Listing of stock exchanges used in project
 */
public enum Exchange {
    NASDAQ("NASDAQ"),
    TSE("TSE");

    private String code;

    /**
     *
     * @param code of exchange used in google finance
     */
    Exchange(String code) {
        this.code = code;
    }

    /**
     *
     * @return code of exchange used in google finance
     */
    public String getCode() {
        return code;
    }




}
