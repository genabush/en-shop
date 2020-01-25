/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.impl;

import java.util.Map;
import java.util.Set;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.TaxValue;

/**
 * @author Jagadeesh
 */
public class DefaultTbsCalculationService extends DefaultCalculationService
{

	private final CommonI18NService commonI18NService;

	private final OrderRequiresCalculationStrategy orderRequiresCalculationStrategy;

	public DefaultTbsCalculationService(final CommonI18NService commonI18NService, final OrderRequiresCalculationStrategy orderRequiresCalculationStrategy)
	{
		this.commonI18NService = commonI18NService;
		this.orderRequiresCalculationStrategy = orderRequiresCalculationStrategy;
	}

	@Override
	protected void calculateTotals(final AbstractOrderModel order, final boolean recalculate, final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap) throws CalculationException
	{
		if (recalculate || getOrderRequiresCalculationStrategy().requiresCalculation(order))
		{
			final CurrencyModel curr = order.getCurrency();
			final int digits = curr.getDigits().intValue();
			// subtotal
			final double subtotal = order.getSubtotal().doubleValue();
			// discounts

			final double totalDiscounts = calculateDiscountValues(order, recalculate);
			final double roundedTotalDiscounts = getCommonI18NService().roundCurrency(totalDiscounts, digits);
			order.setTotalDiscounts(Double.valueOf(roundedTotalDiscounts));
			// set total
			final double total = subtotal + order.getPaymentCost().doubleValue() + order.getDeliveryCost().doubleValue() + order.getGiftWrapPrice().doubleValue() - roundedTotalDiscounts;
			final double totalRounded = getCommonI18NService().roundCurrency(total, digits);
			order.setTotalPrice(Double.valueOf(totalRounded));
			// taxes
			final double totalTaxes = calculateTotalTaxValues(//
					order, recalculate, //
					digits, //
					getTaxCorrectionFactor(taxValueMap, subtotal, total, order), //
					taxValueMap);//
			final double totalRoundedTaxes = getCommonI18NService().roundCurrency(totalTaxes, digits);
			order.setTotalTax(Double.valueOf(totalRoundedTaxes));
			setCalculatedStatus(order);
			saveOrder(order);
		}
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected OrderRequiresCalculationStrategy getOrderRequiresCalculationStrategy()
	{
		return orderRequiresCalculationStrategy;
	}
}
