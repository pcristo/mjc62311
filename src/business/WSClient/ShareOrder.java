
package business.WSClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for shareOrder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="shareOrder">
 *   &lt;complexContent>
 *     &lt;extension base="{http://business/}share">
 *       &lt;sequence>
 *         &lt;element name="brokerRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unitPriceOrder" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shareOrderws", propOrder = {
    "brokerRef",
    "orderNum",
    "quantity",
    "unitPriceOrder"
})
public class ShareOrder
    extends Share
{

    protected String brokerRef;
    protected String orderNum;
    protected int quantity;
    protected float unitPriceOrder;

    /**
     * Gets the value of the brokerRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrokerRef() {
        return brokerRef;
    }

    /**
     * Sets the value of the brokerRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrokerRef(String value) {
        this.brokerRef = value;
    }

    /**
     * Gets the value of the orderNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * Sets the value of the orderNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderNum(String value) {
        this.orderNum = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the unitPriceOrder property.
     * 
     */
    public float getUnitPriceOrder() {
        return unitPriceOrder;
    }

    /**
     * Sets the value of the unitPriceOrder property.
     * 
     */
    public void setUnitPriceOrder(float value) {
        this.unitPriceOrder = value;
    }

}
