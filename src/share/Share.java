package share;

/**
 * A class for share information
 * @author patrick
 */
public class Share {
	private String businessSymbol;
	private ShareType shareType;
	private float unitPrice;
	
	public Share(String businessSymbol, ShareType shareType, float unitPrice) {
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

	public ShareType getShareType() {
		return shareType;
	}

	public void setShareType(ShareType shareType) {
		this.shareType = shareType;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
}
