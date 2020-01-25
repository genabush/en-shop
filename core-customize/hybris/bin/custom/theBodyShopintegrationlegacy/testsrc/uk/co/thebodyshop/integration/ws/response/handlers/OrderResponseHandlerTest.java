/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.response.handlers;

import static org.mockito.Mockito.when;

import java.util.function.Predicate;

import javax.xml.bind.JAXBElement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.services.TbsOrderService;
import uk.co.thebodyshop.integration.jaxb.order.Order;


/**
 * @author vasanthramprakasam
 */
@UnitTest
public class OrderResponseHandlerTest
{
	private OrderResponseHandler orderResponseHandler;
	@Mock
	private TbsOrderService tbsOrderService;
	@Mock
	private Predicate<Object> retryOnErrorPredicate;
	@Mock
	private ModelService modelService;
	@Mock
	private JAXBElement<PlaceOrder> input;
	@Mock
	private JAXBElement<PlaceOrderResponse> output;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		orderResponseHandler = new OrderResponseHandler(tbsOrderService);
		orderResponseHandler.setModelService(modelService);
		orderResponseHandler.setRetryOnErrorPredicate(retryOnErrorPredicate);
		PlaceOrder placeOrder = new PlaceOrder();
		Order order = new Order();
		order.setCode("code");
		placeOrder.setRequest(order);
		when(input.getValue()).thenReturn(placeOrder);
	}

	@Test
	public void testSuccessResponse()
	{
		when(retryOnErrorPredicate.test(output)).thenReturn(false);
		boolean result = orderResponseHandler.handleResponse(input,output);
		Assert.assertTrue(result);
	}

	@Test
	public void testFailureResponse()
	{
		when(retryOnErrorPredicate.test(output)).thenReturn(true);
		boolean result = orderResponseHandler.handleResponse(input,output);
		Assert.assertFalse(result);
	}

}
