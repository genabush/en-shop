/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPricePopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.loyalty.points.calculation.strategies.LoyaltyPointsCalculationStrategy;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.strategies.impl.PricePerMetricCalculationStrategy;

/**
 * @author Krishna
 */
public class TbsProductPricePopulator extends ProductPricePopulator<ProductModel, ProductData>
{
	private final BaseStoreService baseStoreService;
	private final PricePerMetricCalculationStrategy pricePerMetricCalculationStrategy;
	private final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy;

	@Override
	public void populate(final ProductModel source, final ProductData target) throws ConversionException
	{
		super.populate(source, target);

		if (source instanceof TbsVariantProductModel && null != target.getPrice())
		{
			final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();

			if (baseStore != null)
			{
				final TbsVariantProductModel variantProduct = (TbsVariantProductModel) source;
				final PriceData price = target.getPrice();

				price.setPricePerMetric(getPricePerMetricCalculationStrategy().calculatePricePerMetric(variantProduct, baseStore, price.getValue()));
				price.setLoyaltyPoints(getLoyaltyPointsCalculationStrategy().calculateLoyaltyPoints(variantProduct, baseStore, price.getValue()));
			}
		}
	}

	public TbsProductPricePopulator(final BaseStoreService baseStoreService, final PricePerMetricCalculationStrategy pricePerMetricCalculationStrategy, final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy)
	{
		this.baseStoreService = baseStoreService;
		this.pricePerMetricCalculationStrategy = pricePerMetricCalculationStrategy;
		this.loyaltyPointsCalculationStrategy = loyaltyPointsCalculationStrategy;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected PricePerMetricCalculationStrategy getPricePerMetricCalculationStrategy()
	{
		return pricePerMetricCalculationStrategy;
	}

	protected LoyaltyPointsCalculationStrategy getLoyaltyPointsCalculationStrategy()
	{
		return loyaltyPointsCalculationStrategy;
	}
}
