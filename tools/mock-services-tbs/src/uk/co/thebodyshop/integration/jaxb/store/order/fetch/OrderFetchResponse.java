//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2016.11.07 at 02:29:28 PM GMT
//

package uk.co.thebodyshop.integration.jaxb.store.order.fetch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponseStatus" type="{http://thebodyshop/orderFetchSchema}ResponseStatus"/>
 *         &lt;element name="Order" type="{http://thebodyshop/orderFetchSchema}Order" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "responseStatus", "order" })
@XmlRootElement(name = "OrderFetchResponse")
public class OrderFetchResponse
{

    @XmlElement(name = "ResponseStatus", required = true)
    protected ResponseStatus responseStatus;

    @XmlElement(name = "Order")
    protected Order order;

    /**
     * Gets the value of the responseStatus property.
     *
     * @return possible object is {@link ResponseStatus }
     * 
     */
    public ResponseStatus getResponseStatus()
    {
        return responseStatus;
    }

    /**
     * Sets the value of the responseStatus property.
     *
     * @param value
     *            allowed object is {@link ResponseStatus }
     * 
     */
    public void setResponseStatus(final ResponseStatus value)
    {
        this.responseStatus = value;
    }

    /**
     * Gets the value of the order property.
     *
     * @return possible object is {@link Order }
     * 
     */
    public Order getOrder()
    {
        return order;
    }

    /**
     * Sets the value of the order property.
     *
     * @param value
     *            allowed object is {@link Order }
     * 
     */
    public void setOrder(final Order value)
    {
        this.order = value;
    }

}
