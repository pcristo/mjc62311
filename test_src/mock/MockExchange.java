package mock;

import business.BusinessServant;
import common.Customer;
import common.logger.LoggerClient;
import common.share.ShareOrder;
import common.share.ShareType;
import stockexchange.exchange.Exchange;
import stockexchange.exchange.ShareItem;
import stockexchange.exchange.ShareList;
import stockexchange.exchange.ShareSalesStatusList;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        boolean test = registerBusiness("GOOG", 10000);
    }

    @Override
    public boolean registerBusiness(String symbol, float price) {
        try {
            // Only line changed
            businessDirectory.put(symbol, new BusinessServant(symbol));
            // Rest is same as super
            priceDirectory.put(symbol, price);
            System.out.println(businessDirectory);

            return this.orderShares(this.businessDirectory.get(symbol), symbol, price, 1000);

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

		synchronized (orderNum) {
			try {
				sharesIssued = bi.issueShares(orderNum,
						"not applicable", sItem.getBusinessSymbol(), 0,
						sItem.getUnitPrice(), 1000, sItem.getUnitPrice());
			} catch (Exception e) {
				System.out.println(" \n " + e.getMessage());
			}
		}

		if (sharesIssued) {
			ShareItem newShareItem = new ShareItem(orderNum,sItem.getBusinessSymbol(), sItem.getShareType(), sItem.getUnitPrice(), 1000);
			return newShareItem;
		}

        return null;
    }

    @Override
    public ShareSalesStatusList sellShares(ShareList shareItemList, Customer info) {

        ShareItem soldShare = null;

        for  (ShareItem s : shareItemList.getLstShareItems())
        {
            int requestedShares = s.getQuantity();
            int toComplete = requestedShares;

            //Is business registered in Exchange
            if (this.priceDirectory.get(s.getBusinessSymbol()) != null){

                //Business Shares Listing
                List<ShareItem> lstShares = shareStatusSaleList.getNewAvShares().get(s.getBusinessSymbol());

                synchronized(lstShares)
                {
                    //Is quantity on hand
                    if (this.getShareQuantity(lstShares, s.getShareType()) >= s.getQuantity() ) {

                        for (ShareItem sItem : lstShares) {

                            //Populate new Sold Share
                            if (soldShare == null) {

                                soldShare = new ShareItem("",
                                        sItem.getBusinessSymbol(),
                                        sItem.getShareType(),
                                        sItem.getUnitPrice(),
                                        requestedShares);
                            }

                            //Just iterate through the companies share of a specific share type
                            if (sItem.getShareType() == s.getShareType()) {

                                if(toComplete == requestedShares && sItem.getQuantity() >= requestedShares) {

                                    //Reduce the available amount
                                    sItem.reduceQuantity(requestedShares);
                                    break;

                                } else {
                                    //Share will be coming from more then one order
                                    if (toComplete > 0) {

                                        if (sItem.getQuantity() >= toComplete) {
                                            sItem.reduceQuantity(toComplete);
                                            toComplete = 0;
                                        } else {
                                            toComplete -= sItem.getQuantity();
                                            sItem.reduceQuantity(sItem.getQuantity());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //Pay the companies once shares are at 0 in the newAvailable Shares
                    List<String> lstOrders = new ArrayList<String>();

                    for(ShareItem sItem : lstShares) {

                        if (sItem.getQuantity() == 0) {
                            lstOrders.add(sItem.getOrderNum());
                        }
                    }

                    //Pay all orders if needed
                    if (lstOrders.size()>0){
                        payBusiness(lstOrders);
                    }

                }

                //TODO: Review this synchronization
                synchronized (soldShare) {

                    shareStatusSaleList.addToSoldShares(s, info);
                }
            }

            //Restock Share Lists
            this.restock();
        }


        return  shareStatusSaleList;
    }

    @Override
    protected boolean payBusiness(List<String> lstOrders) {

        boolean paid = false;

        for(String orderNumber : lstOrders ){

            ShareItem shareToBePaid = shareStatusSaleList.getOrderesShares().get(orderNumber);

            synchronized (shareToBePaid) {

                // if the business is not registered, there is no interface, and null is returned
                //TODO To Review
                BusinessServant bi = businessDirectory.get(shareToBePaid.getBusinessSymbol());
                if (bi != null) {
                    try {
                        paid = bi.recievePayment(shareToBePaid.getOrderNum(),
                                shareToBePaid.getUnitPrice() * shareToBePaid.getQuantity());

                        if (paid) {

                            shareStatusSaleList.getOrderesShares().remove(orderNumber);
                        }

                    } catch (Exception e) {
                        System.out.println(" \n " + e.getMessage());
                    }
                }
            }
        }

        return paid;

    }

    protected boolean orderShares(BusinessServant iBusiness, String symbol, float price,  int quantity) {

        boolean ordered = false;

        try {

            String orderNumber = generateOrderNumber();

            ordered =  iBusiness.issueShares(orderNumber, "br01", symbol, 0, price, quantity, price);

            if (ordered) {

                ShareItem newShares = new ShareItem(orderNumber,symbol, ShareType.COMMON,price, quantity);

                shareStatusSaleList.addToNewAvShares(newShares);
                shareStatusSaleList.addToOrderedShares(newShares);

                LoggerClient.log("Sucessfully added " + quantity + " shares of " + iBusiness.getTicker());

            }


        } catch(Exception e) {
            LoggerClient.log(e.getMessage());
        }

        return  ordered;
    }


    public BusinessServant getMockBusiness(String businessName) {

        this.businessDirectory.get(businessName);

    }
}
