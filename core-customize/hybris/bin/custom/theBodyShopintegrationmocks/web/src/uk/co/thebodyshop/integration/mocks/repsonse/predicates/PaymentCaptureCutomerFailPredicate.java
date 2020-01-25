/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.mocks.repsonse.predicates;

import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import uk.co.thebodyshop.integration.jaxb.order.Order;

/**
 * @author Marcin
 */
@Component("customerEmailFailPredicate")
public class PaymentCaptureCutomerFailPredicate implements Predicate<Order.Customer>
{
	private static final String FAILURE_STRING = "failCapture";

	@Override
	public boolean test(final Order.Customer customer)
	{
		if (null == customer)
		{
			return true;
		}
		final String customerEmail = customer.getEmailAddress();
		return (StringUtils.isNotBlank(customerEmail) && StringUtils.containsIgnoreCase(customerEmail, FAILURE_STRING));
	}
}
