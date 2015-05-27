package business;

import share.Share;
import share.ShareOrder;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BusinessInterface extends Remote {

    public boolean issueShares(ShareOrder aSO) throws RemoteException;

    public Share getShareInfo(String aShareType) throws RemoteException;

    public List<Share> getSharesList() throws RemoteException;

    public boolean recievePayment(String orderNum, float totalPrice) throws RemoteException;




}
