package mock;

import business.Business;
import business.WSClient.IBusiness;
import business.WSClient.ShareOrder;

import javax.jws.WebParam;

public class MockBusiness extends Business implements IBusiness{

    @Override
    public boolean issueShares(@WebParam(name = "arg0", targetNamespace = "") ShareOrder arg0) {
        return false;
    }

    public MockBusiness(String identifier) {
        super(identifier);
    }
}
