
package WebServices.ExchangeClientServices;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for share complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="share">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="businessSymbol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shareType" type="{http://exchange.stockexchange/}shareType" minOccurs="0"/>
 *         &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "share", propOrder = {
    "businessSymbol",
    "shareType",
    "unitPrice"
})
@XmlSeeAlso({
    ShareItem.class
})
public class Share {

    protected String businessSymbol;
    @XmlSchemaType(name = "string")
    protected ShareType shareType;
    protected float unitPrice;

    /**
     * Gets the value of the businessSymbol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessSymbol() {
        return businessSymbol;
    }

    /**
     * Sets the value of the businessSymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessSymbol(String value) {
        this.businessSymbol = value;
    }

    /**
     * Gets the value of the shareType property.
     * 
     * @return
     *     possible object is
     *     {@link ShareType }
     *     
     */
    public ShareType getShareType() {
        return shareType;
    }

    /**
     * Sets the value of the shareType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShareType }
     *     
     */
    public void setShareType(ShareType value) {
        this.shareType = value;
    }

    /**
     * Gets the value of the unitPrice property.
     * 
     */
    public float getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the value of the unitPrice property.
     * 
     */
    public void setUnitPrice(float value) {
        this.unitPrice = value;
    }

}
