/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.facades;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.io.IOException;
import java.util.HashMap;

import com.adyen.model.checkout.PaymentsResponse;
import com.adyen.service.exception.ApiException;
import com.adyen.v6.facades.AdyenCheckoutFacade;

import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;


/**
 *
 */
public interface TbsAdyenCheckoutFacade extends AdyenCheckoutFacade
{
	OrderData handleRedirectPayload(final String paymentData, HashMap<String, String> details);

	PaymentDetailsWsDTO addPaymentDetails(PaymentDetailsWsDTO paymentDetails, final boolean isAdyenPaymentRequired);

   OrderData handle3DResponse(final PaymentTransactionModel paymentRedirectTransaction, final String paRes, final String md) throws Exception;

   PaymentDetailsListWsDTO getPaymentDetails() throws IOException, ApiException;

   OrderData getPaymentRedirectInfo(final PaymentsResponse paymentsResponse);

   OrderData handlePaymentRedirectResponse(final CartData cartData, final PaymentRedirectInfoModel paymentRedirectInfoModel) throws Exception;
}
