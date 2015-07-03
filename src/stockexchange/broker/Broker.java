package stockexchange.broker;

import common.Customer;
import common.share.ShareType;
import stockexchange.exchange.Exchange;
import stockexchange.exchange.ShareItem;
import stockexchange.exchange.ShareList;
import stockexchange.exchange.ShareSalesStatusList;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Broker class takes customer request and validates it and sends it over
 * to stock exchange
 */
public class Broker implements Serializable{

    // Require for serialization.  Ensures object deserialized and serialized are the same.
    private static final long serialVersionUID = 1467890432560789065L;

    protected static Exchange exchange;


    /**
     * Create broker class, point him to the exchange he trades on
     */
    public Broker() {
    	exchange = getExchange();
    }


    /**
     * This is a separate method than constructor to enable
     * sub classes to override (see MockBroker)
     * @return Exchange object
    */
    protected Exchange getExchange() {
        return Exchange.exchange;
    }

    /**
     * @return list of company tickers on the stock exchange
     *
     */
    public ArrayList<String> getTickerListing() {
        return exchange.getListing();
    }

    public String getBusinessTicker(String businessName) {
        return exchange.getBusinessTicker(businessName);
    }


    /**
     * Sell Shares
     *
     * @param tickers  arraylist that need to be sold
     * @param type     type that the tickers belong to
     * @param quantity that wants to be sold
     * @param customer customer who made the request
     * @return
     */
    public boolean sellShares(ArrayList<String> tickers, ShareType type, int quantity, Customer customer) {

        ShareList sharesToSell = prepareTrade(tickers, type, quantity);

        if (sharesToSell != null) {
            ShareSalesStatusList shareSatusList = exchange.sellShares(sharesToSell, customer);
            if(shareSatusList.getShares(customer) == null || shareSatusList.getShares(customer).isEmpty()){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * Prepare the trade to go to the exchange
     *
     * @param tickers  involved in transaction
     * @param type     of stocks being traded
     * @param quantity amount of stocks
     * @return a shareList used by exchange or null if validation fail
     */
    private ShareList prepareTrade(ArrayList<String> tickers, ShareType type, int quantity) {
        // Prepare shares to action - honestly this should be done a common.share at a time
        ArrayList<ShareItem> sharesToAction = new ArrayList<ShareItem>();
        for (String ticker : tickers) {
            if (!validateTicker(ticker)) {
                // We don't trade anything unless all tickers are valid
                return null;
            } else {
                float price = 50;
                String orderNumber = "";
                sharesToAction.add(new ShareItem(orderNumber, ticker, type, price, quantity));
            }
        }

        // Another list...why not just arrayList?
        ShareList sharesToSellObj = new ShareList(sharesToAction);
        return sharesToSellObj;
    }


    /**
     * Ensure the ticker is a valid ticker listed on the exchange
     *
     * @param ticker
     * @return
     */
    private boolean validateTicker(String ticker) {
    	 ArrayList<String> tickerListing = exchange.getListing();
         return tickerListing.contains(ticker);
    }


}