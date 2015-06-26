package exchangeServer;

/**
 * Interface definition: ExchangeServerIF.
 * 
 * @author OpenORB Compiler
 */
public interface ExchangeServerIFOperations
{
    /**
     * Operation getBusiness
     */
    public BusinessInfo getBusiness(String businessName);

    /**
     * Operation updateSharePrice
     */
    public boolean updateSharePrice(String businessSymbol, float unitPrice);

    /**
     * Operation registerBusiness
     */
    public boolean registerBusiness(String businessSymbol, float unitPrice);

    /**
     * Operation unregisterBusiness
     */
    public boolean unregisterBusiness(String businessSymbol);

}
