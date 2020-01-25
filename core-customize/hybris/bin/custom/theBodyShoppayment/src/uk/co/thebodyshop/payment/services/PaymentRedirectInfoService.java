/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.services;

import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;

/**
 * @author Marcin
 */
public interface PaymentRedirectInfoService
{
	/**
	 * Gets @PaymentRedirectInfoModel for specific userId and cartId
	 *
	 * @param userId
	 *          - unique user identifier
	 * @param cartId
	 *          - unique cart identifier
	 * @return @PaymentRedirectInfoModel
	 */
	PaymentRedirectInfoModel getPaymentRedirectInfoForUserAndCart(final String userId, final String cartId);

	/**
	 * Removes all @PaymentRedirectInfoModel for specific userId and cartId
	 *
	 * @param userId
	 *          - unique user identifier
	 * @param cartId
	 *          - unique cart identifier
	 */
	void clearAllPaymentRedirectInfoForUserAndCart(final String userId, final String cartId);
}
