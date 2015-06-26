package exchangeServer;

/**
 * Holder class for : CORBAShareOrder
 * 
 * @author OpenORB Compiler
 */
final public class CORBAShareOrderHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal CORBAShareOrder value
     */
    public CORBAShareOrder value;

    /**
     * Default constructor
     */
    public CORBAShareOrderHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public CORBAShareOrderHolder(CORBAShareOrder initial)
    {
        value = initial;
    }

    /**
     * Read CORBAShareOrder from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = CORBAShareOrderHelper.read(istream);
    }

    /**
     * Write CORBAShareOrder into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        CORBAShareOrderHelper.write(ostream,value);
    }

    /**
     * Return the CORBAShareOrder TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return CORBAShareOrderHelper.type();
    }

}
