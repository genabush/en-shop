/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.cart.populators;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.commercefacades.order.converters.populator.ExtendedCartPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;

import uk.co.thebodyshop.core.calculation.strategies.OutstandingAmountCalculationStrategy;
import uk.co.thebodyshop.core.model.ServiceProductModel;

/**
 * @author Marcin
 */
public class TbsCartPopulator extends ExtendedCartPopulator
{
	private final OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy;

	private final CommercePriceService commercePriceService;

	private final PriceDataFactory priceDataFactory;

	public TbsCartPopulator(final OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy, final CommercePriceService commercePriceService, final PriceDataFactory priceDataFactory)
	{
		this.outstandingAmountCalculationStrategy = outstandingAmountCalculationStrategy;
		this.commercePriceService = commercePriceService;
		this.priceDataFactory = priceDataFactory;
	}

	@Override
	protected void addTotals(final AbstractOrderModel source, final AbstractOrderData target)
	{
		super.addTotals(source, target);
		// total gift card amount
		BigDecimal outstandingAmount = getOutstandingAmountCalculationStrategy().getOutstandingAmount(source);
		if (outstandingAmount.compareTo(BigDecimal.ZERO) < 0)
		{
			outstandingAmount = BigDecimal.ZERO;
		}
		target.setOutstandingAmount(createPrice(source, outstandingAmount.doubleValue()));
		if (BigDecimal.valueOf(outstandingAmount.doubleValue()).compareTo(BigDecimal.ZERO) == 0)
		{
			target.setHasOutstandingAmount(false);
		}
		else
		{
			target.setHasOutstandingAmount(true);
		}
		target.setEligibleForGiftWrap(source.getEligibleForGiftWrap());
		if (Objects.nonNull(source.getStore()))
		{
			if (Objects.nonNull(source.getStore().getGiftWrapProduct()))
			{
				final ProductModel product = source.getStore().getGiftWrapProduct();
				final PriceInformation info = getCommercePriceService().getWebPriceForProduct(product);
				if (info != null)
				{
					final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(info.getPriceValue().getValue()), info.getPriceValue().getCurrencyIso());
					target.setGiftWrapPrice(priceData);
				}
			}
			target.setEligibleForGiftMessage(source.getStore().getEligibleForGiftMessage() && CollectionUtils.isNotEmpty(source.getEntries()) && source.getEntries().size() > 0);
		}
		if (StringUtils.isNotEmpty(source.getGiftMessageName()) && StringUtils.isNotEmpty(source.getGiftMessage()))
		{
			target.setGiftMessage(source.getGiftMessage());
			target.setGiftMessageName(source.getGiftMessageName());
			target.setGiftMessageSenderName(source.getGiftMessageSenderName());
		}
		if (Objects.nonNull(source.getGiftWrapProduct()) && source.getGiftWrapProduct() instanceof ServiceProductModel)
		{
			target.setGiftWrapApplied(Boolean.TRUE);
		}
		target.setTotalPriceWithOutDeliveryCost(createPrice(source, source.getTotalPrice() - source.getDeliveryCost()));
	}

	protected OutstandingAmountCalculationStrategy getOutstandingAmountCalculationStrategy()
	{
		return outstandingAmountCalculationStrategy;
	}

	protected CommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Override
	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}
}
