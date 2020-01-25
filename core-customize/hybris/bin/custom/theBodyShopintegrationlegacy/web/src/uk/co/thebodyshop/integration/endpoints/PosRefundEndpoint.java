/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import uk.co.thebodyshop.integration.jaxb.store.refund.RefundRequest;
import uk.co.thebodyshop.integration.jaxb.store.refund.RefundResponse;


@Endpoint
public class PosRefundEndpoint
{

	private static final String TARGET_NAMESPACE = "http://thebodyshop/posRefundSchema";

	@PayloadRoot(localPart = "RefundRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload RefundResponse placeOrders(final @RequestPayload RefundRequest request)
	{
		final String orderCode = request.getOrder().getCode().intern();
		synchronized (orderCode)
		{
			final RefundResponse response = new RefundResponse();

			return response;
		}
	}
}
