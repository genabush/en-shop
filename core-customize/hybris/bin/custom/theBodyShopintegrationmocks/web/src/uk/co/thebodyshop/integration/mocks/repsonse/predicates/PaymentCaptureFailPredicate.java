/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.mocks.repsonse.predicates;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercury.ws.v1.paymentcapture.PaymentCapture;

import uk.co.thebodyshop.integration.jaxb.order.Order;

/**
 * @author Marcin
 */
@Component("paymentCaptureFailPredicate")
public class PaymentCaptureFailPredicate implements Predicate<PaymentCapture>
{
	@Autowired
	private Predicate<Order.Customer> customerEmailFailPredicate;

	@Override
	public boolean test(final PaymentCapture paymentCapture)
	{
		final Order request = paymentCapture.getRequest();
		return request != null && ((request.getCustomer() != null && customerEmailFailPredicate.test(request.getCustomer())));
	}
}