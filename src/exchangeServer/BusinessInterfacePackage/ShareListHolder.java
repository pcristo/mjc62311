package exchangeServer.BusinessInterfacePackage;

import exchangeServer.BusinessInfo;

/**
 * Holder class for : ShareList
 * 
 * @author OpenORB Compiler
 */
final public class ShareListHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal ShareList value
     */
    public BusinessInfo[] value;

    /**
     * Default constructor
     */
    public ShareListHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ShareListHolder(BusinessInfo[] initial)
    {
        value = initial;
    }

    /**
     * Read ShareList from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = ShareListHelper.read(istream);
    }

    /**
     * Write ShareList into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        ShareListHelper.write(ostream,value);
    }

    /**
     * Return the ShareList TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return ShareListHelper.type();
    }

}
