package common.share;

import java.io.Serializable;

/**
 * A class for common.share information
 */
public class Share implements Serializable {

	private static final long serialVersionUID = 1L;	// required for serialization
	private String businessSymbol;
	private ShareType shareType;
	private float unitPrice;
	
	public Share(String businessSymbol, ShareType shareType, float unitPrice) {
		setBusinessSymbol(businessSymbol);
		setShareType(shareType);
		setUnitPrice(unitPrice);
	}
	
	public Share() {		
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

	@Override
	public String toString() {
		return getBusinessSymbol() + ": (" + getShareType() + ", " + getUnitPrice() + ")";
	}

}
