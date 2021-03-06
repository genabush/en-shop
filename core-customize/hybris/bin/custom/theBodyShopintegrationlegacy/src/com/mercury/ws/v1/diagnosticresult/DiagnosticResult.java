//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.11 at 10:50:10 AM GMT 
//


package com.mercury.ws.v1.diagnosticresult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import uk.co.thebodyshop.integration.jaxb.skindiagnostic.SkinCareDiagnosticsResult;


/**
 * <p>Java class for diagnosticResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="diagnosticResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="request" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Firstname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Lastname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Market" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Channel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="StoreID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SkinCareDiagnosticDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                   &lt;element name="SkinCareDiagnosticsResult" type="{http://www.example.org/CustomerSkinDiagnosticsResult}SkinCareDiagnosticsResult" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="messageid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diagnosticResult", propOrder = {
    "request",
    "messageid"
})
public class DiagnosticResult {

    protected DiagnosticResult.Request request;
    protected String messageid;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link DiagnosticResult.Request }
     *     
     */
    public DiagnosticResult.Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagnosticResult.Request }
     *     
     */
    public void setRequest(DiagnosticResult.Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the messageid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageid() {
        return messageid;
    }

    /**
     * Sets the value of the messageid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageid(String value) {
        this.messageid = value;
    }


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
     *         &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Firstname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Lastname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Market" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Channel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="StoreID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SkinCareDiagnosticDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *         &lt;element name="SkinCareDiagnosticsResult" type="{http://www.example.org/CustomerSkinDiagnosticsResult}SkinCareDiagnosticsResult" minOccurs="0"/>
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
        "customerID",
        "firstname",
        "lastname",
        "emailAddress",
        "gender",
        "market",
        "channel",
        "storeID",
        "skinCareDiagnosticDate",
        "skinCareDiagnosticsResult"
    })
    public static class Request {

        @XmlElement(name = "CustomerID", required = true)
        protected String customerID;
        @XmlElement(name = "Firstname")
        protected String firstname;
        @XmlElement(name = "Lastname")
        protected String lastname;
        @XmlElement(name = "EmailAddress")
        protected String emailAddress;
        @XmlElement(name = "Gender")
        protected String gender;
        @XmlElement(name = "Market")
        protected String market;
        @XmlElement(name = "Channel")
        protected String channel;
        @XmlElement(name = "StoreID")
        protected String storeID;
        @XmlElement(name = "SkinCareDiagnosticDate")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar skinCareDiagnosticDate;
        @XmlElement(name = "SkinCareDiagnosticsResult")
        protected SkinCareDiagnosticsResult skinCareDiagnosticsResult;

        /**
         * Gets the value of the customerID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCustomerID() {
            return customerID;
        }

        /**
         * Sets the value of the customerID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCustomerID(String value) {
            this.customerID = value;
        }

        /**
         * Gets the value of the firstname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFirstname() {
            return firstname;
        }

        /**
         * Sets the value of the firstname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFirstname(String value) {
            this.firstname = value;
        }

        /**
         * Gets the value of the lastname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLastname() {
            return lastname;
        }

        /**
         * Sets the value of the lastname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLastname(String value) {
            this.lastname = value;
        }

        /**
         * Gets the value of the emailAddress property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmailAddress() {
            return emailAddress;
        }

        /**
         * Sets the value of the emailAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmailAddress(String value) {
            this.emailAddress = value;
        }

        /**
         * Gets the value of the gender property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGender() {
            return gender;
        }

        /**
         * Sets the value of the gender property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGender(String value) {
            this.gender = value;
        }

        /**
         * Gets the value of the market property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMarket() {
            return market;
        }

        /**
         * Sets the value of the market property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMarket(String value) {
            this.market = value;
        }

        /**
         * Gets the value of the channel property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getChannel() {
            return channel;
        }

        /**
         * Sets the value of the channel property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setChannel(String value) {
            this.channel = value;
        }

        /**
         * Gets the value of the storeID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStoreID() {
            return storeID;
        }

        /**
         * Sets the value of the storeID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStoreID(String value) {
            this.storeID = value;
        }

        /**
         * Gets the value of the skinCareDiagnosticDate property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getSkinCareDiagnosticDate() {
            return skinCareDiagnosticDate;
        }

        /**
         * Sets the value of the skinCareDiagnosticDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setSkinCareDiagnosticDate(XMLGregorianCalendar value) {
            this.skinCareDiagnosticDate = value;
        }

        /**
         * Gets the value of the skinCareDiagnosticsResult property.
         * 
         * @return
         *     possible object is
         *     {@link SkinCareDiagnosticsResult }
         *     
         */
        public SkinCareDiagnosticsResult getSkinCareDiagnosticsResult() {
            return skinCareDiagnosticsResult;
        }

        /**
         * Sets the value of the skinCareDiagnosticsResult property.
         * 
         * @param value
         *     allowed object is
         *     {@link SkinCareDiagnosticsResult }
         *     
         */
        public void setSkinCareDiagnosticsResult(SkinCareDiagnosticsResult value) {
            this.skinCareDiagnosticsResult = value;
        }

    }

}
