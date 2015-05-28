package stockexchange.broker;

import client.Customer;
import share.ShareOrder;
import share.ShareType;
import stockexchange.ShareItem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Define operations accessible for the client in the stock exchange server
 * Created by sai sun on 5/25/2015.
 */
public interface BrokerInterface extends Remote {


    public boolean buyShares(ArrayList<String> ticker, ShareType type, int quantity, Customer customer) throws RemoteException;

    public boolean sellShares(ArrayList<String> ticker, ShareType type, int quantity, Customer customer) throws RemoteException;

    public ArrayList<String> getTickerListing() throws RemoteException;

}
