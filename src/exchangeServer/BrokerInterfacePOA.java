package exchangeServer;

import exchangeServer.BrokerInterfacePackage.shareItemListHelper;
import exchangeServer.BrokerInterfacePackage.tickerListHelper;

/**
 * Interface definition: BrokerInterface.
 * 
 * @author OpenORB Compiler
 */
public abstract class BrokerInterfacePOA extends org.omg.PortableServer.Servant
        implements BrokerInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{
    public BrokerInterface _this()
    {
        return BrokerInterfaceHelper.narrow(_this_object());
    }

    public BrokerInterface _this(org.omg.CORBA.ORB orb)
    {
        return BrokerInterfaceHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:exchangeServer/BrokerInterface:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("buyShares")) {
                return _invoke_buyShares(_is, handler);
        } else if (opName.equals("getBusinessTicker")) {
                return _invoke_getBusinessTicker(_is, handler);
        } else if (opName.equals("getTickerListing")) {
                return _invoke_getTickerListing(_is, handler);
        } else if (opName.equals("sellShareList")) {
                return _invoke_sellShareList(_is, handler);
        } else if (opName.equals("sellShares")) {
                return _invoke_sellShares(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_buyShares(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String[] arg0_in = tickerListHelper.read(_is);
        CORBAShareType arg1_in = CORBAShareTypeHelper.read(_is);
        int arg2_in = _is.read_long();
        CORBACustomer arg3_in = CORBACustomerHelper.read(_is);

        boolean _arg_result = buyShares(arg0_in, arg1_in, arg2_in, arg3_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_sellShares(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String[] arg0_in = tickerListHelper.read(_is);
        CORBAShareType arg1_in = CORBAShareTypeHelper.read(_is);
        int arg2_in = _is.read_long();
        CORBACustomer arg3_in = CORBACustomerHelper.read(_is);

        boolean _arg_result = sellShares(arg0_in, arg1_in, arg2_in, arg3_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getTickerListing(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        String[] _arg_result = getTickerListing();

        _output = handler.createReply();
        tickerListHelper.write(_output,_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getBusinessTicker(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        String _arg_result = getBusinessTicker(arg0_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_sellShareList(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        CORBAShareItem[] arg0_in = shareItemListHelper.read(_is);
        CORBACustomer arg1_in = CORBACustomerHelper.read(_is);

        boolean _arg_result = sellShareList(arg0_in, arg1_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

}
