/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.actions;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;

import uk.co.thebodyshop.integration.gateways.PlaceOrderGateway;
import uk.co.thebodyshop.integration.utils.XmlUtils;
import uk.co.thebodyshop.integration.ws.response.handlers.ResponseHandler;


/**
 * @author vasanthramprakasam
 */
public class OrderSendResponseAction implements SendCheckResponseAction<JAXBElement<PlaceOrder>>
{

	private static final Logger LOG = LoggerFactory.getLogger(OrderSendResponseAction.class);

	private final PlaceOrderGateway placeOrderGateway;

	private final ResponseHandler<JAXBElement<PlaceOrder>,JAXBElement<PlaceOrderResponse>> orderResponseHandler;

	public OrderSendResponseAction(PlaceOrderGateway placeOrderGateway,
			ResponseHandler<JAXBElement<PlaceOrder>, JAXBElement<PlaceOrderResponse>> orderResponseHandler)
	{
		this.placeOrderGateway = placeOrderGateway;
		this.orderResponseHandler = orderResponseHandler;
	}


	@Override
	public boolean sendToGatewayAndCheckResponse(JAXBElement<PlaceOrder> input)
	{
		JAXBElement<PlaceOrderResponse> output = getPlaceOrderGateway().placeOrder(input);
		if(LOG.isDebugEnabled())
		{
			LOG.debug("Place Order Response received is \n {}", XmlUtils.toXml(output));
		}
		return getOrderResponseHandler().handleResponse(input,output);
	}

	protected PlaceOrderGateway getPlaceOrderGateway()
	{
		return placeOrderGateway;
	}

	protected ResponseHandler<JAXBElement<PlaceOrder>, JAXBElement<PlaceOrderResponse>> getOrderResponseHandler()
	{
		return orderResponseHandler;
	}
}
