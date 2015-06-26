package exchangeServer;

/**
 * Interface definition: ExchangeServerIF.
 * 
 * @author OpenORB Compiler
 */
public abstract class ExchangeServerIFPOA extends org.omg.PortableServer.Servant
        implements ExchangeServerIFOperations, org.omg.CORBA.portable.InvokeHandler
{
    public ExchangeServerIF _this()
    {
        return ExchangeServerIFHelper.narrow(_this_object());
    }

    public ExchangeServerIF _this(org.omg.CORBA.ORB orb)
    {
        return ExchangeServerIFHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:exchangeServer/ExchangeServerIF:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("getBusiness")) {
                return _invoke_getBusiness(_is, handler);
        } else if (opName.equals("registerBusiness")) {
                return _invoke_registerBusiness(_is, handler);
        } else if (opName.equals("unregisterBusiness")) {
                return _invoke_unregisterBusiness(_is, handler);
        } else if (opName.equals("updateSharePrice")) {
                return _invoke_updateSharePrice(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_getBusiness(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        BusinessInfo _arg_result = getBusiness(arg0_in);

        _output = handler.createReply();
        BusinessInfoHelper.write(_output,_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_updateSharePrice(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        float arg1_in = _is.read_float();

        boolean _arg_result = updateSharePrice(arg0_in, arg1_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_registerBusiness(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        float arg1_in = _is.read_float();

        boolean _arg_result = registerBusiness(arg0_in, arg1_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_unregisterBusiness(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        boolean _arg_result = unregisterBusiness(arg0_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

}
