package exchangeServer;

/**
 * Struct definition: CORBAShareItem.
 * 
 * @author OpenORB Compiler
*/
public final class CORBAShareItem implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member businessSymbol
     */
    public String businessSymbol;

    /**
     * Struct member shareType
     */
    public CORBAShareType shareType;

    /**
     * Struct member unitPrice
     */
    public float unitPrice;

    /**
     * Struct member orderNum
     */
    public float orderNum;

    /**
     * Default constructor
     */
    public CORBAShareItem()
    { }

    /**
     * Constructor with fields initialization
     * @param businessSymbol businessSymbol struct member
     * @param shareType shareType struct member
     * @param unitPrice unitPrice struct member
     * @param orderNum orderNum struct member
     */
    public CORBAShareItem(String businessSymbol, CORBAShareType shareType, float unitPrice, float orderNum)
    {
        this.businessSymbol = businessSymbol;
        this.shareType = shareType;
        this.unitPrice = unitPrice;
        this.orderNum = orderNum;
    }

}
