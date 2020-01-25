/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.services;

import java.util.List;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import uk.co.thebodyshop.integrations.svs.data.GiftCardCaptureData;

/**
 * @author Marcin
 */
public interface TbsPaymentService
{
	/**
	 * Checks if all consignments order entries were successfully shipped
	 *
	 * @param order
	 * @return
	 */
	boolean isFullOrderCapture(final OrderModel order);

	public GiftCardCaptureData captureSecondaryPayment(final OrderModel order, final double amountRemaining);

	/**
	 * Processes primary payment capture and returns a success flag
	 *
	 * @param amountRemaining
	 * @param order
	 * @return
	 */
	public boolean capturePrimaryPayment(double amountRemaining, final OrderModel order);

	/**
	 * Processes primary payment transactions and issues a capture
	 *
	 * @param order
	 *          - current order
	 * @param amountRemaining
	 *          - remaining amount for primary transaction
	 * @param paymentTransaction
	 *          - current payment transaction
	 * @return a @boolean flag to mark the capture as success or failure
	 */
	Double capturePrimaryPaymentTransaction(final OrderModel order, final double amountRemaining, final PaymentTransactionModel paymentTransaction);

	/**
	 * Performs payment capture based on the amount
	 *
	 * @param transaction
	 *          - payment transaction with authorisation entry
	 * @param amount
	 *          - calculated capture amount
	 * @return - @PaymentTransactionEntryModel created based on the capture response
	 */
	PaymentTransactionEntryModel capture(PaymentTransactionModel transaction, final double amount);

	/**
	 * Performs gift card capture based on the amount
	 *
	 * @param order
	 *          - current order
	 * @param amount
	 *          - calculated capture amount
	 * @return the remaining amount which needs to be captured using primary payment
	 */
	double captureGiftCardAmount(final OrderModel order, final double amount);

	/**
	 * Calculates the remaining capture amount after secondary payment capture return an error
	 *
	 * @param order
	 *          - current order
	 * @param amountRemaining
	 *          - full capture amount
	 * @return - the remaining amount which needs to be captured using primary payment
	 */
	double getRemainingAmoutAfterSecondaryPayments(final OrderModel order, double amountRemaining);

	/**
	 * Returns primary payment transactions with successful authorisation transaction entries
	 *
	 * @param paymentTransactions
	 *          - current order payment transactions
	 * @return @List<@PaymentTransactionModel> - primary payment transactions
	 */
	List<PaymentTransactionModel> getPrimaryAuthPaymentTransactions(final List<PaymentTransactionModel> paymentTransactions);


	/**
	 * Returns primary payment transactions with failed capture transaction entries
	 *
	 * @param paymentTransactions
	 *          - current order payment transactions
	 * @return @List<@PaymentTransactionModel> - primary payment transactions
	 */
	List<PaymentTransactionEntryModel> getPrimaryFailedCapturePaymentTransactionEntries(final List<PaymentTransactionModel> paymentTransactions);

	/**
	 * Reverses unused gift card transactions
	 * 
	 * @param order
	 */
	public void reverseGftCardPayment(final OrderModel order);

	/**
	 * Reverses unused primary payment transactions - this scenario might happen for partial shipments when the amount allocated on attached gift cards is >= calculated capture amount
	 *
	 * @param order
	 *          - current order
	 */
	void reverseAuthUnusedPrimaryPayment(final OrderModel order);

	/**
	 * Returns AUTHORIZATION PaymentTransactionEntryModel with status set to ACCEPTED
	 *
	 * @param paymentTransactions
	 *          - current order payment Transactions
	 * @return @PaymentTransactionEntryModel
	 */
	PaymentTransactionEntryModel getSuccesfullAuthorizationEntry(final List<PaymentTransactionEntryModel> paymentTransactions);
}
