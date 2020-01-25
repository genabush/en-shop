/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.mocks.response;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercury.ws.v1.placeorder.HybrisResponse;
import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;

import static uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants.SERVICE_RESPONSE_STATUS.FAILED;

/**
 * @author vasanthramprakasam
 */
@Component("placeOrderResponseFactory")
public class PlaceOrderResponseFactory extends AbstractResponseFactory<PlaceOrder, PlaceOrderResponse>
{
	 private static final String SUCCESS = "SUCCESS";

	 @Autowired
	 private Predicate<PlaceOrder> orderFailPredicate;

	 @Override
	 protected PlaceOrderResponse createFailureResponse(PlaceOrder type)
	 {
			PlaceOrderResponse placeOrderResponse = createCommonResponse(type);
			placeOrderResponse.getReturn().setStatus(FAILED);
			return placeOrderResponse;
	 }

	 private PlaceOrderResponse createCommonResponse(PlaceOrder type)
	 {
			PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
			HybrisResponse hybrisResponse = new HybrisResponse();
			hybrisResponse.setMessageId(type.getMessageid());
			hybrisResponse.setRequestId(type.getMessageid());
			placeOrderResponse.setReturn(hybrisResponse);
			return placeOrderResponse;
	 }

	 @Override
	 protected PlaceOrderResponse createSuccessResponse(PlaceOrder type)
	 {
			PlaceOrderResponse placeOrderResponse = createCommonResponse(type);
			placeOrderResponse.getReturn().setStatus(SUCCESS);
			return placeOrderResponse;
	 }

	 @Override
	 protected Predicate<PlaceOrder> getFailurePredicate()
	 {
			return orderFailPredicate;
	 }
}
