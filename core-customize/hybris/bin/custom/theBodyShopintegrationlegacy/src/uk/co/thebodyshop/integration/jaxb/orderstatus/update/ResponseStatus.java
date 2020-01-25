
package uk.co.thebodyshop.integration.jaxb.orderstatus.update;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResponseStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResponseStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUCCESS"/>
 *     &lt;enumeration value="FAILURE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResponseStatus", namespace = "http://thebodyshop/orderUpdateSchema")
@XmlEnum
public enum ResponseStatus {

    SUCCESS,
    FAILURE;

    public String value() {
        return name();
    }

    public static ResponseStatus fromValue(String v) {
        return valueOf(v);
    }

}
