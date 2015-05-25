package stockexchange;

import business.ShareOrder;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Define operations accessible for the client in the stock exchange server
 * Created by sai sun on 5/25/2015.
 */
public interface BrokerInterface extends Remote {

    /**
     * For each share in the shareList, check the records. Sell the shares that are available in
     * the records to the Customer with a commission markup
     * @param shareItemList share list to sell
     * @param info client info
     * @return share sale status list
     * @throws RemoteException
     */
    ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) throws RemoteException;
}
