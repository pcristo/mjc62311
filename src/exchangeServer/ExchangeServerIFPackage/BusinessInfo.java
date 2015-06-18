package exchangeServer.ExchangeServerIFPackage;

/**
 * Struct definition: BusinessInfo.
 * 
 * @author OpenORB Compiler
*/
public final class BusinessInfo implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member businessSymbol
     */
    public String businessSymbol;

    /**
     * Struct member shareType
     */
    public exchangeServer.ExchangeServerIFPackage.CORBAShareType shareType;

    /**
     * Struct member unitPrice
     */
    public float unitPrice;

    /**
     * Default constructor
     */
    public BusinessInfo()
    { }

    /**
     * Constructor with fields initialization
     * @param businessSymbol businessSymbol struct member
     * @param shareType shareType struct member
     * @param unitPrice unitPrice struct member
     */
    public BusinessInfo(String businessSymbol, exchangeServer.ExchangeServerIFPackage.CORBAShareType shareType, float unitPrice)
    {
        this.businessSymbol = businessSymbol;
        this.shareType = shareType;
        this.unitPrice = unitPrice;
    }

}
