
package business;

import javax.jws.WebMethod;
import javax.jws.WebService;

import common.share.ShareOrder;

@WebService
public interface IBusiness {

	@WebMethod
	public String getTicker();

	@WebMethod
	public boolean issueShares(ShareOrder aSO);

	@WebMethod
	public boolean recievePayment(String orderNum, float totalPrice) ;
}
