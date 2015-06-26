package exchangeServer.BrokerInterfacePackage;

import exchangeServer.CORBAShareItem;

/**
 * Holder class for : shareItemList
 * 
 * @author OpenORB Compiler
 */
final public class shareItemListHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal shareItemList value
     */
    public CORBAShareItem[] value;

    /**
     * Default constructor
     */
    public shareItemListHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public shareItemListHolder(CORBAShareItem[] initial)
    {
        value = initial;
    }

    /**
     * Read shareItemList from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = shareItemListHelper.read(istream);
    }

    /**
     * Write shareItemList into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        shareItemListHelper.write(ostream,value);
    }

    /**
     * Return the shareItemList TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return shareItemListHelper.type();
    }

}
