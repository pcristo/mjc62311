package business;

/**
 * Define functions accessible by clients
 * Created by Sai on 2015/5/23.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

import business.Share;
import business.ShareOrder;

public interface BusinessServerInterface extends Remote {
    /**
     * This operation is called by the StockExchange service. If the unit price is higher or
     * equal to the defined price in the data file, will produce extra shares;
     * otherwise, return false.
     *
     * @param aSP share order info
     * @return true if share order is issued, otherwise false
     * @throws RemoteException
     */
    boolean issueShares(ShareOrder aSP) throws RemoteException;

    /**
     * If the share type is from ¡°common¡±, ¡°preferred¡±, or ¡°convertible¡±, return the corresponding
     * share information as defined in the data file; otherwise return null.
     *
     * @param aShareType share type
     * @return corresponding
     * share information or null
     * @throws RemoteException
     */
    Share getShareInfo(String aShareType) throws RemoteException;

    /**
     * This operation is called by the StockExchange service. The orderNum and the totalPrice
     should match a recorded order in the data file. If you find a match, you mark the matching
     order as ¡°paid¡± in the data file and return true. Otherwise, return false.
     * @param orderNum share order number
     * @param totalPrice total price of the share order
     * @return true if order is paid
     * @throws RemoteException
     */
    boolean receivePayment(String orderNum, float totalPrice) throws RemoteException;
}
