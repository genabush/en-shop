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

import uk.co.thebodyshop.integration.jaxb.store.benefit.AuthBenefitRequest;
import uk.co.thebodyshop.integration.jaxb.store.benefit.AuthBenefitResponse;
import uk.co.thebodyshop.integration.jaxb.store.benefit.CaptureBenefitRequest;
import uk.co.thebodyshop.integration.jaxb.store.benefit.CaptureBenefitResponse;
import uk.co.thebodyshop.integration.jaxb.store.benefit.ResponseStatus;
import uk.co.thebodyshop.integration.jaxb.store.benefit.ReverseAuthBenefitRequest;
import uk.co.thebodyshop.integration.jaxb.store.benefit.ReverseAuthBenefitResponse;
import uk.co.thebodyshop.integration.jaxb.store.benefit.StatusTypeEnum;


@Endpoint
public class BenefitUpdateEndpoint
{
	private static final String TARGET_NAMESPACE = "http://thebodyshop/posBenefitSchema";

	private static final Logger LOG = Logger.getLogger(BenefitUpdateEndpoint.class);

	private ResponseStatus createFailureResponse(final String message)
	{
		final ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.setStatus(StatusTypeEnum.FAILURE);
		responseStatus.setStatusMessage(message);
		return responseStatus;
	}

	private ResponseStatus createSuccessResponse(final String message)
	{
		final ResponseStatus responseStatus = new ResponseStatus();
		responseStatus.setStatus(StatusTypeEnum.SUCCESS);
		responseStatus.setStatusMessage(message);
		return responseStatus;
	}

	@PayloadRoot(localPart = "AuthBenefitRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload AuthBenefitResponse authoriseBenefit(final @RequestPayload AuthBenefitRequest authBenefitRequest)
	{
		final AuthBenefitResponse response = new AuthBenefitResponse();

		return response;
	}

	@PayloadRoot(localPart = "CaptureBenefitRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload CaptureBenefitResponse captureBenefit(
			final @RequestPayload CaptureBenefitRequest captureBenefitRequest)
	{
		final CaptureBenefitResponse response = new CaptureBenefitResponse();

		return response;
	}

	@PayloadRoot(localPart = "ReverseAuthBenefitRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload ReverseAuthBenefitResponse reverseAuthoriseBenefit(
			final @RequestPayload ReverseAuthBenefitRequest reverseAuthBenefitRequest)
	{
		final ReverseAuthBenefitResponse response = new ReverseAuthBenefitResponse();

		return response;
	}
}
