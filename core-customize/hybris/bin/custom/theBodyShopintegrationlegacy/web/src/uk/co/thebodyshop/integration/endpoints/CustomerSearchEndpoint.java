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

import uk.co.thebodyshop.integration.jaxb.store.customer.search.AdvancedCustomerSearchRequest;
import uk.co.thebodyshop.integration.jaxb.store.customer.search.AdvancedCustomerSearchResponse;
import uk.co.thebodyshop.integration.jaxb.store.customer.search.CustomerSearchRequest;
import uk.co.thebodyshop.integration.jaxb.store.customer.search.CustomerSearchResponse;


@Endpoint
public class CustomerSearchEndpoint
{
	private static final Logger LOG = Logger.getLogger(CustomerSearchEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/customerSearchSchema";

	private static final String DOB_PATTERN = "yyyy-MM-dd";

	@PayloadRoot(localPart = "CustomerSearchRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload CustomerSearchResponse customerSearch(final @RequestPayload CustomerSearchRequest request)
	{
		final CustomerSearchResponse response = new CustomerSearchResponse();

		return response;
	}

	@PayloadRoot(localPart = "AdvancedCustomerSearchRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload AdvancedCustomerSearchResponse advancedCustomerSearch(
			final @RequestPayload AdvancedCustomerSearchRequest request)
	{
		final AdvancedCustomerSearchResponse response = new AdvancedCustomerSearchResponse();

		return response;
	}
}
