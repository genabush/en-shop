
package uk.co.thebodyshop.integration.jaxb.orderstatus.update;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderStatusEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OrderStatusEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="READY_FOR_PICKING"/>
 *     &lt;enumeration value="READY_FOR_COLLECTION"/>
 *     &lt;enumeration value="COLLECTED"/>
 *     &lt;enumeration value="REFUNDED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OrderStatusEnum", namespace = "http://thebodyshop/orderUpdateSchema")
@XmlEnum
public enum OrderStatusEnum {

    READY_FOR_PICKING,
    READY_FOR_COLLECTION,
    COLLECTED,
    REFUNDED;

    public String value() {
        return name();
    }

    public static OrderStatusEnum fromValue(String v) {
        return valueOf(v);
    }

}
