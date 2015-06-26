package exchangeServer.BrokerInterfacePackage;

import exchangeServer.CORBAShareItem;
import exchangeServer.CORBAShareItemHelper;

/**
 * Helper class for : shareItemList
 *  
 * @author OpenORB Compiler
 */ 
public class shareItemListHelper
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
     * Insert shareItemList into an any
     * @param a an any
     * @param t shareItemList value
     */
    public static void insert(org.omg.CORBA.Any a, CORBAShareItem[] t)
    {
        a.insert_Streamable(new shareItemListHolder(t));
    }

    /**
     * Extract shareItemList from an any
     *
     * @param a an any
     * @return the extracted shareItemList value
     */
    public static CORBAShareItem[] extract( org.omg.CORBA.Any a )
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
                if ( s instanceof shareItemListHolder )
                {
                    return ( ( shareItemListHolder ) s ).value;
                }
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            shareItemListHolder h = new shareItemListHolder( read( a.create_input_stream() ) );
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
     * Return the shareItemList TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc( id(), "shareItemList", orb.create_sequence_tc( 0, CORBAShareItemHelper.type() ) );
        }
        return _tc;
    }

    /**
     * Return the shareItemList IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:exchangeServer/BrokerInterface/shareItemList:1.0";

    /**
     * Read shareItemList from a marshalled stream
     * @param istream the input stream
     * @return the readed shareItemList value
     */
    public static CORBAShareItem[] read(org.omg.CORBA.portable.InputStream istream)
    {
        CORBAShareItem[] new_one;
        {
        int size7 = istream.read_ulong();
        new_one = new CORBAShareItem[size7];
        for (int i7=0; i7<new_one.length; i7++)
         {
            new_one[i7] = CORBAShareItemHelper.read(istream);

         }
        }

        return new_one;
    }

    /**
     * Write shareItemList into a marshalled stream
     * @param ostream the output stream
     * @param value shareItemList value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, CORBAShareItem[] value)
    {
        ostream.write_ulong( value.length );
        for ( int i7 = 0; i7 < value.length; i7++ )
        {
            CORBAShareItemHelper.write( ostream, value[ i7 ] );

        }
    }

}
