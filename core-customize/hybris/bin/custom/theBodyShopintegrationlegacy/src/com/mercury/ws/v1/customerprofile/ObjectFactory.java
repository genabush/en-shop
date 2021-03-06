//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.15 at 12:12:33 PM GMT 
//


package com.mercury.ws.v1.customerprofile;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.mercury.ws.v1.customerprofile package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CustomerProfile_QNAME = new QName("http://customerProfile.v1.ws.mercury.com", "customerProfile");
    private final static QName _CustomerProfileResponse_QNAME = new QName("http://customerProfile.v1.ws.mercury.com", "customerProfileResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mercury.ws.v1.customerprofile
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CustomerProfile }
     * 
     */
    public CustomerProfile createCustomerProfile() {
        return new CustomerProfile();
    }

    /**
     * Create an instance of {@link CustomerProfileResponse }
     * 
     */
    public CustomerProfileResponse createCustomerProfileResponse() {
        return new CustomerProfileResponse();
    }

    /**
     * Create an instance of {@link HybrisResponse }
     * 
     */
    public HybrisResponse createHybrisResponse() {
        return new HybrisResponse();
    }

    /**
     * Create an instance of {@link CustomerProfile.Request }
     * 
     */
    public CustomerProfile.Request createCustomerProfileRequest() {
        return new CustomerProfile.Request();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerProfile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://customerProfile.v1.ws.mercury.com", name = "customerProfile")
    public JAXBElement<CustomerProfile> createCustomerProfile(CustomerProfile value) {
        return new JAXBElement<CustomerProfile>(_CustomerProfile_QNAME, CustomerProfile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerProfileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://customerProfile.v1.ws.mercury.com", name = "customerProfileResponse")
    public JAXBElement<CustomerProfileResponse> createCustomerProfileResponse(CustomerProfileResponse value) {
        return new JAXBElement<CustomerProfileResponse>(_CustomerProfileResponse_QNAME, CustomerProfileResponse.class, null, value);
    }

}
