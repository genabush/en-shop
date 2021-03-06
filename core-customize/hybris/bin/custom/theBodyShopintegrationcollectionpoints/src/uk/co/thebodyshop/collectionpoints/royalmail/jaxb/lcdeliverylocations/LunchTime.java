//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.20 at 04:19:52 PM BST 
//


package uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for lunchTime complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lunchTime">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lunchClosingTime" type="{http://www.royalmailgroup.com/API/LocalCollect/V2.0}lunchOpeningTime"/>
 *         &lt;element name="lunchOpeningTime" type="{http://www.royalmailgroup.com/API/LocalCollect/V2.0}lunchClosingTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lunchTime", namespace = "http://www.royalmailgroup.com/API/LocalCollect/V2.0", propOrder = {
    "lunchClosingTime",
    "lunchOpeningTime"
})
public class LunchTime {

    @XmlElement(namespace = "http://www.royalmailgroup.com/API/LocalCollect/V2.0", required = true)
    protected XMLGregorianCalendar lunchClosingTime;
    @XmlElement(namespace = "http://www.royalmailgroup.com/API/LocalCollect/V2.0", required = true)
    protected XMLGregorianCalendar lunchOpeningTime;

    /**
     * Gets the value of the lunchClosingTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLunchClosingTime() {
        return lunchClosingTime;
    }

    /**
     * Sets the value of the lunchClosingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLunchClosingTime(XMLGregorianCalendar value) {
        this.lunchClosingTime = value;
    }

    /**
     * Gets the value of the lunchOpeningTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLunchOpeningTime() {
        return lunchOpeningTime;
    }

    /**
     * Sets the value of the lunchOpeningTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLunchOpeningTime(XMLGregorianCalendar value) {
        this.lunchOpeningTime = value;
    }

}
