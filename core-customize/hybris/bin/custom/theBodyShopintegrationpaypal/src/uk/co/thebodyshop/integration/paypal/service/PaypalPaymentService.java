/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.service;

import java.util.Optional;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.store.BaseStoreModel;

/**
 * @author vasanthramprakasam
 */
public interface PaypalPaymentService
{
	 /**
		* creates order on paypal
		* @param cartData
		* @return the order id created at paypal
		*/
	Optional<String> createOrder(CartData cartData);

	 /**
		* authorize order on paypal
		* @param orderId
		* @return authId
		*/
	 Optional<String> authorize(String orderId);

	/**
	 * authorize for void on paypal
	 * @param authorizationId,baseStore
	 * @return void response status code
	 */
	Optional<Integer> voidAuthorize(String authorizationId, BaseStoreModel baseStore);
}
