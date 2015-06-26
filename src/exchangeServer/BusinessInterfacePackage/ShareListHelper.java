package exchangeServer.BusinessInterfacePackage;

import exchangeServer.BusinessInfo;
import exchangeServer.BusinessInfoHelper;

/**
 * Helper class for : ShareList
 *  
 * @author OpenORB Compiler
 */ 
public class ShareListHelper
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
     * Insert ShareList into an any
     * @param a an any
     * @param t ShareList value
     */
    public static void insert(org.omg.CORBA.Any a, BusinessInfo[] t)
    {
        a.insert_Streamable(new ShareListHolder(t));
    }

    /**
     * Extract ShareList from an any
     *
     * @param a an any
     * @return the extracted ShareList value
     */
    public static BusinessInfo[] extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        if ( HAS_OPENORB && a instanceof org.openorb.orb.core.Any )
        {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.orb.core.Any any = ( org.openorb.orb.core.Any ) a;
            try
            {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if ( s instanceof ShareListHolder )
                {
                    return ( ( ShareListHolder ) s ).value;
                }
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            ShareListHolder h = new ShareListHolder( read( a.create_input_stream() ) );
            a.insert_Streamable( h );
            return h.value;
        }
        return read( a.create_input_stream() );
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the ShareList TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc( id(), "ShareList", orb.create_sequence_tc( 0, BusinessInfoHelper.type() ) );
        }
        return _tc;
    }

    /**
     * Return the ShareList IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/BusinessInterface/ShareList:1.0";

    /**
     * Read ShareList from a marshalled stream
     * @param istream the input stream
     * @return the readed ShareList value
     */
    public static BusinessInfo[] read(org.omg.CORBA.portable.InputStream istream)
    {
        BusinessInfo[] new_one;
        {
        int size7 = istream.read_ulong();
        new_one = new BusinessInfo[size7];
        for (int i7=0; i7<new_one.length; i7++)
         {
            new_one[i7] = BusinessInfoHelper.read(istream);

         }
        }

        return new_one;
    }

    /**
     * Write ShareList into a marshalled stream
     * @param ostream the output stream
     * @param value ShareList value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, BusinessInfo[] value)
    {
        ostream.write_ulong( value.length );
        for ( int i7 = 0; i7 < value.length; i7++ )
        {
            BusinessInfoHelper.write( ostream, value[ i7 ] );

        }
    }

}
