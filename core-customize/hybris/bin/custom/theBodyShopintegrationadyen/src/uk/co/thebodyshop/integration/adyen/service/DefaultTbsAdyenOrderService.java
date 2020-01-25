/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;

import org.apache.log4j.Logger;

import com.adyen.model.checkout.PaymentsResponse;
import com.adyen.v6.service.DefaultAdyenOrderService;

/**
 * @author Marcin
 */
public class DefaultTbsAdyenOrderService extends DefaultAdyenOrderService
{
	private static final Logger LOG = Logger.getLogger(DefaultTbsAdyenOrderService.class);

	@Override
	public void updateOrderFromPaymentsResponse(final OrderModel order, final PaymentsResponse paymentsResponse)
	{
		if (order == null)
		{
			LOG.error("Order is null");
			return;
		}

		final PaymentInfoModel paymentInfo = order.getPaymentInfo();
		if (null != paymentsResponse.getPaymentMethod())
		{
			paymentInfo.setAdyenPaymentMethod(paymentsResponse.getPaymentMethod());
		}

		//Card specific data
		paymentInfo.setAdyenAuthCode(paymentsResponse.getAuthCode());
		paymentInfo.setAdyenAvsResult(paymentsResponse.getAvsResult());
		paymentInfo.setAdyenCardBin(paymentsResponse.getCardBin());
		paymentInfo.setAdyenCardHolder(paymentsResponse.getCardHolderName());
		paymentInfo.setAdyenCardSummary(paymentsResponse.getCardSummary());
		paymentInfo.setAdyenCardExpiry(paymentsResponse.getExpiryDate());
		paymentInfo.setAdyenThreeDOffered(paymentsResponse.get3DOffered());
		paymentInfo.setAdyenThreeDAuthenticated(paymentsResponse.get3DAuthenticated());

		//Boleto data
		paymentInfo.setAdyenBoletoUrl(paymentsResponse.getBoletoUrl());
		paymentInfo.setAdyenBoletoBarCodeReference(paymentsResponse.getBoletoBarCodeReference());
		paymentInfo.setAdyenBoletoDueDate(paymentsResponse.getBoletoDueDate());
		paymentInfo.setAdyenBoletoExpirationDate(paymentsResponse.getBoletoExpirationDate());

		//Multibanco data
		paymentInfo.setAdyenMultibancoEntity(paymentsResponse.getMultibancoEntity());
		paymentInfo.setAdyenMultibancoAmount(paymentsResponse.getMultibancoAmount());
		paymentInfo.setAdyenMultibancoDeadline(paymentsResponse.getMultibancoDeadline());
		paymentInfo.setAdyenMultibancoReference(paymentsResponse.getMultibancoReference());

		getModelService().save(paymentInfo);

		storeFraudReportFromPaymentsResponse(order, paymentsResponse);
	}
}
