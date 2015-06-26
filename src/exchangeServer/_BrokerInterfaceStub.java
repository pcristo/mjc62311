package exchangeServer;

import exchangeServer.BrokerInterfacePackage.shareItemListHelper;
import exchangeServer.BrokerInterfacePackage.tickerListHelper;

/**
 * Interface definition: BrokerInterface.
 * 
 * @author OpenORB Compiler
 */
public class _BrokerInterfaceStub extends org.omg.CORBA.portable.ObjectImpl
        implements BrokerInterface
{
    static final String[] _ids_list =
    {
        "IDL:exchangeServer/BrokerInterface:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = BrokerInterfaceOperations.class;

    /**
     * Operation buyShares
     */
    public boolean buyShares(String[] list, CORBAShareType type, int quantity, CORBACustomer customer)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("buyShares",true);
                    tickerListHelper.write(_output,list);
                    CORBAShareTypeHelper.write(_output,type);
                    _output.write_long(quantity);
                    CORBACustomerHelper.write(_output,customer);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("buyShares",_opsClass);
                if (_so == null)
                   continue;
                BrokerInterfaceOperations _self = (BrokerInterfaceOperations) _so.servant;
                try
                {
                    return _self.buyShares( list,  type,  quantity,  customer);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation sellShares
     */
    public boolean sellShares(String[] list, CORBAShareType type, int quantity, CORBACustomer customer)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("sellShares",true);
                    tickerListHelper.write(_output,list);
                    CORBAShareTypeHelper.write(_output,type);
                    _output.write_long(quantity);
                    CORBACustomerHelper.write(_output,customer);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sellShares",_opsClass);
                if (_so == null)
                   continue;
                BrokerInterfaceOperations _self = (BrokerInterfaceOperations) _so.servant;
                try
                {
                    return _self.sellShares( list,  type,  quantity,  customer);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getTickerListing
     */
    public String[] getTickerListing()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getTickerListing",true);
                    _input = this._invoke(_output);
                    String[] _arg_ret = tickerListHelper.read(_input);
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getTickerListing",_opsClass);
                if (_so == null)
                   continue;
                BrokerInterfaceOperations _self = (BrokerInterfaceOperations) _so.servant;
                try
                {
                    return _self.getTickerListing();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getBusinessTicker
     */
    public String getBusinessTicker(String businessName)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getBusinessTicker",true);
                    _output.write_string(businessName);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getBusinessTicker",_opsClass);
                if (_so == null)
                   continue;
                BrokerInterfaceOperations _self = (BrokerInterfaceOperations) _so.servant;
                try
                {
                    return _self.getBusinessTicker( businessName);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation sellShareList
     */
    public boolean sellShareList(CORBAShareItem[] lstShares, CORBACustomer customer)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("sellShareList",true);
                    shareItemListHelper.write(_output,lstShares);
                    CORBACustomerHelper.write(_output,customer);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sellShareList",_opsClass);
                if (_so == null)
                   continue;
                BrokerInterfaceOperations _self = (BrokerInterfaceOperations) _so.servant;
                try
                {
                    return _self.sellShareList( lstShares,  customer);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
