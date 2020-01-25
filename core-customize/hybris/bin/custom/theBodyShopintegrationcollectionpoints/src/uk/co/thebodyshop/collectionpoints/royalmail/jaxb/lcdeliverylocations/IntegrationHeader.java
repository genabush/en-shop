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
import java.math.BigDecimal;

/**
 * <p>Java class for integrationHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="integrationHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="date" type="{http://www.royalmailgroup.com/cm/rmDatatypes/V1}date" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.royalmailgroup.com/cm/rmDatatypes/V1}decimal" minOccurs="0"/>
 *         &lt;element name="identification" type="{http://www.royalmailgroup.com/integration/core/V1}identificationStructure"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "integrationHeader", namespace = "http://www.royalmailgroup.com/integration/core/V1", propOrder = {
    "date",
    "version",
    "identification"
})
public class IntegrationHeader {

    @XmlElement(namespace = "http://www.royalmailgroup.com/integration/core/V1")
    protected XMLGregorianCalendar date;
    @XmlElement(namespace = "http://www.royalmailgroup.com/integration/core/V1")
    protected BigDecimal version;
    @XmlElement(namespace = "http://www.royalmailgroup.com/integration/core/V1", required = true)
    protected IdentificationStructure identification;

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }

    /**
     * Gets the value of the identification property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationStructure }
     *     
     */
    public IdentificationStructure getIdentification() {
        return identification;
    }

    /**
     * Sets the value of the identification property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationStructure }
     *     
     */
    public void setIdentification(IdentificationStructure value) {
        this.identification = value;
    }

}
