
package business;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import common.share.ShareOrder;


public interface IBusiness {


	public String getTicker();

	public boolean issueShares(ShareOrder aSO);

	public boolean recievePayment(String orderNum, float totalPrice) ;

}
