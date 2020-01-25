/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.mocks.repsonse.predicates;

import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import uk.co.thebodyshop.integration.jaxb.order.AddressType;

/**
 * @author vasanthramprakasam
 */
@Component("addressFailPredicate")
public class PlaceOrderAddressFailPredicate implements Predicate<AddressType>
{

	 private static final String FAILURE_STRING = "fail";

	 @Override
	 public boolean test(AddressType addressType)
	 {
			final String firstName = addressType.getFirstName();
			final String lastName = addressType.getLastName();
			return (StringUtils.isNotBlank(firstName) && StringUtils.containsIgnoreCase(firstName,FAILURE_STRING))
					|| (StringUtils.isNotBlank(lastName) && StringUtils.containsIgnoreCase(lastName,FAILURE_STRING));
	 }
}
