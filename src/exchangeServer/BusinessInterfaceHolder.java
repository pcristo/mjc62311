package exchangeServer;

/**
 * Holder class for : BusinessInterface
 * 
 * @author OpenORB Compiler
 */
final public class BusinessInterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal BusinessInterface value
     */
    public BusinessInterface value;

    /**
     * Default constructor
     */
    public BusinessInterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public BusinessInterfaceHolder(BusinessInterface initial)
    {
        value = initial;
    }

    /**
     * Read BusinessInterface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = BusinessInterfaceHelper.read(istream);
    }

    /**
     * Write BusinessInterface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        BusinessInterfaceHelper.write(ostream,value);
    }

    /**
     * Return the BusinessInterface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return BusinessInterfaceHelper.type();
    }

}
