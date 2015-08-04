
package WebServices.ExchangeClientServices;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for shareType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="shareType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PREFERRED"/>
 *     &lt;enumeration value="COMMON"/>
 *     &lt;enumeration value="CONVERTIBLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "shareType")
@XmlEnum
public enum ShareType {

    PREFERRED,
    COMMON,
    CONVERTIBLE;

    public String value() {
        return name();
    }

    public static ShareType fromValue(String v) {
        return valueOf(v);
    }

}
