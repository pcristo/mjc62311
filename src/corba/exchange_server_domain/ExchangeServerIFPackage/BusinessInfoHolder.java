package corba.exchange_server_domain.ExchangeServerIFPackage;

/**
 * Holder class for : BusinessInfo
 * 
 * @author OpenORB Compiler
 */
final public class BusinessInfoHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal BusinessInfo value
     */
    public corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo value;

    /**
     * Default constructor
     */
    public BusinessInfoHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public BusinessInfoHolder(corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo initial)
    {
        value = initial;
    }

    /**
     * Read BusinessInfo from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = BusinessInfoHelper.read(istream);
    }

    /**
     * Write BusinessInfo into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        BusinessInfoHelper.write(ostream,value);
    }

    /**
     * Return the BusinessInfo TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return BusinessInfoHelper.type();
    }

}
