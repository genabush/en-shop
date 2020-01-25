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

import uk.co.thebodyshop.integration.jaxb.product.ProductRequest;
import uk.co.thebodyshop.integration.jaxb.product.ProductResponse;


@Endpoint
public class ProductEndpoint
{
	private static final Logger LOG = Logger.getLogger(ProductEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/productSchema";

	@PayloadRoot(localPart = "ProductRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload ProductResponse productService(final @RequestPayload ProductRequest productRequest)
	{
		final ProductResponse productResponse = new ProductResponse();

		return productResponse;
	}
}
