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

import uk.co.thebodyshop.integration.jaxb.store.customer.detail.CustomerDetailRequest;
import uk.co.thebodyshop.integration.jaxb.store.customer.detail.CustomerDetailResponse;
import uk.co.thebodyshop.integration.jaxb.store.customer.detail.ResponseStatus;
import uk.co.thebodyshop.integration.jaxb.store.customer.detail.StatusTypeEnum;


@Endpoint
public class CustomerDetailEndpoint
{
	private static final Logger LOG = Logger.getLogger(CustomerDetailEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/customerDetailSchema";

	@PayloadRoot(localPart = "CustomerDetailRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload CustomerDetailResponse customerDetail(final @RequestPayload CustomerDetailRequest request)
	{
		final CustomerDetailResponse response = new CustomerDetailResponse();

		return response;
	}

	private ResponseStatus createFailureResponse()
	{
		final ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.setStatus(StatusTypeEnum.FAILURE);
		return responseStatus;
	}

	private ResponseStatus createSuccuessResponse()
	{
		final ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.setStatus(StatusTypeEnum.SUCCESS);
		return responseStatus;
	}
}
