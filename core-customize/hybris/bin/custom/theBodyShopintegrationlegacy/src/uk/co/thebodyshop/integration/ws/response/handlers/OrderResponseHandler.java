/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.response.handlers;

import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.springframework.util.Assert;

import com.mercury.ws.v1.placeorder.HybrisResponse;
import com.mercury.ws.v1.placeorder.PlaceOrder;
import com.mercury.ws.v1.placeorder.PlaceOrderResponse;

import uk.co.thebodyshop.core.services.TbsOrderService;
import uk.co.thebodyshop.integration.jaxb.order.Order;


/**
 * @author vasanthramprakasam
 */
public class OrderResponseHandler extends AbstractResponseHandler<JAXBElement<PlaceOrder>,JAXBElement<PlaceOrderResponse>>
{

	private final TbsOrderService tbsOrderService;

	public OrderResponseHandler(TbsOrderService tbsOrderService)
	{
		this.tbsOrderService = tbsOrderService;
	}

	@Override
	protected void handleSuccessFulResponse(JAXBElement<PlaceOrder> input, JAXBElement<PlaceOrderResponse> output)
	{
		final PlaceOrder placeOrder = input.getValue();
		Assert.notNull(placeOrder,"Place order is required");
		final Order order = placeOrder.getRequest();
		Assert.notNull(order,"Order can't be null");
		final String orderCode = order.getCode();
		if(LOG.isDebugEnabled())
		{
			LOG.debug("Received status [{}] for order [{}]", Optional.ofNullable(output.getValue()).map(PlaceOrderResponse::getReturn).map(
					HybrisResponse::getStatus).orElse(null),orderCode);
		}
	}

	protected TbsOrderService getTbsOrderService()
	{
		return tbsOrderService;
	}
}
