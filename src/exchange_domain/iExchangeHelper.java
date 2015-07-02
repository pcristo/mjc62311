package exchange_domain;


/**
* exchange_domain/iExchangeHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from stockexchange/exchange.idl
* Wednesday, July 1, 2015 11:19:19 PM EDT
*/

abstract public class iExchangeHelper
{
  private static String  _id = "IDL:exchange_domain/iExchange:1.0";

  public static void insert (org.omg.CORBA.Any a, exchange_domain.iExchange that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static exchange_domain.iExchange extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (exchange_domain.iExchangeHelper.id (), "iExchange");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static exchange_domain.iExchange read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_iExchangeStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, exchange_domain.iExchange value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static exchange_domain.iExchange narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof exchange_domain.iExchange)
      return (exchange_domain.iExchange)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      exchange_domain._iExchangeStub stub = new exchange_domain._iExchangeStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static exchange_domain.iExchange unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof exchange_domain.iExchange)
      return (exchange_domain.iExchange)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      exchange_domain._iExchangeStub stub = new exchange_domain._iExchangeStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
