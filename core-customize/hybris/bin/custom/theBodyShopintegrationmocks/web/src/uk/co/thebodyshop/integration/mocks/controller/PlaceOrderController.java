/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.mocks.controller;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.mercury.ws.v1.placeorder.ObjectFactory;
import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;

import uk.co.thebodyshop.integration.mocks.response.PlaceOrderResponseFactory;

/**
 * @author vasanthramprakasam
 */
@Endpoint
public class PlaceOrderController
{
	 private static final String NAMESPACE_URI = "http://placeOrder.v1.ws.mercury.com";

	 @Autowired
	 private PlaceOrderResponseFactory placeOrderResponseFactory;

	 @PayloadRoot(namespace = NAMESPACE_URI, localPart = "placeOrder")
	 @ResponsePayload
	 public JAXBElement<PlaceOrderResponse> order(@RequestPayload JAXBElement<PlaceOrder> placeOrder)
	 {
			PlaceOrderResponse response = placeOrderResponseFactory.createResponse(placeOrder.getValue());
			ObjectFactory objectFactory = new ObjectFactory();
			return objectFactory.createPlaceOrderResponse(response);
	 }
}
