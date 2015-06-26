package exchangeServer;

/**
 * Interface definition: BusinessInterface.
 * 
 * @author OpenORB Compiler
 */
public interface BusinessInterfaceOperations
{
    /**
     * Operation issueShares
     */
    public boolean issueShares(CORBAShareOrder aSO);

    /**
     * Operation getShareInfo
     */
    public BusinessInfo getShareInfo(CORBAShareType aShareType);

    /**
     * Operation getSharesList
     */
    public BusinessInfo[] getSharesList();

    /**
     * Operation recievePayment
     */
    public boolean recievePayment(String orderNum, float totalPrice);

    /**
     * Operation getTicker
     */
    public String getTicker();

}
