package exchangeServer;

/**
 * Holder class for : CORBACustomer
 * 
 * @author OpenORB Compiler
 */
final public class CORBACustomerHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal CORBACustomer value
     */
    public CORBACustomer value;

    /**
     * Default constructor
     */
    public CORBACustomerHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public CORBACustomerHolder(CORBACustomer initial)
    {
        value = initial;
    }

    /**
     * Read CORBACustomer from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = CORBACustomerHelper.read(istream);
    }

    /**
     * Write CORBACustomer into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        CORBACustomerHelper.write(ostream,value);
    }

    /**
     * Return the CORBACustomer TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return CORBACustomerHelper.type();
    }

}
