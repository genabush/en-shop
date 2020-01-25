//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.30 at 10:52:47 AM GMT 
//


package com.mercury.ws.v1.paymentcapture;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.mercury.ws.v1.paymentcapture package. 
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

    private final static QName _PaymentCapture_QNAME = new QName("http://paymentCapture.v1.ws.mercury.com", "paymentCapture");
    private final static QName _PaymentCaptureResponse_QNAME = new QName("http://paymentCapture.v1.ws.mercury.com", "paymentCaptureResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mercury.ws.v1.paymentcapture
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaymentCaptureResponse }
     * 
     */
    public PaymentCaptureResponse createPaymentCaptureResponse() {
        return new PaymentCaptureResponse();
    }

    /**
     * Create an instance of {@link PaymentCapture }
     * 
     */
    public PaymentCapture createPaymentCapture() {
        return new PaymentCapture();
    }

    /**
     * Create an instance of {@link HybrisResponse }
     * 
     */
    public HybrisResponse createHybrisResponse() {
        return new HybrisResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentCapture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://paymentCapture.v1.ws.mercury.com", name = "paymentCapture")
    public JAXBElement<PaymentCapture> createPaymentCapture(PaymentCapture value) {
        return new JAXBElement<PaymentCapture>(_PaymentCapture_QNAME, PaymentCapture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentCaptureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://paymentCapture.v1.ws.mercury.com", name = "paymentCaptureResponse")
    public JAXBElement<PaymentCaptureResponse> createPaymentCaptureResponse(PaymentCaptureResponse value) {
        return new JAXBElement<PaymentCaptureResponse>(_PaymentCaptureResponse_QNAME, PaymentCaptureResponse.class, null, value);
    }

}
