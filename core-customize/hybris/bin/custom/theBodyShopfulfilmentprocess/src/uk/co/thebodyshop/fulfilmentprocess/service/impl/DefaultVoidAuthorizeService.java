/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import uk.co.thebodyshop.fulfilmentprocess.service.VoidAuthorizeService;
import uk.co.thebodyshop.payment.services.impl.DefaultTbsPaymentService;

/**
 * @author Balakrishna
 **/
public class DefaultVoidAuthorizeService implements VoidAuthorizeService {

	private static final String ADYEN_PAYMENT_PROVIDER = "Adyen";

	private static final String GIFT_CARD_PAYMENT_PROVIDER = "Gift Card";

	private static final Logger LOG= LoggerFactory.getLogger(DefaultVoidAuthorizeService.class);

	private DefaultTbsPaymentService tbsPaymentService;

	@Override
	public boolean voidAuthorize(final OrderModel order) {
		boolean isVoided = false;
		final boolean voidStatusForGiftCardBefore = isAlreadyVoidAuthorize(order, GIFT_CARD_PAYMENT_PROVIDER);
		final boolean voidStatusForPrimaryPaymentBefore = isAlreadyVoidAuthorize(order, ADYEN_PAYMENT_PROVIDER);
		try
		{
			if (!voidStatusForGiftCardBefore)
			{
				getTbsPaymentService().reverseGftCardPayment(order);
			}
			if (!voidStatusForPrimaryPaymentBefore)
			{
				getTbsPaymentService().reverseAuthUnusedPrimaryPayment(order);
			}
			final boolean voidStatus = isAlreadyVoidAuthorize(order, GIFT_CARD_PAYMENT_PROVIDER) && isAlreadyVoidAuthorize(order, ADYEN_PAYMENT_PROVIDER);
			if (voidStatus && !order.getStatus().equals(OrderStatus.PROCESSING_ERROR))
			{
				isVoided = true;
			}
		}catch (final Exception e)
		{
			LOG.error("Unable to void the authorization :[{}]",e);
		}
		return isVoided;
	}

	private boolean isAlreadyVoidAuthorize(final OrderModel order, final String paymentProvider) {
		boolean hasSuccesfullVoid = true;
		for (final PaymentTransactionModel paymentTransactionModel : order.getPaymentTransactions()) {
			if (paymentProvider.equals(paymentTransactionModel.getPaymentProvider())) {
				hasSuccesfullVoid = paymentTransactionModel.getEntries().stream()
						.filter(paymentTransactionEntryModel -> (PaymentTransactionType.CANCEL == paymentTransactionEntryModel.getType()) && TransactionStatus.ACCEPTED.name().equalsIgnoreCase(paymentTransactionEntryModel.getTransactionStatus())).findAny()
						.isPresent();
				if (!hasSuccesfullVoid)
				{
					return false;
				}
			}
		}
		return hasSuccesfullVoid;
	}

	public DefaultTbsPaymentService getTbsPaymentService() {
		return tbsPaymentService;
	}

	public void setTbsPaymentService(final DefaultTbsPaymentService tbsPaymentService) {
		this.tbsPaymentService = tbsPaymentService;
	}
}
