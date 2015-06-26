package exchangeServer;

/** 
 * Helper class for : CORBAShareOrder
 *  
 * @author OpenORB Compiler
 */ 
public class CORBAShareOrderHelper
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
     * Insert CORBAShareOrder into an any
     * @param a an any
     * @param t CORBAShareOrder value
     */
    public static void insert(org.omg.CORBA.Any a, CORBAShareOrder t)
    {
        a.insert_Streamable(new CORBAShareOrderHolder(t));
    }

    /**
     * Extract CORBAShareOrder from an any
     *
     * @param a an any
     * @return the extracted CORBAShareOrder value
     */
    public static CORBAShareOrder extract( org.omg.CORBA.Any a )
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
                if ( s instanceof CORBAShareOrderHolder )
                    return ( ( CORBAShareOrderHolder ) s ).value;
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            CORBAShareOrderHolder h = new CORBAShareOrderHolder( read( a.create_input_stream() ) );
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
     * Return the CORBAShareOrder TypeCode
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
                org.omg.CORBA.StructMember _members[] = new org.omg.CORBA.StructMember[ 7 ];

                _members[ 0 ] = new org.omg.CORBA.StructMember();
                _members[ 0 ].name = "orderNum";
                _members[ 0 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 1 ] = new org.omg.CORBA.StructMember();
                _members[ 1 ].name = "brokerRef";
                _members[ 1 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 2 ] = new org.omg.CORBA.StructMember();
                _members[ 2 ].name = "quantity";
                _members[ 2 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_float );
                _members[ 3 ] = new org.omg.CORBA.StructMember();
                _members[ 3 ].name = "unitPrice";
                _members[ 3 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_float );
                _members[ 4 ] = new org.omg.CORBA.StructMember();
                _members[ 4 ].name = "unitPriceOrder";
                _members[ 4 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_float );
                _members[ 5 ] = new org.omg.CORBA.StructMember();
                _members[ 5 ].name = "shareType";
                _members[ 5 ].type = CORBAShareTypeHelper.type();
                _members[ 6 ] = new org.omg.CORBA.StructMember();
                _members[ 6 ].name = "businessSymbol";
                _members[ 6 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _tc = orb.create_struct_tc( id(), "CORBAShareOrder", _members );
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the CORBAShareOrder IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/CORBAShareOrder:1.0";

    /**
     * Read CORBAShareOrder from a marshalled stream
     * @param istream the input stream
     * @return the readed CORBAShareOrder value
     */
    public static CORBAShareOrder read(org.omg.CORBA.portable.InputStream istream)
    {
        CORBAShareOrder new_one = new CORBAShareOrder();

        new_one.orderNum = istream.read_string();
        new_one.brokerRef = istream.read_string();
        new_one.quantity = istream.read_float();
        new_one.unitPrice = istream.read_float();
        new_one.unitPriceOrder = istream.read_float();
        new_one.shareType = CORBAShareTypeHelper.read(istream);
        new_one.businessSymbol = istream.read_string();

        return new_one;
    }

    /**
     * Write CORBAShareOrder into a marshalled stream
     * @param ostream the output stream
     * @param value CORBAShareOrder value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, CORBAShareOrder value)
    {
        ostream.write_string( value.orderNum );
        ostream.write_string( value.brokerRef );
        ostream.write_float( value.quantity );
        ostream.write_float( value.unitPrice );
        ostream.write_float( value.unitPriceOrder );
        CORBAShareTypeHelper.write( ostream, value.shareType );
        ostream.write_string( value.businessSymbol );
    }

}
