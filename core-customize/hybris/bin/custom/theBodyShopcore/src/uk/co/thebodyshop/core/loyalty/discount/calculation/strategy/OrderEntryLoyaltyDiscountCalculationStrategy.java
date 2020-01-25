/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.loyalty.discount.calculation.strategy;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Krishna
 */
public class OrderEntryLoyaltyDiscountCalculationStrategy
{
	private final ModelService modelService;

	public void calculateOrderEntryLoyaltyDiscount(final CartModel cart)
	{
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		if (CollectionUtils.isNotEmpty(entries))
		{
			final double totalAmount = getCalculatedTotalOrderAmount(cart);
			final CurrencyModel currency = cart.getCurrency();
			final int digits = currency.getDigits().intValue();
			final Double loyaltyDiscount = cart.getLoyaltyVoucherDiscount();

			for (final AbstractOrderEntryModel entry : entries)
			{
				final ProductModel product = entry.getProduct();
				if (product instanceof TbsVariantProductModel)
				{
					if (BooleanUtils.isTrue(((TbsVariantProductModel) product).getLoyalty()))
					{
						final double apportionValue = CoreAlgorithms.round(loyaltyDiscount / totalAmount * entry.getTotalPrice(), digits);
						entry.setLoyaltyDiscount(apportionValue);
						getModelService().save(entry);
					}
				}
			}
		}
	}

	private double getCalculatedTotalOrderAmount(final AbstractOrderModel order)
	{
		final CurrencyModel currency = order.getCurrency();
		final int digits = currency.getDigits().intValue();
		Double subTotal = 0.0D;
		for (final AbstractOrderEntryModel entry : order.getEntries())
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
		return CoreAlgorithms.round(subTotal.doubleValue(), digits);
	}

	public OrderEntryLoyaltyDiscountCalculationStrategy(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
