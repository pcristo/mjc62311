package business;

/**
 * A class for share information
 * @author patrick
 */
public class Share {
	private String businessSymbol;
	private String shareType;
	private float unitPrice;
	
	public Share(String businessSymbol, String shareType, float unitPrice) {
		setBusinessSymbol(businessSymbol);
		setShareType(shareType);
		setUnitPrice(unitPrice);
	}

	public String getBusinessSymbol() {
		return businessSymbol;
	}

	public void setBusinessSymbol(String businessSymbol) {
		this.businessSymbol = businessSymbol;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
}
