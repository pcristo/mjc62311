package business;

import corba.business_domain.interface_business;
import corba.business_domain.interface_businessPOA;
import common.share.ShareOrder;
import common.share.ShareType;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;

/**
 * This class provides CORBA servant implementation for the business class.
 * 
 * @author patrick
 */
public class BusinessServant extends interface_businessPOA implements interface_business {


	private Business business;

	/**
	 * Constructor
	 * @param businessSymbol the symbol for the business to create
	 */
	public BusinessServant(String businessSymbol) {
		business = new Business(businessSymbol);
	}

	/**
	 * Gets the price of a share
	 * @return price of share
	 */
	public float getUnitPrice() {
		// All share types are assumed to have the same price, so return 
		// price of a common share
		return business.getShareInfo(ShareType.COMMON).getUnitPrice();
	}
	
	/*
	 * IMPLEMENTATION OF THE BUSINESS STUB
	 */
	public String getTicker() {
		return business.getTicker();
	}

	public boolean issueShares(String orderNum, String brokerRef,
			String businessSymbol, int shareType, float unitPrice,
			int quantity, float unitPriceOrder) {
		ShareType aST = ShareType.values()[shareType];
		ShareOrder aSO = new ShareOrder(orderNum, brokerRef, businessSymbol,
				aST, unitPrice, quantity, unitPriceOrder);
		return business.issueShares(aSO);
	}

	public boolean recievePayment(String orderNum, float totalPrice) {
		return business.recievePayment(orderNum, totalPrice);
	}


	/**
	 * Below methods are required to implement from interface_business
	 * We implement from interface_business so that business servant can be cast to interface business
	 *
	 */
	@Override
	public DomainManager[] _get_domain_managers() {return new DomainManager[0];}
	@Override
	public int _hash(int maximum) { return 0;}
	@Override
	public Object _set_policy_override(Policy[] policies, SetOverrideType set_add) {return null;}
	@Override
	public Policy _get_policy(int policy_type) {return null;}
	@Override
	public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result) {return null;}
	@Override
	public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result, ExceptionList exclist, ContextList ctxlist) {return null;}
	@Override
	public boolean _is_equivalent(Object other) {return false;}
	@Override
	public Request _request(String operation) {return null;}
	@Override
	public void _release() {}
	@Override
	public Object _duplicate() {return null;}



}
