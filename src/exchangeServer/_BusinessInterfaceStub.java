package exchangeServer;

import exchangeServer.BusinessInterfacePackage.ShareListHelper;

/**
 * Interface definition: BusinessInterface.
 * 
 * @author OpenORB Compiler
 */
public class _BusinessInterfaceStub extends org.omg.CORBA.portable.ObjectImpl
        implements BusinessInterface
{
    static final String[] _ids_list =
    {
        "IDL:exchangeServer/BusinessInterface:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = BusinessInterfaceOperations.class;

    /**
     * Operation issueShares
     */
    public boolean issueShares(CORBAShareOrder aSO)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("issueShares",true);
                    CORBAShareOrderHelper.write(_output,aSO);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("issueShares",_opsClass);
                if (_so == null)
                   continue;
                BusinessInterfaceOperations _self = (BusinessInterfaceOperations) _so.servant;
                try
                {
                    return _self.issueShares( aSO);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getShareInfo
     */
    public BusinessInfo getShareInfo(CORBAShareType aShareType)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getShareInfo",true);
                    CORBAShareTypeHelper.write(_output,aShareType);
                    _input = this._invoke(_output);
                    BusinessInfo _arg_ret = BusinessInfoHelper.read(_input);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getShareInfo",_opsClass);
                if (_so == null)
                   continue;
                BusinessInterfaceOperations _self = (BusinessInterfaceOperations) _so.servant;
                try
                {
                    return _self.getShareInfo( aShareType);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getSharesList
     */
    public BusinessInfo[] getSharesList()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getSharesList",true);
                    _input = this._invoke(_output);
                    BusinessInfo[] _arg_ret = ShareListHelper.read(_input);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getSharesList",_opsClass);
                if (_so == null)
                   continue;
                BusinessInterfaceOperations _self = (BusinessInterfaceOperations) _so.servant;
                try
                {
                    return _self.getSharesList();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation recievePayment
     */
    public boolean recievePayment(String orderNum, float totalPrice)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("recievePayment",true);
                    _output.write_string(orderNum);
                    _output.write_float(totalPrice);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("recievePayment",_opsClass);
                if (_so == null)
                   continue;
                BusinessInterfaceOperations _self = (BusinessInterfaceOperations) _so.servant;
                try
                {
                    return _self.recievePayment( orderNum,  totalPrice);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getTicker
     */
    public String getTicker()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getTicker",true);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getTicker",_opsClass);
                if (_so == null)
                   continue;
                BusinessInterfaceOperations _self = (BusinessInterfaceOperations) _so.servant;
                try
                {
                    return _self.getTicker();
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
