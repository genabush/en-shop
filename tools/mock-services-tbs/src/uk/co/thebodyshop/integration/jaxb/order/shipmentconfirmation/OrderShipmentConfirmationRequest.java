//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.06 at 05:41:00 PM BST 
//


package uk.co.thebodyshop.integration.jaxb.order.shipmentconfirmation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderShipmentConfirmation" type="{http://thebodyshop/orderShipmentConfirmationSchema}OrderShipmentConfirmation" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orderShipmentConfirmation"
})
@XmlRootElement(name = "OrderShipmentConfirmationRequest")
public class OrderShipmentConfirmationRequest {

    @XmlElement(name = "OrderShipmentConfirmation", required = true)
    protected List<OrderShipmentConfirmation> orderShipmentConfirmation;

    /**
     * Gets the value of the orderShipmentConfirmation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orderShipmentConfirmation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrderShipmentConfirmation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrderShipmentConfirmation }
     * 
     * 
     */
    public List<OrderShipmentConfirmation> getOrderShipmentConfirmation() {
        if (orderShipmentConfirmation == null) {
            orderShipmentConfirmation = new ArrayList<OrderShipmentConfirmation>();
        }
        return this.orderShipmentConfirmation;
    }

}
