package exchangeServer;

/**
 * Interface definition: ExchangeServerIF.
 * 
 * @author OpenORB Compiler
 */
public class _ExchangeServerIFStub extends org.omg.CORBA.portable.ObjectImpl
        implements ExchangeServerIF
{
    static final String[] _ids_list =
    {
        "IDL:exchangeServer/ExchangeServerIF:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = ExchangeServerIFOperations.class;

    /**
     * Operation getBusiness
     */
    public BusinessInfo getBusiness(String businessName)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getBusiness",true);
                    _output.write_string(businessName);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getBusiness",_opsClass);
                if (_so == null)
                   continue;
                ExchangeServerIFOperations _self = (ExchangeServerIFOperations) _so.servant;
                try
                {
                    return _self.getBusiness( businessName);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation updateSharePrice
     */
    public boolean updateSharePrice(String businessSymbol, float unitPrice)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("updateSharePrice",true);
                    _output.write_string(businessSymbol);
                    _output.write_float(unitPrice);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("updateSharePrice",_opsClass);
                if (_so == null)
                   continue;
                ExchangeServerIFOperations _self = (ExchangeServerIFOperations) _so.servant;
                try
                {
                    return _self.updateSharePrice( businessSymbol,  unitPrice);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation registerBusiness
     */
    public boolean registerBusiness(String businessSymbol, float unitPrice)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("registerBusiness",true);
                    _output.write_string(businessSymbol);
                    _output.write_float(unitPrice);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("registerBusiness",_opsClass);
                if (_so == null)
                   continue;
                ExchangeServerIFOperations _self = (ExchangeServerIFOperations) _so.servant;
                try
                {
                    return _self.registerBusiness( businessSymbol,  unitPrice);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation unregisterBusiness
     */
    public boolean unregisterBusiness(String businessSymbol)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("unregisterBusiness",true);
                    _output.write_string(businessSymbol);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("unregisterBusiness",_opsClass);
                if (_so == null)
                   continue;
                ExchangeServerIFOperations _self = (ExchangeServerIFOperations) _so.servant;
                try
                {
                    return _self.unregisterBusiness( businessSymbol);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
