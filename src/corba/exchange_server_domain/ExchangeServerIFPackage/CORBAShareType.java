package corba.exchange_server_domain.ExchangeServerIFPackage;

/**
 * Enum definition: CORBAShareType.
 *
 * @author OpenORB Compiler
*/
public final class CORBAShareType implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Enum member PREFERRED value 
     */
    public static final int _PREFERRED = 0;

    /**
     * Enum member PREFERRED
     */
    public static final CORBAShareType PREFERRED = new CORBAShareType(_PREFERRED);

    /**
     * Enum member COMMON value 
     */
    public static final int _COMMON = 1;

    /**
     * Enum member COMMON
     */
    public static final CORBAShareType COMMON = new CORBAShareType(_COMMON);

    /**
     * Enum member CONVERTIBLE value 
     */
    public static final int _CONVERTIBLE = 2;

    /**
     * Enum member CONVERTIBLE
     */
    public static final CORBAShareType CONVERTIBLE = new CORBAShareType(_CONVERTIBLE);

    /**
     * Internal member value 
     */
    private final int _CORBAShareType_value;

    /**
     * Private constructor
     * @param  the enum value for this new member
     */
    private CORBAShareType( final int value )
    {
        _CORBAShareType_value = value;
    }

    /**
     * Maintains singleton property for serialized enums.
     * Issue 4271: IDL/Java issue, Mapping for IDL enum.
     */
    public java.lang.Object readResolve() throws java.io.ObjectStreamException
    {
        return from_int( value() );
    }

    /**
     * Return the internal member value
     * @return the member value
     */
    public int value()
    {
        return _CORBAShareType_value;
    }

    /**
     * Return a enum member from its value.
     * @param value An enum value
     * @return An enum member
         */
    public static CORBAShareType from_int( int value )
    {
        switch ( value )
        {
        case 0:
            return PREFERRED;
        case 1:
            return COMMON;
        case 2:
            return CONVERTIBLE;
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    /**
     * Return a string representation
     * @return a string representation of the enumeration
     */
    public java.lang.String toString()
    {
        switch ( _CORBAShareType_value )
        {
        case 0:
            return "PREFERRED";
        case 1:
            return "COMMON";
        case 2:
            return "CONVERTIBLE";
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

}
