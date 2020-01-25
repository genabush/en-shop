//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.11 at 10:50:10 AM GMT 
//


package com.mercury.ws.v1.diagnosticresult;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.mercury.ws.v1.diagnosticresult package. 
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

    private final static QName _DiagnosticResultResponse_QNAME = new QName("http://diagnosticResult.v1.ws.mercury.com", "diagnosticResultResponse");
    private final static QName _DiagnosticResult_QNAME = new QName("http://diagnosticResult.v1.ws.mercury.com", "diagnosticResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.mercury.ws.v1.diagnosticresult
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DiagnosticResult }
     * 
     */
    public DiagnosticResult createDiagnosticResult() {
        return new DiagnosticResult();
    }

    /**
     * Create an instance of {@link DiagnosticResultResponse }
     * 
     */
    public DiagnosticResultResponse createDiagnosticResultResponse() {
        return new DiagnosticResultResponse();
    }

    /**
     * Create an instance of {@link HybrisResponse }
     * 
     */
    public HybrisResponse createHybrisResponse() {
        return new HybrisResponse();
    }

    /**
     * Create an instance of {@link DiagnosticResult.Request }
     * 
     */
    public DiagnosticResult.Request createDiagnosticResultRequest() {
        return new DiagnosticResult.Request();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DiagnosticResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://diagnosticResult.v1.ws.mercury.com", name = "diagnosticResultResponse")
    public JAXBElement<DiagnosticResultResponse> createDiagnosticResultResponse(DiagnosticResultResponse value) {
        return new JAXBElement<DiagnosticResultResponse>(_DiagnosticResultResponse_QNAME, DiagnosticResultResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DiagnosticResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://diagnosticResult.v1.ws.mercury.com", name = "diagnosticResult")
    public JAXBElement<DiagnosticResult> createDiagnosticResult(DiagnosticResult value) {
        return new JAXBElement<DiagnosticResult>(_DiagnosticResult_QNAME, DiagnosticResult.class, null, value);
    }

}
