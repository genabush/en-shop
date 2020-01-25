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
 * <p>Java class for DayOfWeekType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DayOfWeekType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Day" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OpenHours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CloseHours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LatestDropOffHours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PrepHours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClosedIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Open24HoursIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DayOfWeekType", propOrder = {
    "day",
    "openHours",
    "closeHours",
    "latestDropOffHours",
    "prepHours",
    "closedIndicator",
    "open24HoursIndicator"
})
public class DayOfWeekType {

    @XmlElement(name = "Day", required = true)
    protected String day;
    @XmlElement(name = "OpenHours")
    protected String openHours;
    @XmlElement(name = "CloseHours")
    protected String closeHours;
    @XmlElement(name = "LatestDropOffHours")
    protected String latestDropOffHours;
    @XmlElement(name = "PrepHours")
    protected String prepHours;
    @XmlElement(name = "ClosedIndicator")
    protected String closedIndicator;
    @XmlElement(name = "Open24HoursIndicator")
    protected String open24HoursIndicator;

    /**
     * Gets the value of the day property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the value of the day property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDay(String value) {
        this.day = value;
    }

    /**
     * Gets the value of the openHours property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenHours() {
        return openHours;
    }

    /**
     * Sets the value of the openHours property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenHours(String value) {
        this.openHours = value;
    }

    /**
     * Gets the value of the closeHours property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloseHours() {
        return closeHours;
    }

    /**
     * Sets the value of the closeHours property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloseHours(String value) {
        this.closeHours = value;
    }

    /**
     * Gets the value of the latestDropOffHours property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatestDropOffHours() {
        return latestDropOffHours;
    }

    /**
     * Sets the value of the latestDropOffHours property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatestDropOffHours(String value) {
        this.latestDropOffHours = value;
    }

    /**
     * Gets the value of the prepHours property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrepHours() {
        return prepHours;
    }

    /**
     * Sets the value of the prepHours property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrepHours(String value) {
        this.prepHours = value;
    }

    /**
     * Gets the value of the closedIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosedIndicator() {
        return closedIndicator;
    }

    /**
     * Sets the value of the closedIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosedIndicator(String value) {
        this.closedIndicator = value;
    }

    /**
     * Gets the value of the open24HoursIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpen24HoursIndicator() {
        return open24HoursIndicator;
    }

    /**
     * Sets the value of the open24HoursIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpen24HoursIndicator(String value) {
        this.open24HoursIndicator = value;
    }

}
