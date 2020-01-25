/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.endpoints;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import uk.co.thebodyshop.integration.jaxb.customer.identifier.CustomerIdentifierUpdateRequest;
import uk.co.thebodyshop.integration.jaxb.customer.identifier.CustomerIdentifierUpdateResponse;


@Endpoint
public class CustomerIdentifierUpdateEndpoint
{
	private static final Logger LOG = Logger.getLogger(CommunicationPreferenceEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/CustomerIdentifierUpdateSchema";

	@PayloadRoot(localPart = "CustomerIdentifierUpdateRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload CustomerIdentifierUpdateResponse updateCustomerSiebelID(@RequestPayload
	final CustomerIdentifierUpdateRequest request)
	{
		final CustomerIdentifierUpdateResponse response = new CustomerIdentifierUpdateResponse();

		return response;
	}

	private boolean validateParameters(final String customerID, final String customerIdentifier, final String identifierType,
			final CustomerIdentifierUpdateResponse response)
	{
		boolean validationPass = true;
		if (StringUtils.isEmpty(customerID))
		{
			response.setStatusCode("Customer ID is empty");
			validationPass = false;
		}

		if (StringUtils.isEmpty(customerIdentifier))
		{
			response.setStatusCode("Customer Identifier is empty");
			validationPass = false;
		}

		if (StringUtils.isEmpty(identifierType))
		{
			response.setStatusCode("Customer Identifier type is empty");
			validationPass = false;
		}
		return validationPass;
	}
}
