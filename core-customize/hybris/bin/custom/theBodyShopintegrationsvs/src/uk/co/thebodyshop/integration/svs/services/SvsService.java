/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.services;

import com.svs.svsxml.beans.BalanceInquiryResponse;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import uk.co.thebodyshop.integration.svs.model.GiftCardModel;

public interface SvsService
{
	public static final String ERROR = "00";

	public static final String APPROVED = "01";

	public static final String HOST_UNAVAILABLE = "15";

	public static final String INSUFFICIENT_FUNDS = "05";

	String authorise(final String giftCardNumber, final String pinNumber, final CartModel cartModel, final double requestedAmount, final String currencyIsocode);

	boolean capture(final GiftCardModel giftCard, final AbstractOrderModel order);

	boolean reverseAuthorise(final GiftCardModel giftCard, final AbstractOrderModel order);

	BalanceInquiryResponse checkBalance(final String giftCardNumber, final String pinNumber, String cartCode, final String currencyIsocode);

	boolean capture(final GiftCardModel giftCard, final AbstractOrderModel order, final double amount);

	PaymentTransactionModel getGiftCardTransaction(final AbstractOrderModel order, final String giftCardNumber);

	PaymentTransactionEntryModel getAuthTransactionEntry(final PaymentTransactionModel paymentTransaction);
}
