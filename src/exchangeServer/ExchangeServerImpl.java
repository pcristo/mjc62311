package exchangeServer;

import org.omg.CORBA.ORB;

/**
 * Created by Sai on 2015/6/18.
 */
public class ExchangeServerImpl extends ExchangeServerIFPOA {

    private final String m_serverName;
    private ORB m_orb;

    public ExchangeServerImpl(String name)
    {
        m_serverName = name;
        /**
         * TODO:
         * initialize everything
         */
    }

    public void setORB(ORB orb_val) {
        m_orb = orb_val;
    }
    @Override
    public BusinessInfo getBusiness(String businessName) {
        //TODO:implement real logic, remove dummy test code after
        BusinessInfo info = new BusinessInfo();
        info.businessSymbol = "test";
        info.shareType = CORBAShareType.COMMON;
        info.unitPrice = 111;
        return info;
    }

    @Override
    public boolean updateSharePrice(String businessSymbol, float unitPrice) {
        //TODO:implement real logic
        return false;
    }

    @Override
    public boolean registerBusiness(String businessSymbol, float unitPrice) {
        return false;
    }

    @Override
    public boolean unregisterBusiness(String businessSymbol) {
        return false;
    }
}
