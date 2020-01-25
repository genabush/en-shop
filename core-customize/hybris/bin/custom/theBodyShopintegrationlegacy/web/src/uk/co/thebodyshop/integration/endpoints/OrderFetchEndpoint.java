/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.endpoints;

import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import uk.co.thebodyshop.integration.jaxb.store.order.fetch.OrderFetchRequest;
import uk.co.thebodyshop.integration.jaxb.store.order.fetch.OrderFetchResponse;


@Endpoint
public class OrderFetchEndpoint
{
	private static final Logger LOG = Logger.getLogger(OrderFetchEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/orderFetchSchema";

	@PayloadRoot(localPart = "OrderFetchRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload OrderFetchResponse orderSearchService(final @RequestPayload OrderFetchRequest orderFetchRequest)
	{
		final OrderFetchResponse orderFetchResponse = new OrderFetchResponse();

		return orderFetchResponse;
	}
}
