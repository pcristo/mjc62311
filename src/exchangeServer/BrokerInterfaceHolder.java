package exchangeServer;

/**
 * Holder class for : BrokerInterface
 * 
 * @author OpenORB Compiler
 */
final public class BrokerInterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal BrokerInterface value
     */
    public BrokerInterface value;

    /**
     * Default constructor
     */
    public BrokerInterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public BrokerInterfaceHolder(BrokerInterface initial)
    {
        value = initial;
    }

    /**
     * Read BrokerInterface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = BrokerInterfaceHelper.read(istream);
    }

    /**
     * Write BrokerInterface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        BrokerInterfaceHelper.write(ostream,value);
    }

    /**
     * Return the BrokerInterface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return BrokerInterfaceHelper.type();
    }

}
