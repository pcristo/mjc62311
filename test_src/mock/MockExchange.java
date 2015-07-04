package mock;

import business.BusinessServant;
import corba.business_domain.interface_business;
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

    @Override
    public interface_business getBusinessIFace(String businessName){
        interface_business bi = (interface_business) new BusinessServant(businessName);
        return bi;
    }


}
