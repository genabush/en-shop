package hello;

import com.mercury.ws.v1.placeorder.HybrisResponse;
import com.mercury.ws.v1.placeorder.ObjectFactory;
import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;
import java.util.Random;

/**
 * @author vasanthramprakasam
 */
@Endpoint
public class PlaceOrderEndPoint {

    private static final String NAMESPACE_URI = "http://placeOrder.v1.ws.mercury.com";

    private static final Random RANDOM = new Random();

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "placeOrder")
    @ResponsePayload
    public JAXBElement<PlaceOrderResponse> order(@RequestPayload JAXBElement<PlaceOrder> placeOrder) {
        PlaceOrderResponse response = new PlaceOrderResponse();
        HybrisResponse hybrisResponse = new HybrisResponse();
        PlaceOrder placeOrderRequest = placeOrder.getValue();
        hybrisResponse.setMessageId(placeOrderRequest.getMessageid());
        hybrisResponse.setRequestId(placeOrderRequest.getRequest().getCode());
        hybrisResponse.setStatus(getResponseStatus(placeOrderRequest));
        hybrisResponse.setStatusMessage(getResponseStatus(placeOrderRequest));
        response.setReturn(hybrisResponse);
        ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createPlaceOrderResponse(response);
    }

    /**
     * Randomise the response status - fail every third order if the order code is a number
     * or randomly send success/failure
     * @param placeOrderRequest
     * @return
     */
    protected String getResponseStatus(PlaceOrder placeOrderRequest){
        String code = placeOrderRequest.getRequest().getCode();
        try {
            Double orderCode = Double.parseDouble(code);
            return orderCode % 3 == 0 ? "FAILED" : "SUCCESS";
        }catch (NumberFormatException nfe){
            return RANDOM.nextBoolean() ? "FAILED" : "SUCCESS";
        }
    }

}
