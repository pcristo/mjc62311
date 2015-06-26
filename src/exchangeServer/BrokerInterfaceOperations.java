package exchangeServer;

/**
 * Interface definition: BrokerInterface.
 * 
 * @author OpenORB Compiler
 */
public interface BrokerInterfaceOperations
{
    /**
     * Operation buyShares
     */
    public boolean buyShares(String[] list, CORBAShareType type, int quantity, CORBACustomer customer);

    /**
     * Operation sellShares
     */
    public boolean sellShares(String[] list, CORBAShareType type, int quantity, CORBACustomer customer);

    /**
     * Operation getTickerListing
     */
    public String[] getTickerListing();

    /**
     * Operation getBusinessTicker
     */
    public String getBusinessTicker(String businessName);

    /**
     * Operation sellShareList
     */
    public boolean sellShareList(CORBAShareItem[] lstShares, CORBACustomer customer);

}
