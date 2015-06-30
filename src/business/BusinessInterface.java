package business;

import common.share.Share;
import common.share.ShareOrder;
import common.share.ShareType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BusinessInterface extends Remote {

    boolean issueShares(ShareOrder aSO) throws RemoteException;

    Share getShareInfo(ShareType aShareType) throws RemoteException;

    List<Share> getSharesList() throws RemoteException;

    boolean recievePayment(String orderNum, float totalPrice) throws RemoteException;

    String getTicker() throws RemoteException;




}
