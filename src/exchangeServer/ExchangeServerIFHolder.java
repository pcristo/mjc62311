package exchangeServer;

/**
 * Holder class for : ExchangeServerIF
 * 
 * @author OpenORB Compiler
 */
final public class ExchangeServerIFHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal ExchangeServerIF value
     */
    public exchangeServer.ExchangeServerIF value;

    /**
     * Default constructor
     */
    public ExchangeServerIFHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ExchangeServerIFHolder(exchangeServer.ExchangeServerIF initial)
    {
        value = initial;
    }

    /**
     * Read ExchangeServerIF from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = ExchangeServerIFHelper.read(istream);
    }

    /**
     * Write ExchangeServerIF into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        ExchangeServerIFHelper.write(ostream,value);
    }

    /**
     * Return the ExchangeServerIF TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return ExchangeServerIFHelper.type();
    }

}
