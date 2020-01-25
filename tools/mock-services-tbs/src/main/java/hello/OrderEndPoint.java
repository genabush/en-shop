package hello;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import uk.co.thebodyshop.integration.jaxb.order.Order;
import uk.co.thebodyshop.integration.jaxb.order.OrderRequest;
import uk.co.thebodyshop.integration.jaxb.order.OrderResponse;

import java.util.Optional;

/**
 * @author vasanthramprakasam
 */
@Endpoint
public class OrderEndPoint {

    private static final String NAMESPACE_URI = "http://thebodyshop/orderSchema";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "OrderRequest")
    @ResponsePayload
    public OrderResponse placeOrder(@RequestPayload OrderRequest request) {
        String orderCode = Optional.ofNullable(request.getOrder()).map(Order::getCode).orElse("EMPTY CODE");
        OrderResponse response = new OrderResponse();
        response.setStatusCode("200");
        response.setStatusMessage(orderCode+"Success");
        return response;
    }

}
