/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.cart.strategies;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartRestorationStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

/**
 * @author Marcin
 */
public class TbsCommerceCartRestorationStrategy extends DefaultCommerceCartRestorationStrategy
{
	@Override
	protected void clearPaymentTransactionsOnCart(final CartModel cartModel)
	{
		getModelService().removeAll(getTransactionsForRemoval(cartModel));
		// cartModel.setPaymentTransactions(Collections.EMPTY_LIST);
	}

	private List<PaymentTransactionModel> getTransactionsForRemoval(final CartModel cartModel)
	{
		final List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();
		boolean hasGiftCardApplied = false;
		if (null != cartModel.getAmountGiftCards() && cartModel.getAmountGiftCards() > 0)
		{
			hasGiftCardApplied = true;
		}
		if (!hasGiftCardApplied)
		{
			return cartModel.getPaymentTransactions();
		}
		else
		{
			for (final PaymentTransactionModel paymentTransaction : cartModel.getPaymentTransactions())
			{
				if (StringUtils.isEmpty(paymentTransaction.getPaymentProvider()) || !"Gift Card".equalsIgnoreCase(paymentTransaction.getPaymentProvider().trim()))
				{
					paymentTransactions.add(paymentTransaction);
				}
			}
		}
		return paymentTransactions;
	}
}
