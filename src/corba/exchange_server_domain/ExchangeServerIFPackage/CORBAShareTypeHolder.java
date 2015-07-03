package corba.exchange_server_domain.ExchangeServerIFPackage;

/**
 * Holder class for : CORBAShareType
 * 
 * @author OpenORB Compiler
 */
final public class CORBAShareTypeHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal CORBAShareType value
     */
    public corba.exchange_server_domain.ExchangeServerIFPackage.CORBAShareType value;

    /**
     * Default constructor
     */
    public CORBAShareTypeHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public CORBAShareTypeHolder(corba.exchange_server_domain.ExchangeServerIFPackage.CORBAShareType initial)
    {
        value = initial;
    }

    /**
     * Read CORBAShareType from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = CORBAShareTypeHelper.read(istream);
    }

    /**
     * Write CORBAShareType into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        CORBAShareTypeHelper.write(ostream,value);
    }

    /**
     * Return the CORBAShareType TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return CORBAShareTypeHelper.type();
    }

}
