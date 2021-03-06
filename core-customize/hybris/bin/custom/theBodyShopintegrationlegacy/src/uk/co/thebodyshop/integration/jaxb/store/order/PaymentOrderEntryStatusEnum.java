//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.17 at 08:34:26 AM BST 
//


package uk.co.thebodyshop.integration.jaxb.store.order;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentOrderEntryStatusEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PaymentOrderEntryStatusEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PAYMENT_AUTHORIZED"/>
 *     &lt;enumeration value="PAYMENT_CAPTURED"/>
 *     &lt;enumeration value="PAYMENT_REFUNDED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PaymentOrderEntryStatusEnum")
@XmlEnum
public enum PaymentOrderEntryStatusEnum {

    PAYMENT_AUTHORIZED,
    PAYMENT_CAPTURED,
    PAYMENT_REFUNDED;

    public String value() {
        return name();
    }

    public static PaymentOrderEntryStatusEnum fromValue(String v) {
        return valueOf(v);
    }

}
