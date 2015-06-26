package exchangeServer;

/** 
 * Helper class for : CORBACustomer
 *  
 * @author OpenORB Compiler
 */ 
public class CORBACustomerHelper
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
     * Insert CORBACustomer into an any
     * @param a an any
     * @param t CORBACustomer value
     */
    public static void insert(org.omg.CORBA.Any a, CORBACustomer t)
    {
        a.insert_Streamable(new CORBACustomerHolder(t));
    }

    /**
     * Extract CORBACustomer from an any
     *
     * @param a an any
     * @return the extracted CORBACustomer value
     */
    public static CORBACustomer extract( org.omg.CORBA.Any a )
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
                if ( s instanceof CORBACustomerHolder )
                    return ( ( CORBACustomerHolder ) s ).value;
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            CORBACustomerHolder h = new CORBACustomerHolder( read( a.create_input_stream() ) );
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
     * Return the CORBACustomer TypeCode
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
                org.omg.CORBA.StructMember _members[] = new org.omg.CORBA.StructMember[ 8 ];

                _members[ 0 ] = new org.omg.CORBA.StructMember();
                _members[ 0 ].name = "customerReferenceNumber";
                _members[ 0 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_long );
                _members[ 1 ] = new org.omg.CORBA.StructMember();
                _members[ 1 ].name = "name";
                _members[ 1 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 2 ] = new org.omg.CORBA.StructMember();
                _members[ 2 ].name = "street1";
                _members[ 2 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 3 ] = new org.omg.CORBA.StructMember();
                _members[ 3 ].name = "street2";
                _members[ 3 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 4 ] = new org.omg.CORBA.StructMember();
                _members[ 4 ].name = "city";
                _members[ 4 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 5 ] = new org.omg.CORBA.StructMember();
                _members[ 5 ].name = "province";
                _members[ 5 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 6 ] = new org.omg.CORBA.StructMember();
                _members[ 6 ].name = "postalCode";
                _members[ 6 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 7 ] = new org.omg.CORBA.StructMember();
                _members[ 7 ].name = "country";
                _members[ 7 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _tc = orb.create_struct_tc( id(), "CORBACustomer", _members );
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the CORBACustomer IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/CORBACustomer:1.0";

    /**
     * Read CORBACustomer from a marshalled stream
     * @param istream the input stream
     * @return the readed CORBACustomer value
     */
    public static CORBACustomer read(org.omg.CORBA.portable.InputStream istream)
    {
        CORBACustomer new_one = new CORBACustomer();

        new_one.customerReferenceNumber = istream.read_long();
        new_one.name = istream.read_string();
        new_one.street1 = istream.read_string();
        new_one.street2 = istream.read_string();
        new_one.city = istream.read_string();
        new_one.province = istream.read_string();
        new_one.postalCode = istream.read_string();
        new_one.country = istream.read_string();

        return new_one;
    }

    /**
     * Write CORBACustomer into a marshalled stream
     * @param ostream the output stream
     * @param value CORBACustomer value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, CORBACustomer value)
    {
        ostream.write_long( value.customerReferenceNumber );
        ostream.write_string( value.name );
        ostream.write_string( value.street1 );
        ostream.write_string( value.street2 );
        ostream.write_string( value.city );
        ostream.write_string( value.province );
        ostream.write_string( value.postalCode );
        ostream.write_string( value.country );
    }

}
