package exchangeServer.BrokerInterfacePackage;

/**
 * Holder class for : tickerList
 * 
 * @author OpenORB Compiler
 */
final public class tickerListHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal tickerList value
     */
    public String[] value;

    /**
     * Default constructor
     */
    public tickerListHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public tickerListHolder(String[] initial)
    {
        value = initial;
    }

    /**
     * Read tickerList from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = tickerListHelper.read(istream);
    }

    /**
     * Write tickerList into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        tickerListHelper.write(ostream,value);
    }

    /**
     * Return the tickerList TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return tickerListHelper.type();
    }

}
