package exchangeServer;

/** 
 * Helper class for : BusinessInterface
 *  
 * @author OpenORB Compiler
 */ 
public class BusinessInterfaceHelper
{
    /**
     * Insert BusinessInterface into an any
     * @param a an any
     * @param t BusinessInterface value
     */
    public static void insert(org.omg.CORBA.Any a, BusinessInterface t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract BusinessInterface from an any
     *
     * @param a an any
     * @return the extracted BusinessInterface value
     */
    public static BusinessInterface extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return BusinessInterfaceHelper.narrow( a.extract_Object() );
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
     * Return the BusinessInterface TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "BusinessInterface" );
        }
        return _tc;
    }

    /**
     * Return the BusinessInterface IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/BusinessInterface:1.0";

    /**
     * Read BusinessInterface from a marshalled stream
     * @param istream the input stream
     * @return the readed BusinessInterface value
     */
    public static BusinessInterface read(org.omg.CORBA.portable.InputStream istream)
    {
        return(BusinessInterface)istream.read_Object(_BusinessInterfaceStub.class);
    }

    /**
     * Write BusinessInterface into a marshalled stream
     * @param ostream the output stream
     * @param value BusinessInterface value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, BusinessInterface value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to BusinessInterface
     * @param obj the CORBA Object
     * @return BusinessInterface Object
     */
    public static BusinessInterface narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof BusinessInterface)
            return (BusinessInterface)obj;

        if (obj._is_a(id()))
        {
            _BusinessInterfaceStub stub = new _BusinessInterfaceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to BusinessInterface
     * @param obj the CORBA Object
     * @return BusinessInterface Object
     */
    public static BusinessInterface unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof BusinessInterface)
            return (BusinessInterface)obj;

        _BusinessInterfaceStub stub = new _BusinessInterfaceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
