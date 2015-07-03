package corba.exchange_server_domain;

/** 
 * Helper class for : ExchangeServerIF
 *  
 * @author OpenORB Compiler
 */ 
public class ExchangeServerIFHelper
{
    /**
     * Insert ExchangeServerIF into an any
     * @param a an any
     * @param t ExchangeServerIF value
     */
    public static void insert(org.omg.CORBA.Any a, ExchangeServerIF t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract ExchangeServerIF from an any
     *
     * @param a an any
     * @return the extracted ExchangeServerIF value
     */
    public static ExchangeServerIF extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return ExchangeServerIFHelper.narrow(a.extract_Object());
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the ExchangeServerIF TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "ExchangeServerIF" );
        }
        return _tc;
    }

    /**
     * Return the ExchangeServerIF IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/ExchangeServerIF:1.0";

    /**
     * Read ExchangeServerIF from a marshalled stream
     * @param istream the input stream
     * @return the readed ExchangeServerIF value
     */
    public static ExchangeServerIF read(org.omg.CORBA.portable.InputStream istream)
    {
        return(ExchangeServerIF)istream.read_Object(_ExchangeServerIFStub.class);
    }

    /**
     * Write ExchangeServerIF into a marshalled stream
     * @param ostream the output stream
     * @param value ExchangeServerIF value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, ExchangeServerIF value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to ExchangeServerIF
     * @param obj the CORBA Object
     * @return ExchangeServerIF Object
     */
    public static ExchangeServerIF narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof ExchangeServerIF)
            return (ExchangeServerIF)obj;

        if (obj._is_a(id()))
        {
            _ExchangeServerIFStub stub = new _ExchangeServerIFStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to ExchangeServerIF
     * @param obj the CORBA Object
     * @return ExchangeServerIF Object
     */
    public static ExchangeServerIF unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof ExchangeServerIF)
            return (ExchangeServerIF)obj;

        _ExchangeServerIFStub stub = new _ExchangeServerIFStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
