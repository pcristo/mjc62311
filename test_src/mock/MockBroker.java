package mock;


import stockexchange.broker.Broker;
import stockexchange.exchange.Exchange;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Since unit tests shouldn't use any servers
 * MockBroker is a stand in for broker so that
 * no RMI is used or created
 */
public class MockBroker extends Broker {



    public MockBroker() throws RemoteException, NotBoundException {
       exchange = getExchange();
    }

    @Override
    protected Exchange getExchange() throws RemoteException, NotBoundException {
        return new MockExchange();
    }

}
