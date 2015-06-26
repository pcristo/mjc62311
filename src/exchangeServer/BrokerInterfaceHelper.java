package exchangeServer;

/** 
 * Helper class for : BrokerInterface
 *  
 * @author OpenORB Compiler
 */ 
public class BrokerInterfaceHelper
{
    /**
     * Insert BrokerInterface into an any
     * @param a an any
     * @param t BrokerInterface value
     */
    public static void insert(org.omg.CORBA.Any a, BrokerInterface t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract BrokerInterface from an any
     *
     * @param a an any
     * @return the extracted BrokerInterface value
     */
    public static BrokerInterface extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return BrokerInterfaceHelper.narrow( a.extract_Object() );
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
     * Return the BrokerInterface TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "BrokerInterface" );
        }
        return _tc;
    }

    /**
     * Return the BrokerInterface IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/BrokerInterface:1.0";

    /**
     * Read BrokerInterface from a marshalled stream
     * @param istream the input stream
     * @return the readed BrokerInterface value
     */
    public static BrokerInterface read(org.omg.CORBA.portable.InputStream istream)
    {
        return(BrokerInterface)istream.read_Object(_BrokerInterfaceStub.class);
    }

    /**
     * Write BrokerInterface into a marshalled stream
     * @param ostream the output stream
     * @param value BrokerInterface value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, BrokerInterface value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to BrokerInterface
     * @param obj the CORBA Object
     * @return BrokerInterface Object
     */
    public static BrokerInterface narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof BrokerInterface)
            return (BrokerInterface)obj;

        if (obj._is_a(id()))
        {
            _BrokerInterfaceStub stub = new _BrokerInterfaceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to BrokerInterface
     * @param obj the CORBA Object
     * @return BrokerInterface Object
     */
    public static BrokerInterface unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof BrokerInterface)
            return (BrokerInterface)obj;

        _BrokerInterfaceStub stub = new _BrokerInterfaceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
