/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.loyalty.points.calculation.strategies;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.DiscountValue;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Krishna
 */
public class LoyaltyPointsCalculationStrategy
{
	public Integer calculateLoyaltyPoints(final TbsVariantProductModel product, final BaseStoreModel baseStore, final BigDecimal bigDecimal)
	{
		Integer loyaltyPoints = 0;
		if (Boolean.TRUE.equals(baseStore.getLybcEnabled()) && Boolean.TRUE.equals(product.getLoyalty()) && bigDecimal != null)
		{
			final Double productPrice = Double.valueOf(bigDecimal.doubleValue());
			final Double loyaltyCurrencyUnit = baseStore.getLoyaltyCurrencyUnit();
			final Double loyaltyPointsRate = baseStore.getLoyaltyPointsRate();
			final Double loyaltyPointsMultiplier = baseStore.getLoyaltyPointsMultiplier();
			final Double points = (((productPrice / loyaltyCurrencyUnit) * loyaltyPointsRate) * (loyaltyPointsMultiplier));
			loyaltyPoints = points == null ? 0 : Integer.valueOf((int) Math.round(points));
		}
		return loyaltyPoints;
	}
	
	public Integer calculate(final AbstractOrderModel cart, final BaseStoreModel baseStore)
	{
		Integer loyaltyPoints = 0;
		final double totalPrice = getTotalOrderAmount(cart);
		if(totalPrice > 0.0)
		{
			if (Boolean.TRUE.equals(baseStore.getLybcEnabled()))
			{
				final Double loyaltyCurrencyUnit = baseStore.getLoyaltyCurrencyUnit();
				final Double loyaltyPointsRate = baseStore.getLoyaltyPointsRate();
				final Double loyaltyPointsMultiplier = baseStore.getLoyaltyPointsMultiplier();
				final Double points = (((totalPrice / loyaltyCurrencyUnit) * loyaltyPointsRate) * (loyaltyPointsMultiplier));
				loyaltyPoints = points == null ? 0 : Integer.valueOf((int) Math.round(points));
			}
		}
		return loyaltyPoints;
	}
	
	private double getTotalOrderAmount(final AbstractOrderModel order)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		final CurrencyModel curr = order.getCurrency();
		final int digits = curr.getDigits().intValue();
		Double subTotal = 0.0D;
		if (CollectionUtils.isNotEmpty(entries))
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final ProductModel product = entry.getProduct();
				if (product instanceof TbsVariantProductModel)
				{
					if (BooleanUtils.isTrue(((TbsVariantProductModel) product).getLoyalty()))
					{
						subTotal = subTotal + entry.getTotalPrice();
					}
				}
			}
		}
		double totalDiscounts = 0.0D;
		final List<DiscountValue> globalDiscountValues = order.getGlobalDiscountValues();
		// global discount total
		if (CollectionUtils.isNotEmpty(globalDiscountValues))
		{
			for (final DiscountValue discount : globalDiscountValues)
			{
				totalDiscounts = totalDiscounts + discount.getValue();
			}
		}
		final double totalPrice = CoreAlgorithms.round(subTotal.doubleValue() - totalDiscounts, digits);
		return totalPrice;
	}
}
