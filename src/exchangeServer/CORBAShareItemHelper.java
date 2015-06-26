package exchangeServer;

/** 
 * Helper class for : CORBAShareItem
 *  
 * @author OpenORB Compiler
 */ 
public class CORBAShareItemHelper
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
     * Insert CORBAShareItem into an any
     * @param a an any
     * @param t CORBAShareItem value
     */
    public static void insert(org.omg.CORBA.Any a, CORBAShareItem t)
    {
        a.insert_Streamable(new CORBAShareItemHolder(t));
    }

    /**
     * Extract CORBAShareItem from an any
     *
     * @param a an any
     * @return the extracted CORBAShareItem value
     */
    public static CORBAShareItem extract( org.omg.CORBA.Any a )
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
                if ( s instanceof CORBAShareItemHolder )
                    return ( ( CORBAShareItemHolder ) s ).value;
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            CORBAShareItemHolder h = new CORBAShareItemHolder( read( a.create_input_stream() ) );
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
     * Return the CORBAShareItem TypeCode
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
                org.omg.CORBA.StructMember _members[] = new org.omg.CORBA.StructMember[ 4 ];

                _members[ 0 ] = new org.omg.CORBA.StructMember();
                _members[ 0 ].name = "businessSymbol";
                _members[ 0 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 1 ] = new org.omg.CORBA.StructMember();
                _members[ 1 ].name = "shareType";
                _members[ 1 ].type = CORBAShareTypeHelper.type();
                _members[ 2 ] = new org.omg.CORBA.StructMember();
                _members[ 2 ].name = "unitPrice";
                _members[ 2 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_float );
                _members[ 3 ] = new org.omg.CORBA.StructMember();
                _members[ 3 ].name = "orderNum";
                _members[ 3 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_float );
                _tc = orb.create_struct_tc( id(), "CORBAShareItem", _members );
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the CORBAShareItem IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/CORBAShareItem:1.0";

    /**
     * Read CORBAShareItem from a marshalled stream
     * @param istream the input stream
     * @return the readed CORBAShareItem value
     */
    public static CORBAShareItem read(org.omg.CORBA.portable.InputStream istream)
    {
        CORBAShareItem new_one = new CORBAShareItem();

        new_one.businessSymbol = istream.read_string();
        new_one.shareType = CORBAShareTypeHelper.read(istream);
        new_one.unitPrice = istream.read_float();
        new_one.orderNum = istream.read_float();

        return new_one;
    }

    /**
     * Write CORBAShareItem into a marshalled stream
     * @param ostream the output stream
     * @param value CORBAShareItem value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, CORBAShareItem value)
    {
        ostream.write_string( value.businessSymbol );
        CORBAShareTypeHelper.write( ostream, value.shareType );
        ostream.write_float( value.unitPrice );
        ostream.write_float( value.orderNum );
    }

}
