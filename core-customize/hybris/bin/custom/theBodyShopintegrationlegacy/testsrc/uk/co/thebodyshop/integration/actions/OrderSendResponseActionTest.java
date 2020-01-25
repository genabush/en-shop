/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.actions;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.xml.bind.JAXBElement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mercury.ws.v1.placeorder.ObjectFactory;
import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;

import de.hybris.bootstrap.annotations.UnitTest;

import uk.co.thebodyshop.integration.gateways.PlaceOrderGateway;
import uk.co.thebodyshop.integration.ws.response.handlers.ResponseHandler;


/**
 * @author vasanthramprakasam
 */
@UnitTest
public class OrderSendResponseActionTest
{

	private OrderSendResponseAction action;
	@Mock
	private PlaceOrderGateway placeOrderGateway;
	@Mock
	private ResponseHandler<JAXBElement<PlaceOrder>,JAXBElement<PlaceOrderResponse>> orderResponseHandler;
	private JAXBElement<PlaceOrder> input;
	@Mock
	private JAXBElement<PlaceOrderResponse> output;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		action = new OrderSendResponseAction(placeOrderGateway,orderResponseHandler);
		ObjectFactory objectFactory = new ObjectFactory();
		PlaceOrder placeOrder = objectFactory.createPlaceOrder();
		input = objectFactory.createPlaceOrder(placeOrder);
		when(placeOrderGateway.placeOrder(input)).thenReturn(output);
		when(orderResponseHandler.handleResponse(input,output)).thenReturn(true);
	}

	@Test
	public void testSendToGatewayAndCheckResponse()
	{
		action.sendToGatewayAndCheckResponse(input);
		verify(placeOrderGateway,times(1)).placeOrder(input);
		verify(orderResponseHandler,times(1)).handleResponse(input,output);
	}

}
