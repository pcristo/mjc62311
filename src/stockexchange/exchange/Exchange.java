package stockexchange.exchange;

import business.WSClient.BusinessWSImplService;
import business.WSClient.IBusiness;
import business.WSClient.ShareOrder;
import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareType;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.util.*;

/**
 * The exchange class acts as an intermediary between businesses and stock brokers. Brokers
 * make requests to purchase stock from the exchange, which then either sells existing shares
 * to the broker or requests new shares be issued from the business.
 * Please note that the exchange assumes that all share types within a business have the same
 * ticker symbol and price.
 */

public class Exchange implements IExchange, Serializable {

	private static final long serialVersionUID = 1L;
    private static  final int RESTOCK_THRESHOLD = 500;
    private static  int orderInt = 1100;
    protected static ShareSalesStatusList shareStatusSaleList;
	public static Exchange exchange;
	private static Object issueSharesRequestLock = new Object();


    /**
     * Business directory that maps stock symbols to remote interfaces
     */
    protected Map<String, IBusiness> businessDirectory = new HashMap<String, IBusiness>();

    /**
     * Directory that maps stock symbols to stock prices
     */
    protected Map<String, Float> priceDirectory = new HashMap<String, Float>();

    /**
     * Create exchange object by preparing the local list of available shares
     */
    public Exchange() {
        // Creating a static reference to self
        // Singleton better suited here...
    	Exchange.exchange = this;
        try {
            shareStatusSaleList = new ShareSalesStatusList();
			//addShareToTest();
        }
        catch(Exception e) {
            LoggerClient.log("Exception in exchange: " + e.getMessage());
        }

    }

    /**
     * Returns basic business information in string format
     * @param businessName looking for
     * @return business object
     * @throws NotBoundException
     */
    public String getBusiness(String businessName) {

        String ticker = getBusinessTicker(businessName);

        if(ticker == null || ticker.isEmpty() || priceDirectory.get(businessName) == null) {
            return "";
        } else {
            return "Business Ticker : " + this.getBusinessTicker(businessName) + " Price : " + this.priceDirectory.get(businessName);
        }
    }


    /**
     * Registers a new business with the exchange, providing an initial price.
     * @param symbol
     * @param price
     * @return true if successful, false if the exchange was unable to buy initial shares
     */
    public boolean registerBusiness(String symbol, float price, String port) {
        try{
            synchronized(businessDirectory) {
            	businessDirectory.put(symbol, getBusinessIFace(symbol, port)); }

            synchronized(priceDirectory) {
            	priceDirectory.put(symbol, price); }

			System.out.println("Registered " + symbol + " with price " + price);
            //LoggerClient.log("Registered " + symbol + " with price " + price);
            return this.orderShares(this.businessDirectory.get(symbol), symbol, price, 1000);
        } catch (Exception e) {
            LoggerClient.log("Exchange exception in registerBusiness: " + e.getMessage());
        }
	return false;
    }

    /**
     * Delists a business from the exchange
     * @param symbol to delist
     * @throws Exception when the symbol is not listed
     */
    public boolean unregister(String symbol) {
    	// try to remove the stock from the business and price registers. If the symbol
    	// is not found, return false
    	IBusiness bi;
    	synchronized(businessDirectory) {
    		bi = businessDirectory.remove(symbol);
    	}

    	if ((bi == null) || (priceDirectory.remove(symbol) == null)) {
    		return false;
    	}

    	return true;
    }

    /**
     *  Returns a business interface for making calls to the remote business server.
     * @param businessName
     * @return
     */
    public IBusiness getBusinessIFace(String businessName, String port){
        try {
			BusinessWSImplService businessService = new BusinessWSImplService(businessName, port);
			IBusiness iBusiness = businessService.getBusinessWSImplPort();

            return iBusiness;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Sell Shares to a Customer (ie Customer is BUYING shares)
     * @param shareItemList ShareList of shares to transact
     * @param info Customer object making the transaction
     * @return ShareSalesStatusList - Can access sold shares and available shares lists
     */
    public ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) {

        ShareItem soldShare = null;


        for  (ShareItem s : shareItemList.getLstShareItems())
        {

			int requestedShares = s.getQuantity();
			int toComplete = requestedShares;

			// Is business registered in Exchange
			//if (this.priceDirectory.get(s.getBusinessSymbol()) != null) {

				// Business Shares Listing
				List<ShareItem> lstShares = shareStatusSaleList.newAvShares.get(s.getBusinessSymbol());

				// Is quantity on hand
				if (this.getShareQuantity(lstShares, s.getShareType()) >= s.getQuantity()) {

					for (ShareItem sItem : lstShares) {

						// Populate new Sold Share
						if (soldShare == null) {

							soldShare = new ShareItem("",
									sItem.getBusinessSymbol(),
									sItem.getShareType(), sItem.getUnitPrice(),
									requestedShares);
						}

						// Just iterate through the companies share of a
						// specific share type
						if (sItem.getShareType() == s.getShareType()) {

							if (toComplete == requestedShares
									&& sItem.getQuantity() >= requestedShares) {

								// Reduce the available amount
								sItem.reduceQuantity(requestedShares);
								break;

							} else {
								// Share will be coming from more then one order
								if (toComplete > 0) {

									if (sItem.getQuantity() >= toComplete) {
										sItem.reduceQuantity(toComplete);
										toComplete = 0;
									} else {
										toComplete -= sItem.getQuantity();
										sItem.reduceQuantity(sItem
												.getQuantity());
									}
								}
							}
						}
					}
				//}

				// Pay the companies once shares are at 0 in the newAvailable
				// Shares
				List<String> lstOrders = new ArrayList<String>();

				if (lstShares != null) {
					synchronized (lstShares) {
						for (ShareItem sItem : lstShares) {
							if (sItem.getQuantity() == 0) {
								lstOrders.add(sItem.getOrderNum());
							}
						}

					// Pay all orders if needed
					if (lstOrders.size() > 0) {
							payBusiness(lstOrders);
						}
					}
				}

				synchronized (shareStatusSaleList) {
					shareStatusSaleList.addToSoldShares(s, info);
				}
			}

			// Restock Share Lists
			this.restock();
        }

        return  Exchange.shareStatusSaleList;
    }

    /**
     * Given a customer, determine all of that customers' stocks
     * @param customer wanting stock information
     * @return list of customers' stocks
     */
    public List<ShareItem> getShares(Customer customer) {
        return shareStatusSaleList.getShares(customer);
    }

    /**
     * @return arrayList of all tickers listed on exchange
     */
    public ArrayList<String> getListing() {

        ArrayList<String> tickerList = new ArrayList<String>();

		synchronized (businessDirectory) {
			for (String ticker : businessDirectory.keySet()) {
				tickerList.add(ticker);
			}
		}

        return tickerList;
    }

    /**
     * Get the ticker for the business
     * @param businessName
     * @return the ticker commonly used to identify a company | Null if not found
     */
    public String getBusinessTicker(String businessName) {
        //Is business registerd
        IBusiness iBusiness = this.businessDirectory.get(businessName);

        if (iBusiness != null) {
            return iBusiness.getTicker();
        }
        return null;
    }


    /**
     *Method to restock any available common.share that is below the threshold
     */
    protected void restock() {
    	// we must lock the shareStatusSaleList, because if any entry is changed while
    	// we are inside the foreach loop, a concurrency exception is thrown.
		synchronized (shareStatusSaleList) {

			for (Map.Entry<String, List<ShareItem>> entry : shareStatusSaleList.newAvShares
					.entrySet()) {
				List<ShareItem> addToList = new ArrayList<ShareItem>();
				for (ShareItem sItem : entry.getValue()) {
					if (sItem.getQuantity() < RESTOCK_THRESHOLD) {
						ShareItem newShares = this.issueSharesRequest(sItem);

						if (newShares != null && sItem.getQuantity() == 0) {
							sItem.setOrderNum(newShares.getOrderNum());
							sItem.increaseQuantity(newShares.getQuantity());

							shareStatusSaleList
									.addToOrderedShares(new ShareItem(sItem
											.getOrderNum(), sItem
											.getBusinessSymbol(), sItem
											.getShareType(), sItem
											.getUnitPrice(), sItem
											.getQuantity()));
						} else {
							addToList.add(newShares);
							shareStatusSaleList
									.addToOrderedShares(new ShareItem(newShares
											.getOrderNum(), newShares
											.getBusinessSymbol(), newShares
											.getShareType(), newShares
											.getUnitPrice(), newShares
											.getQuantity()));
						}
					}
				}
				entry.getValue().addAll(addToList);
			}

		}
	}

    /**
     * Pays a business for shares that were previously issued but not paid for.
     * @param lstOrders List of order numbers that have been depleted
     * @return true if payment is processed
     */
    protected boolean payBusiness(List<String> lstOrders) {
    	boolean paid = false;
    	for(String orderNumber : lstOrders ){

    		// between the moment we start checking if an order is paid, and the moment we actually
    		// pay it, we must lock out access to the shareStatusSaleList to avoid another thread
    		// trying to pay the same entry.
    		synchronized(shareStatusSaleList) {

    			ShareItem shareToBePaid = shareStatusSaleList.orderedShares.get(orderNumber);
    			// if the business is not registered, there is no interface, and null is returned
    			IBusiness bi = businessDirectory.get(shareToBePaid.getBusinessSymbol());
    			if (bi != null) {
    				try {
    					paid = bi.recievePayment(shareToBePaid.getOrderNum(),
    							shareToBePaid.getUnitPrice() * shareToBePaid.getQuantity());

    					if (paid) {
    						shareStatusSaleList.orderedShares.remove(orderNumber);

    					}

    				} catch (Exception e) {
    					LoggerClient.log("Exchange Exception in payBusiness: " + e.getMessage());
    				}
    			}
    		}
    	}
    	return paid;
    }

    /**
     * Request a business to issue shares
     * @param sItem ShareItem to be issued
     * @return ShareItem (null will be returned if the transaction fails)
     */
	protected ShareItem issueSharesRequest(ShareItem sItem) {
		Boolean sharesIssued = false;

		IBusiness bi = businessDirectory.get(sItem.getBusinessSymbol());
		if (bi == null) return null;

        String orderNum = generateOrderNumber();

        /* Note: Do not synchronize on local variables (like orderNum). Different threads will 
         * have different instances of those objects, so the lock would be irrelevant. The best 
         * way is to just use a static class-level variable. patrickc 2015/07/26
         */
		synchronized (issueSharesRequestLock) {
			try {

				ShareOrder orderShare = new ShareOrder();
				orderShare.setOrderNum(orderNum);
				orderShare.setBrokerRef("br01");
				orderShare.setBusinessSymbol(sItem.getBusinessSymbol());
				orderShare.setShareType(business.WSClient.ShareType.COMMON);
				orderShare.setUnitPrice(sItem.getUnitPrice());
				orderShare.setQuantity(RESTOCK_THRESHOLD);
				orderShare.setUnitPriceOrder(sItem.getUnitPrice());

				sharesIssued = bi.issueShares(orderShare);

			} catch (Exception e) {
				System.out.println(" \n " + e.getMessage());
			}
		}

		if (sharesIssued) {
			ShareItem newShareItem = new ShareItem(orderNum,sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), RESTOCK_THRESHOLD);
			return newShareItem;
		}

		return null;
	}


    /**
     * Method to generate unique sequential order number for issue common.share
     */
    protected synchronized String generateOrderNumber() {
        orderInt = orderInt + 1;
        String orderNumber = Integer.toString(orderInt);
        return orderNumber;
    }

    /**
     * Retrieve the total quantity of share and share type
     * @param lstShareItem
     * @param sType
     * @return
     */
    protected int getShareQuantity(List<ShareItem> lstShareItem, ShareType sType) {
    	// sent an empty list? then there are no shares!
    	if (lstShareItem == null)
    		return 0;

    	int totQuantity = 0;

        //Retrieve Business Shares in Available list
        for (ShareItem sItem : lstShareItem){

            if (sItem.getShareType() == sType){
                totQuantity = totQuantity + sItem.getQuantity();
            }

        }

        return totQuantity;

    }

     /**
     *  updateSharePrice(SYMBOL, PRICE) updates existing business's share price. If business
     *  does not exist, it's an error.
     * @param symbol
     * @param price
     * @return
     */
    public boolean updateSharePrice(String symbol, float price) {
        //Does business exist in exchange
        if (this.priceDirectory.get(symbol) == null) {
            return false;
        } else {

            //Update Registry price
            synchronized (priceDirectory){
                this.priceDirectory.put(symbol, price);
            }
        }

        return true;

    }

    /**
     * Order shares from business
     * @param iBusiness
     * @param symbol
     * @param price
     * @param quantity
     * @return true if order was successful or false if not
     */
    protected boolean orderShares(IBusiness iBusiness, String symbol, float price,  int quantity) {

        boolean ordered = false;

        try {

            String orderNumber = generateOrderNumber();

			ShareOrder orderShare = new ShareOrder();
			orderShare.setOrderNum(orderNumber);
			orderShare.setBrokerRef("br01");
			orderShare.setBusinessSymbol(symbol);
			orderShare.setShareType(business.WSClient.ShareType.COMMON);
			orderShare.setUnitPrice(price);
			orderShare.setQuantity(quantity);
			orderShare.setUnitPriceOrder(price);

            ordered =  iBusiness.issueShares(orderShare);

            if (ordered) {
                ShareItem newShares = new ShareItem(orderNumber,symbol,ShareType.COMMON,price, quantity);

                synchronized(shareStatusSaleList) {
                	shareStatusSaleList.addToNewAvShares(newShares);
                    shareStatusSaleList.addToOrderedShares(newShares);
                }

                LoggerClient.log("Successfully added " + quantity + " shares of " + iBusiness.getTicker());
            }
        } catch(Exception e) {
            LoggerClient.log(e.getMessage());
        }

        return  ordered;
    }

    public void printCurrentShareInfo() {

    	//Print all Ordered Shares

    	System.out.println("Ordered Shares Listing");
    	System.out.println("----------------------");

    	synchronized(shareStatusSaleList) {
    		for(Map.Entry<String, ShareItem> entry : shareStatusSaleList.orderedShares.entrySet()) {

    			System.out.println(entry.getKey());
    			System.out.println(entry.getValue().printShareInfo());
    		}
    	}

    	//Print All Available Shares
    	System.out.println("Available Shares Listing");
    	System.out.println("----------------------");
    	synchronized(shareStatusSaleList) {
    		for(Map.Entry<String, List<ShareItem>> entry : shareStatusSaleList.newAvShares.entrySet()) {

    			System.out.println(entry.getKey());

    			for(ShareItem sItem : entry.getValue()) {
    				System.out.println(sItem.printShareInfo());
    			}
    		}
    	}

    	//Print All Available Shares
    	LoggerClient.log("Sold Shares Listing");
    	LoggerClient.log("----------------------");
    	synchronized(shareStatusSaleList) {
    		for(Map.Entry<Integer, List<ShareItem>> entry : shareStatusSaleList.getSoldShares().entrySet()) {

    			System.out.println("Customer ID : " + entry.getKey());

    			for(ShareItem sItem : entry.getValue()) {
    				System.out.println(sItem.printShareInfo());
    			}
    		}
    	}
    }

	/**
	 * Used to connect to sellShares to service
	 * @param share
	 * @param info
	 * @return
	 */
	public boolean sellShareService(ShareItem share, Customer info){

		ArrayList<ShareItem> lstCustShares = new ArrayList<ShareItem>();

		/*Customer newCust = new Customer(info.getName(),info.getStreet1(),info.getStreet2(),info.getCity(),
				info.getProvince(),info.getPostalCode(),info.getCountry());*/

		lstCustShares.add(share);

		ShareSalesStatusList shareList = sellShares(new ShareList(lstCustShares), info);

		if(shareList == null || shareList.getShares(info) == null ){
			return false;
		}

		int size =  shareList.getShares(info).size();

		// Customer has to have shares now
		if(size <= 0 ){
			return false;
		} else {
			return true;
		}
	}
}
