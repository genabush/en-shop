/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.daos;

import java.util.List;

import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;

/**
 * @author Marcin
 */
public interface PaymentRedirectInfoDao
{
	/**
	 * Finds most recent @PaymentRedirectInfoModel for specific userId and cartId
	 *
	 * @param userId
	 *          - unique user identifier
	 * @param cartId
	 *          - unique cart identifier
	 * @return @PaymentRedirectInfoModel
	 */
	PaymentRedirectInfoModel findPaymentRedirectInfoForUserAndCart(final String userId, final String cartId);

	/**
	 * Finds all @PaymentRedirectInfoModel for specific userId and cartId
	 *
	 * @param userId
	 *          - unique user identifier
	 * @param cartId
	 *          - unique cart identifier
	 * @return @PaymentRedirectInfoModel
	 */
	List<PaymentRedirectInfoModel> findAllPaymentRedirectInfosForUserAndCart(final String userId, final String cartId);
}
