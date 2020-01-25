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

import uk.co.thebodyshop.integration.jaxb.customer.communicationpreference.CustomerCommunicationPreferenceRequest;
import uk.co.thebodyshop.integration.jaxb.customer.communicationpreference.CustomerCommunicationPreferenceResponse;


@Endpoint
public class CommunicationPreferenceEndpoint
{

	private static final Logger LOG = Logger.getLogger(CommunicationPreferenceEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/communicationPreferenceSchema";

	@PayloadRoot(localPart = "CustomerCommunicationPreferenceRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload CustomerCommunicationPreferenceResponse communicationPreferenceService(
			final @RequestPayload CustomerCommunicationPreferenceRequest communicationPreferenceRequest)
	{
		final CustomerCommunicationPreferenceResponse communicationPreferenceResponse = new CustomerCommunicationPreferenceResponse();

		return communicationPreferenceResponse;
	}
}
