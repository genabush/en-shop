//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.09 at 11:40:02 AM BST 
//


package uk.co.thebodyshop.integration.jaxb.store.customer.detail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Benefit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Benefit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BenefitType" type="{http://thebodyshop/customerDetailSchema}BenefitTypeEnum"/>
 *         &lt;element name="BenefitValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BenefitCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpiryDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Currency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Benefit", propOrder = {
    "benefitType",
    "benefitValue",
    "benefitCode",
    "reason",
    "status",
    "expiryDate",
    "currency"
})
public class Benefit {

    @XmlElement(name = "BenefitType", required = true)
    protected BenefitTypeEnum benefitType;
    @XmlElement(name = "BenefitValue", required = true)
    protected String benefitValue;
    @XmlElement(name = "BenefitCode", required = true)
    protected String benefitCode;
    @XmlElement(name = "Reason", required = true)
    protected String reason;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "ExpiryDate", required = true)
    protected String expiryDate;
    @XmlElement(name = "Currency", required = true)
    protected String currency;

    /**
     * Gets the value of the benefitType property.
     * 
     * @return
     *     possible object is
     *     {@link BenefitTypeEnum }
     *     
     */
    public BenefitTypeEnum getBenefitType() {
        return benefitType;
    }

    /**
     * Sets the value of the benefitType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BenefitTypeEnum }
     *     
     */
    public void setBenefitType(BenefitTypeEnum value) {
        this.benefitType = value;
    }

    /**
     * Gets the value of the benefitValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBenefitValue() {
        return benefitValue;
    }

    /**
     * Sets the value of the benefitValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBenefitValue(String value) {
        this.benefitValue = value;
    }

    /**
     * Gets the value of the benefitCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBenefitCode() {
        return benefitCode;
    }

    /**
     * Sets the value of the benefitCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBenefitCode(String value) {
        this.benefitCode = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryDate(String value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

}
