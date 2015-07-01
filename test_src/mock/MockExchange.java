package mock;

import business.BusinessServant;
import stockexchange.exchange.Exchange;
import stockexchange.exchange.ShareItem;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * When testing we don't have the servers running
 * So we use a mockExchange which extends out the
 * server part
 */
public class MockExchange extends Exchange {

    protected Map<String, BusinessServant> businessDirectory = new HashMap<String, BusinessServant>();


    /**
     * Create Exchange object, initializes three businesses
     *
     * @throws RemoteException
     * @throws NotBoundException
     */
    public MockExchange() throws RemoteException, NotBoundException {
        super();
        // Exchange is created...but there will be no business server registry businesses ... OH NO
        // SO we'll register them locally here
        registerBusiness("GOOG", 10000);
    }

    @Override
    public boolean registerBusiness(String symbol, float price) {
        try {
            // Only line changed
            businessDirectory.put(symbol, new BusinessServant(symbol));
            // Rest is same as super
            priceDirectory.put(symbol, price);
            System.out.println(businessDirectory);
            return this.businessDirectory.get(symbol).issueShares(generateOrderNumber(), "br01", symbol, 0, price, 1000, price);

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
        return false;
    }


    @Override
    protected ShareItem issueSharesRequest(ShareItem sItem) {
        Boolean sharesIssued = false;

        BusinessServant bi = businessDirectory.get(sItem.getBusinessSymbol());
        if (bi == null) return null;

        String orderNum = generateOrderNumber();

		/*synchronized (orderNum) {
			try {
				sharesIssued = bi.issueShares(new ShareOrder(orderNum,
						"not applicable", sItem.getBusinessSymbol(), sItem.getShareType(),
						sItem.getUnitPrice(), RESTOCK_THRESHOLD, sItem.getUnitPrice()));
			} catch (Exception e) {
				System.out.println(" \n " + e.getMessage());
			}
		}

		if (sharesIssued) {
			ShareItem newShareItem = new ShareItem(orderNum,sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD);
			return newShareItem;
		}*/

        return null;
    }

}
