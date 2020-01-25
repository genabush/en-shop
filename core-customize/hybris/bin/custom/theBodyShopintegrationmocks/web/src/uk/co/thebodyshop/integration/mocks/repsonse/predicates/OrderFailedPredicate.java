/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.mocks.repsonse.predicates;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercury.ws.v1.placeorder.PlaceOrder;

import uk.co.thebodyshop.integration.jaxb.order.AddressType;
import uk.co.thebodyshop.integration.jaxb.order.Order;

/**
 * @author vasanthramprakasam
 */
@Component("orderFailPredicate")
public class OrderFailedPredicate implements Predicate<PlaceOrder>
{

	 @Autowired
	 private Predicate<AddressType> addressFailPredicate;

	 @Override
	 public boolean test(PlaceOrder placeOrder)
	 {
			final Order request = placeOrder.getRequest();
			final AddressType billingAddress = request.getBillingAddress();
			final AddressType shippingAddress = request.getShippingAddress();
			return request != null
					&& ((billingAddress != null
					&& addressFailPredicate.test(billingAddress)) || (shippingAddress != null && addressFailPredicate.test(shippingAddress)));
	 }
}
