
package business;

import common.share.ShareOrder;


public interface IBusiness {


	public String getTicker();

	public boolean issueShares(ShareOrder aSO);

	public boolean recievePayment(String orderNum, float totalPrice) ;

}
