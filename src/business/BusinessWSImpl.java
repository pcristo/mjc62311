package business;

import javax.jws.WebService;


/*
 * This class is an implementation of Business to include webservice endpoints
 */
@WebService(endpointInterface="business.IBusiness")  
public class BusinessWSImpl extends Business implements IBusiness {

	private static final long serialVersionUID = 1L;

	public BusinessWSImpl(String identifier) {
		super(identifier);
	}

}
