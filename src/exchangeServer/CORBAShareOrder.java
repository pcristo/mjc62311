package exchangeServer;

/**
 * Struct definition: CORBAShareOrder.
 * 
 * @author OpenORB Compiler
*/
public final class CORBAShareOrder implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member orderNum
     */
    public String orderNum;

    /**
     * Struct member brokerRef
     */
    public String brokerRef;

    /**
     * Struct member quantity
     */
    public float quantity;

    /**
     * Struct member unitPrice
     */
    public float unitPrice;

    /**
     * Struct member unitPriceOrder
     */
    public float unitPriceOrder;

    /**
     * Struct member shareType
     */
    public CORBAShareType shareType;

    /**
     * Struct member businessSymbol
     */
    public String businessSymbol;

    /**
     * Default constructor
     */
    public CORBAShareOrder()
    { }

    /**
     * Constructor with fields initialization
     * @param orderNum orderNum struct member
     * @param brokerRef brokerRef struct member
     * @param quantity quantity struct member
     * @param unitPrice unitPrice struct member
     * @param unitPriceOrder unitPriceOrder struct member
     * @param shareType shareType struct member
     * @param businessSymbol businessSymbol struct member
     */
    public CORBAShareOrder(String orderNum, String brokerRef, float quantity, float unitPrice, float unitPriceOrder, CORBAShareType shareType, String businessSymbol)
    {
        this.orderNum = orderNum;
        this.brokerRef = brokerRef;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOrder = unitPriceOrder;
        this.shareType = shareType;
        this.businessSymbol = businessSymbol;
    }

}
