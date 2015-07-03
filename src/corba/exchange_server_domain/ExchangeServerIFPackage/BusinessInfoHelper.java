package corba.exchange_server_domain.ExchangeServerIFPackage;

/** 
 * Helper class for : BusinessInfo
 *  
 * @author OpenORB Compiler
 */ 
public class BusinessInfoHelper
{
    private static final boolean HAS_OPENORB;
    static
    {
        boolean hasOpenORB = false;
        try
        {
            Thread.currentThread().getContextClassLoader().loadClass( "org.openorb.orb.core.Any" );
            hasOpenORB = true;
        }
        catch ( ClassNotFoundException ex )
        {
            // do nothing
        }
        HAS_OPENORB = hasOpenORB;
    }
    /**
     * Insert BusinessInfo into an any
     * @param a an any
     * @param t BusinessInfo value
     */
    public static void insert(org.omg.CORBA.Any a, corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo t)
    {
        a.insert_Streamable(new corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfoHolder(t));
    }

    /**
     * Extract BusinessInfo from an any
     *
     * @param a an any
     * @return the extracted BusinessInfo value
     */
    public static corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        if (HAS_OPENORB && a instanceof org.openorb.orb.core.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.orb.core.Any any = (org.openorb.orb.core.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if ( s instanceof corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfoHolder )
                    return ( ( corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfoHolder ) s ).value;
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfoHolder h = new corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfoHolder( read( a.create_input_stream() ) );
            a.insert_Streamable( h );
            return h.value;
        }
        return read( a.create_input_stream() );
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;
    private static boolean _working = false;

    /**
     * Return the BusinessInfo TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            synchronized(org.omg.CORBA.TypeCode.class) {
                if (_tc != null)
                    return _tc;
                if (_working)
                    return org.omg.CORBA.ORB.init().create_recursive_tc(id());
                _working = true;
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
                org.omg.CORBA.StructMember _members[] = new org.omg.CORBA.StructMember[ 3 ];

                _members[ 0 ] = new org.omg.CORBA.StructMember();
                _members[ 0 ].name = "businessSymbol";
                _members[ 0 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 1 ] = new org.omg.CORBA.StructMember();
                _members[ 1 ].name = "shareType";
                _members[ 1 ].type = corba.exchange_server_domain.ExchangeServerIFPackage.CORBAShareTypeHelper.type();
                _members[ 2 ] = new org.omg.CORBA.StructMember();
                _members[ 2 ].name = "unitPrice";
                _members[ 2 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_float );
                _tc = orb.create_struct_tc( id(), "BusinessInfo", _members );
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the BusinessInfo IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/ExchangeServerIF/BusinessInfo:1.0";

    /**
     * Read BusinessInfo from a marshalled stream
     * @param istream the input stream
     * @return the readed BusinessInfo value
     */
    public static corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo read(org.omg.CORBA.portable.InputStream istream)
    {
        corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo new_one = new corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo();

        new_one.businessSymbol = istream.read_string();
        new_one.shareType = corba.exchange_server_domain.ExchangeServerIFPackage.CORBAShareTypeHelper.read(istream);
        new_one.unitPrice = istream.read_float();

        return new_one;
    }

    /**
     * Write BusinessInfo into a marshalled stream
     * @param ostream the output stream
     * @param value BusinessInfo value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, corba.exchange_server_domain.ExchangeServerIFPackage.BusinessInfo value)
    {
        ostream.write_string( value.businessSymbol );
        corba.exchange_server_domain.ExchangeServerIFPackage.CORBAShareTypeHelper.write( ostream, value.shareType );
        ostream.write_float( value.unitPrice );
    }

}
