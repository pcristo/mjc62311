package exchangeServer;

/**
 * Struct definition: CORBACustomer.
 * 
 * @author OpenORB Compiler
*/
public final class CORBACustomer implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member customerReferenceNumber
     */
    public int customerReferenceNumber;

    /**
     * Struct member name
     */
    public String name;

    /**
     * Struct member street1
     */
    public String street1;

    /**
     * Struct member street2
     */
    public String street2;

    /**
     * Struct member city
     */
    public String city;

    /**
     * Struct member province
     */
    public String province;

    /**
     * Struct member postalCode
     */
    public String postalCode;

    /**
     * Struct member country
     */
    public String country;

    /**
     * Default constructor
     */
    public CORBACustomer()
    { }

    /**
     * Constructor with fields initialization
     * @param customerReferenceNumber customerReferenceNumber struct member
     * @param name name struct member
     * @param street1 street1 struct member
     * @param street2 street2 struct member
     * @param city city struct member
     * @param province province struct member
     * @param postalCode postalCode struct member
     * @param country country struct member
     */
    public CORBACustomer(int customerReferenceNumber, String name, String street1, String street2, String city, String province, String postalCode, String country)
    {
        this.customerReferenceNumber = customerReferenceNumber;
        this.name = name;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
    }

}
