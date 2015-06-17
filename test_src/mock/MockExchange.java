package mock;

import business.Business;
import business.BusinessInterface;
import stockexchange.exchange.Exchange;
import stockexchange.exchange.ShareSalesStatusList;

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
        google = getBusiness("google");
        yahoo = getBusiness("yahoo");
        microsoft = getBusiness("microsoft");

        createBusinessDirectory();
        shareStatusSaleList = new ShareSalesStatusList();

        initializeShares();
    }

    /**
     * @param businessName looking for
     * @return
     */
    @Override
    public BusinessInterface getBusiness(String businessName) {
        return new Business("google_data.csv");
    }

}
