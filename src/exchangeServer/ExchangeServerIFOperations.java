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
    public exchangeServer.ExchangeServerIFPackage.BusinessInfo getBusiness(String businessName);

    /**
     * Operation updateSharePrice
     */
    public boolean updateSharePrice(String businessSymbol, float unitPrice);

    /**
     * Operation registerBuiness
     */
    public boolean registerBuiness(String businessSymbol, float unitPrice);

    /**
     * Operation unregisterBuiness
     */
    public boolean unregisterBuiness(String businessSymbol);

}
