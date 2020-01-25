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

import uk.co.thebodyshop.integration.jaxb.store.order.search.OrderSearchRequest;
import uk.co.thebodyshop.integration.jaxb.store.order.search.OrderSearchResponse;


@Endpoint
public class OrderSearchEndpoint
{
	private static final Logger LOG = Logger.getLogger(OrderSearchEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/orderSearchSchema";

	@PayloadRoot(localPart = "OrderSearchRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload OrderSearchResponse orderSearchService(final @RequestPayload OrderSearchRequest orderSearchRequest)
	{
		final OrderSearchResponse orderSearchResponse = new OrderSearchResponse();

		return orderSearchResponse;
	}
}
