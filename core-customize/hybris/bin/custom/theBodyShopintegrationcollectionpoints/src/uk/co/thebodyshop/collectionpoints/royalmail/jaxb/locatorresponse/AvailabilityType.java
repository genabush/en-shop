//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.21 at 04:55:09 PM BST 
//


package uk.co.thebodyshop.collectionpoints.royalmail.jaxb.locatorresponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AvailabilityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AvailabilityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShippingAvailability" type="{}AvailableReasonType" minOccurs="0"/>
 *         &lt;element name="DCRAvailability" type="{}AvailableReasonType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvailabilityType", propOrder = {
    "shippingAvailability",
    "dcrAvailability"
})
public class AvailabilityType {

    @XmlElement(name = "ShippingAvailability")
    protected AvailableReasonType shippingAvailability;
    @XmlElement(name = "DCRAvailability")
    protected AvailableReasonType dcrAvailability;

    /**
     * Gets the value of the shippingAvailability property.
     * 
     * @return
     *     possible object is
     *     {@link AvailableReasonType }
     *     
     */
    public AvailableReasonType getShippingAvailability() {
        return shippingAvailability;
    }

    /**
     * Sets the value of the shippingAvailability property.
     * 
     * @param value
     *     allowed object is
     *     {@link AvailableReasonType }
     *     
     */
    public void setShippingAvailability(AvailableReasonType value) {
        this.shippingAvailability = value;
    }

    /**
     * Gets the value of the dcrAvailability property.
     * 
     * @return
     *     possible object is
     *     {@link AvailableReasonType }
     *     
     */
    public AvailableReasonType getDCRAvailability() {
        return dcrAvailability;
    }

    /**
     * Sets the value of the dcrAvailability property.
     * 
     * @param value
     *     allowed object is
     *     {@link AvailableReasonType }
     *     
     */
    public void setDCRAvailability(AvailableReasonType value) {
        this.dcrAvailability = value;
    }

}
