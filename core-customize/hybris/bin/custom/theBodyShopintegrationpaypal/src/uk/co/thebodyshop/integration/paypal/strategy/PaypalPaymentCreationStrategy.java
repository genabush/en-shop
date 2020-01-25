/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.paypal.strategy;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import uk.co.thebodyshop.integration.paypal.model.PaypalPaymentInfoModel;

/**
 * @author vasanthramprakasam
 */
public interface PaypalPaymentCreationStrategy
{
	 /**
		* creates a payment info for paypal orders
 		* @param authId
		* @param orderModel
		* @return PaypalPaymentInfoModel created
		*/
	PaypalPaymentInfoModel createPaypalPaymentInfo(String authId, AbstractOrderModel orderModel);

	 /**
		* creates payment transaction for paypal auth id and order
		* @param authId
		* @param orderModel
		* @return created PaymentTransactionModel
		*/
	PaymentTransactionModel createPaymentTransaction(String authId, AbstractOrderModel orderModel);

	 /**
		* creates a payment transaction entry for paypal authorization
		* @param authId
		* @param paymentTransactionModel
		* @param orderModel
		* @return created payment transaction entry
		*/
	PaymentTransactionEntryModel createAuthorizeEntry(String authId,PaymentTransactionModel paymentTransactionModel,AbstractOrderModel orderModel);
}
