package exchangeServer;

/**
 * Holder class for : CORBAShareItem
 * 
 * @author OpenORB Compiler
 */
final public class CORBAShareItemHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal CORBAShareItem value
     */
    public CORBAShareItem value;

    /**
     * Default constructor
     */
    public CORBAShareItemHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public CORBAShareItemHolder(CORBAShareItem initial)
    {
        value = initial;
    }

    /**
     * Read CORBAShareItem from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = CORBAShareItemHelper.read(istream);
    }

    /**
     * Write CORBAShareItem into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        CORBAShareItemHelper.write(ostream,value);
    }

    /**
     * Return the CORBAShareItem TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return CORBAShareItemHelper.type();
    }

}
