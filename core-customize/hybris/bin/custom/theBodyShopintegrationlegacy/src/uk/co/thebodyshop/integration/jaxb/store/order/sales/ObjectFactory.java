//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.27 at 03:33:52 PM BST 
//


package uk.co.thebodyshop.integration.jaxb.store.order.sales;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.co.thebodyshop.integration.jaxb.store.order.sales package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.co.thebodyshop.integration.jaxb.store.order.sales
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link Order.Entries }
     * 
     */
    public Order.Entries createOrderEntries() {
        return new Order.Entries();
    }

    /**
     * Create an instance of {@link Order.Entries.Entry }
     * 
     */
    public Order.Entries.Entry createOrderEntriesEntry() {
        return new Order.Entries.Entry();
    }

    /**
     * Create an instance of {@link Order.Taxes }
     * 
     */
    public Order.Taxes createOrderTaxes() {
        return new Order.Taxes();
    }

    /**
     * Create an instance of {@link Order.Payments }
     * 
     */
    public Order.Payments createOrderPayments() {
        return new Order.Payments();
    }

    /**
     * Create an instance of {@link OrderResponse }
     * 
     */
    public OrderResponse createOrderResponse() {
        return new OrderResponse();
    }

    /**
     * Create an instance of {@link ResponseDetail }
     * 
     */
    public ResponseDetail createResponseDetail() {
        return new ResponseDetail();
    }

    /**
     * Create an instance of {@link OrderRequest }
     * 
     */
    public OrderRequest createOrderRequest() {
        return new OrderRequest();
    }

    /**
     * Create an instance of {@link ShippingAddressType }
     * 
     */
    public ShippingAddressType createShippingAddressType() {
        return new ShippingAddressType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link Order.Customer }
     * 
     */
    public Order.Customer createOrderCustomer() {
        return new Order.Customer();
    }

    /**
     * Create an instance of {@link Order.Entries.Entry.LinePromotions }
     * 
     */
    public Order.Entries.Entry.LinePromotions createOrderEntriesEntryLinePromotions() {
        return new Order.Entries.Entry.LinePromotions();
    }

    /**
     * Create an instance of {@link Order.Taxes.Tax }
     * 
     */
    public Order.Taxes.Tax createOrderTaxesTax() {
        return new Order.Taxes.Tax();
    }

    /**
     * Create an instance of {@link Order.Payments.Payment }
     * 
     */
    public Order.Payments.Payment createOrderPaymentsPayment() {
        return new Order.Payments.Payment();
    }

}
