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
 * <p>Java class for LoyaltyCard complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LoyaltyCard">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CardType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CardNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IssueDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CardIssuingStore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoyaltyCard", propOrder = {
    "cardType",
    "cardNumber",
    "issueDate",
    "cardIssuingStore"
})
public class LoyaltyCard {

    @XmlElement(name = "CardType", required = true)
    protected String cardType;
    @XmlElement(name = "CardNumber", required = true)
    protected String cardNumber;
    @XmlElement(name = "IssueDate", required = true)
    protected String issueDate;
    @XmlElement(name = "CardIssuingStore", required = true)
    protected String cardIssuingStore;

    /**
     * Gets the value of the cardType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Sets the value of the cardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardType(String value) {
        this.cardType = value;
    }

    /**
     * Gets the value of the cardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the value of the cardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
    }

    /**
     * Gets the value of the issueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the value of the issueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueDate(String value) {
        this.issueDate = value;
    }

    /**
     * Gets the value of the cardIssuingStore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardIssuingStore() {
        return cardIssuingStore;
    }

    /**
     * Sets the value of the cardIssuingStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardIssuingStore(String value) {
        this.cardIssuingStore = value;
    }

}
