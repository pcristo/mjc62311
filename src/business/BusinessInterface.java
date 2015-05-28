package business;

import share.Share;
import share.ShareOrder;
import share.ShareType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BusinessInterface extends Remote {

    public boolean issueShares(ShareOrder aSO) throws RemoteException;

    public Share getShareInfo(ShareType aShareType) throws RemoteException;

    public List<Share> getSharesList() throws RemoteException;

    public boolean recievePayment(String orderNum, float totalPrice) throws RemoteException;

    /**
     *
     * @return the ticker commonly used to identify a company
     * @throws RemoteException
     */
    public String getTicker() throws RemoteException;




}
