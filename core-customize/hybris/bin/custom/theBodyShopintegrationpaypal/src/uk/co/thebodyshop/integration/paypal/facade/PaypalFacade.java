/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.facade;

import java.util.Optional;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;

import uk.co.thebodyshop.paypal.data.PaypalCreateOrderResponse;

/**
 * @author vasanthramprakasam
 */
public interface PaypalFacade
{
	 /**
		* create order on paypal
		* @param cartData
		* @return the response of order creation
		*/
	 PaypalCreateOrderResponse createOrder(CartData cartData);

	 /**
		* authorise order on paypal and place order
		* @param orderId
		* @return placed order data
		*/
	 Optional<OrderData> authorizePayment(String orderId);
}
