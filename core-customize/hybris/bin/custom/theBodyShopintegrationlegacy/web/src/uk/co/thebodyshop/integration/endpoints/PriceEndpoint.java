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

import uk.co.thebodyshop.integration.jaxb.price.PriceRequest;
import uk.co.thebodyshop.integration.jaxb.price.PriceResponse;


@Endpoint
public class PriceEndpoint
{
	private static final Logger LOG = Logger.getLogger(PriceEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/priceSchema";

	@PayloadRoot(localPart = "PriceRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload PriceResponse priceService(final @RequestPayload PriceRequest priceRequest)
	{
		final PriceResponse priceResponse = new PriceResponse();

		return priceResponse;
	}
}
