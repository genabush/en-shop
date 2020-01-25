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

import uk.co.thebodyshop.integration.jaxb.store.customer.registration.CustomerRegistrationRequest;
import uk.co.thebodyshop.integration.jaxb.store.customer.registration.CustomerRegistrationResponse;


enum LybcUpdateResult
{
	OK, DIFFERENT_CUSTOMER, DIFFERENT_BASESITE, WRONG_PROGRAMME_ID, INVALID_CARD, MISSING_TERMS_CONDITIONS
}

enum StatusCode
{
	SUCCESS, FAILURE
}

@Endpoint
public class CustomerRegistrationEndpoint
{
	private static final String ANOTHER_USER_EXISTS_FOR_THE_EMAIL_ID = "Another user exists for the email Id";

	private static final Logger LOG = Logger.getLogger(CustomerRegistrationEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/customerRegistrationSchema";

	private static final String GENERIC_ERROR = "Generic Internal Error";

	private static final String ERROR_LYBCCARD_NOT_AVAILABLE = "No Loyalty Cards Availables";

	private static final String ERROR_LYBCCARD_ALREDY_CONNECTED_TO_A_CUSTOMER = "Card already assigned to different customer.";

	private static final String ERROR_LYBCCARD_INVALID_STATUS = "Card has an invalid status";

	private static final String SUCCESS_MESSAGE_UPDATE = "Customer Updated.";

	private static final String SUCCESS_MESSAGE_CREATION = "Customer Created.";

	private static final String COUNTRY_WITH_COUNTY_KEY = "countries.with.county";

	private static final String ERROR_LYBCCARD_PROGRAMME_ID = "ProgrammeID not valid";

	private static final String ERROR_LYBCCARD_TERMSCONDITION_EMPTY = "Terms and conditions empty";

	private static final String ERROR_INVALID_NAMES = "Found user by email but firstname or lastname don't match";

	private static final String ERROR_DUPLICATE_ADDRESS_ERROR = "An address with requested values already exists";

	private static final String ERROR_REGISTER_CUSTOMER_WITHOUT_CONTACT_ADDRESS = "Register Customer without contact address";

	@PayloadRoot(localPart = "CustomerRegistrationRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload CustomerRegistrationResponse customerRegistration(
			final @RequestPayload CustomerRegistrationRequest customerRegistrationRequest)
	{
		final CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();

		return customerRegistrationResponse;
	}
}
