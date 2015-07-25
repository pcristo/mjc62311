package mock;

import business.WSClient.IBusiness;
import stockexchange.exchange.Exchange;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * When testing we don't have the servers running
 * So we use a mockExchange which extends out the
 * server part
 */
public class MockExchange extends Exchange {



    /**
     * Create Exchange object, initializes three businesses
     *
     * @throws RemoteException
     * @throws NotBoundException
     */
    public MockExchange() {
        // Exchange is created...but there will be no business server registry businesses ... OH NO
        // SO we'll register them locally here
        registerBusiness("GOOG", 10000);
        registerBusiness("AAPL", 10000);
        registerBusiness("YHOO", 10000);
        registerBusiness("MSFT", 10000);

    }

   /* @Override
    public IBusiness getBusinessIFace(String businessName){
        IBusiness bi = (interface_business) new BusinessServant(businessName);
        return bi;
    }*/


}
