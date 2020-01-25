/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import com.adyen.model.checkout.PaymentsResponse;
import com.adyen.v6.model.NotificationItemModel;
import com.adyen.v6.service.AdyenTransactionService;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;


/**
 *
 */
public interface TbsAdyenTransactionService extends AdyenTransactionService
{
	/**
	 * Stores the authorization pending transactions for an order
	 */
	PaymentTransactionModel markOrderAsAuthorizationPending(AbstractOrderModel abstractOrderModel, String merchantTransactionCode,
			String pspReference);

	/**
	 * Creates a PaymentTransactionEntryModel with type=PAYMENT_REDIRECT
	 */
	void createPaymentRedirectTransaction(final AbstractOrderModel abstractOrderModel, final PaymentsResponse paymentsresponse);

	/**
	 * Returns latest PaymentTransactionModel with type=PAYMENT_REDIRECT
	 */
	PaymentTransactionModel getLatestRedirectPaymentTransaction(final AbstractOrderModel abstractOrderModel);

	/**
	 * Returns a PaymentTransactionModel with type=PAYMENT_REDIRECT and a matching md attribute
	 */
	PaymentTransactionModel getPaymentTransactionForMd(final AbstractOrderModel abstractOrderModel, final String md);

	/**
	 * Updates the status of the payment redirect transaction in case of failure
	 *
	 * @param paymentRedirectTransaction
	 */
	void handlePaymentRedirectFailure(final PaymentTransactionModel paymentRedirectTransaction);

	/**
	 * Checks is specific payment transaction was successfully authorized
	 */
	boolean isNotAuthorizedTransaction(final PaymentTransactionModel paymentTransaction);

	/**
	 * Adds authorized transaction entry to the specifc payment transaction
	 */
	PaymentTransactionEntryModel createAuthorizedTransactionFromNotification(PaymentTransactionModel paymentTransaction, NotificationItemModel notificationItemModel);
}
