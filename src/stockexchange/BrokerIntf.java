package stockexchange;


import java.util.ArrayList;

/**
 * Broker class relays messages from the client to the stock exchange
 * Performas checks and takes a commission
 */
public interface BrokerIntf {

    public void buyShares(String ticker, String type, int quantity);

    public void sellShares(String ticker, String type, int quantity);

    public ArrayList<ShareItem> getShares();



}
