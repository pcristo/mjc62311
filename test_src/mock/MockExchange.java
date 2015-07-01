package mock;

import stockexchange.exchange.Exchange;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * When testing we don't have the servers running
 * So we use a mockExchange which extends out the
 * server part
 */
public class MockExchange extends Exchange  {


    /**
     * Create Exchange object, initializes three businesses
     *
     * @throws RemoteException
     * @throws NotBoundException
     */
    public MockExchange() throws RemoteException, NotBoundException {
        super();
    }


}
