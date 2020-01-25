/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.calculation.strategies.impl;

import java.math.BigDecimal;
import java.util.Objects;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import uk.co.thebodyshop.core.calculation.strategies.OutstandingAmountCalculationStrategy;

/**
 * @author Marcin
 */
public class DefaultOutstandingAmountCalculationStrategy implements OutstandingAmountCalculationStrategy
{

	@Override
	public BigDecimal getOutstandingAmount(final AbstractOrderModel abstractOrderModel)
	{
		if (Objects.nonNull(abstractOrderModel))
		{
			final BigDecimal appliedLoyaltyVouchersAmount = BigDecimal.valueOf(abstractOrderModel.getLoyaltyVoucherDiscount());
			final BigDecimal appliedGiftcardsAmount = BigDecimal.valueOf(abstractOrderModel.getAmountGiftCards());
			final BigDecimal outstandingAmount = getTotalWithTax(abstractOrderModel).subtract(appliedLoyaltyVouchersAmount.add(appliedGiftcardsAmount)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			return outstandingAmount;
		}
		return BigDecimal.ZERO;
	}

	protected BigDecimal getTotalWithTax(final AbstractOrderModel abstractOrderModel)
	{
		if (abstractOrderModel.getTotalPrice() == null)
		{
			return BigDecimal.ZERO;
		}

		BigDecimal totalPrice = BigDecimal.valueOf(abstractOrderModel.getTotalPrice().doubleValue());

		// Add the taxes to the total price if the cart is net; if the total was null taxes should be null as well
		if (Boolean.TRUE.equals(abstractOrderModel.getNet()) && totalPrice.compareTo(BigDecimal.ZERO) != 0 && abstractOrderModel.getTotalTax() != null)
		{
			totalPrice = totalPrice.add(BigDecimal.valueOf(abstractOrderModel.getTotalTax().doubleValue()));
		}
		return totalPrice;
	}
}
