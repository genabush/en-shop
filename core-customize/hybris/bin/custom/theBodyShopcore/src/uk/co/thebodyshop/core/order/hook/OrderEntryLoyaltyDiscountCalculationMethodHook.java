/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.hook;

import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import uk.co.thebodyshop.core.loyalty.discount.calculation.strategy.OrderEntryLoyaltyDiscountCalculationStrategy;

/**
 * @author Krishna
 */
public class OrderEntryLoyaltyDiscountCalculationMethodHook implements CommerceCartCalculationMethodHook
{
	private final OrderEntryLoyaltyDiscountCalculationStrategy orderEntryLoyaltyDiscountCalculationStrategy;

	@Override
	public void afterCalculate(final CommerceCartParameter parameter)
	{
		getOrderEntryLoyaltyDiscountCalculationStrategy().calculateOrderEntryLoyaltyDiscount(parameter.getCart());
	}

	@Override
	public void beforeCalculate(final CommerceCartParameter parameter)
	{
		// not implemented
	}

	public OrderEntryLoyaltyDiscountCalculationMethodHook(final OrderEntryLoyaltyDiscountCalculationStrategy orderEntryLoyaltyDiscountCalculationStrategy)
	{
		this.orderEntryLoyaltyDiscountCalculationStrategy = orderEntryLoyaltyDiscountCalculationStrategy;
	}

	public OrderEntryLoyaltyDiscountCalculationStrategy getOrderEntryLoyaltyDiscountCalculationStrategy()
	{
		return orderEntryLoyaltyDiscountCalculationStrategy;
	}
}
