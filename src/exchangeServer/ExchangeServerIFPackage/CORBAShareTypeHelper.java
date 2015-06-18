package exchangeServer.ExchangeServerIFPackage;

/** 
 * Helper class for : CORBAShareType
 *  
 * @author OpenORB Compiler
 */ 
public class CORBAShareTypeHelper
{
    /**
     * Insert CORBAShareType into an any
     * @param a an any
     * @param t CORBAShareType value
     */
    public static void insert(org.omg.CORBA.Any a, exchangeServer.ExchangeServerIFPackage.CORBAShareType t)
    {
        a.type(type());
        write(a.create_output_stream(),t);
    }

    /**
     * Extract CORBAShareType from an any
     *
     * @param a an any
     * @return the extracted CORBAShareType value
     */
    public static exchangeServer.ExchangeServerIFPackage.CORBAShareType extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        return read( a.create_input_stream() );
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the CORBAShareType TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            String []_members = new String[ 3 ];
            _members[ 0 ] = "PREFERRED";
            _members[ 1 ] = "COMMON";
            _members[ 2 ] = "CONVERTIBLE";
            _tc = orb.create_enum_tc( id(), "CORBAShareType", _members );
        }
        return _tc;
    }

    /**
     * Return the CORBAShareType IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/ExchangeServerIF/CORBAShareType:1.0";

    /**
     * Read CORBAShareType from a marshalled stream
     * @param istream the input stream
     * @return the readed CORBAShareType value
     */
    public static exchangeServer.ExchangeServerIFPackage.CORBAShareType read(org.omg.CORBA.portable.InputStream istream)
    {
        return CORBAShareType.from_int(istream.read_ulong());
    }

    /**
     * Write CORBAShareType into a marshalled stream
     * @param ostream the output stream
     * @param value CORBAShareType value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, exchangeServer.ExchangeServerIFPackage.CORBAShareType value)
    {
        ostream.write_ulong(value.value());
    }

}
