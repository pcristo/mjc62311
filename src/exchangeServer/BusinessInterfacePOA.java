package exchangeServer;

import exchangeServer.BusinessInterfacePackage.ShareListHelper;

/**
 * Interface definition: BusinessInterface.
 * 
 * @author OpenORB Compiler
 */
public abstract class BusinessInterfacePOA extends org.omg.PortableServer.Servant
        implements BusinessInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{
    public BusinessInterface _this()
    {
        return BusinessInterfaceHelper.narrow(_this_object());
    }

    public BusinessInterface _this(org.omg.CORBA.ORB orb)
    {
        return BusinessInterfaceHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:exchangeServer/BusinessInterface:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("getShareInfo")) {
                return _invoke_getShareInfo(_is, handler);
        } else if (opName.equals("getSharesList")) {
                return _invoke_getSharesList(_is, handler);
        } else if (opName.equals("getTicker")) {
                return _invoke_getTicker(_is, handler);
        } else if (opName.equals("issueShares")) {
                return _invoke_issueShares(_is, handler);
        } else if (opName.equals("recievePayment")) {
                return _invoke_recievePayment(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_issueShares(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        CORBAShareOrder arg0_in = CORBAShareOrderHelper.read(_is);

        boolean _arg_result = issueShares(arg0_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getShareInfo(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        CORBAShareType arg0_in = CORBAShareTypeHelper.read(_is);

        BusinessInfo _arg_result = getShareInfo(arg0_in);

        _output = handler.createReply();
        BusinessInfoHelper.write(_output,_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getSharesList(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        BusinessInfo[] _arg_result = getSharesList();

        _output = handler.createReply();
        ShareListHelper.write(_output,_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_recievePayment(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        float arg1_in = _is.read_float();

        boolean _arg_result = recievePayment(arg0_in, arg1_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getTicker(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        String _arg_result = getTicker();

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

}
