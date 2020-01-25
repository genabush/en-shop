/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helpers.impl;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.factories.TbsPriceDataFactory;
import uk.co.thebodyshop.core.helpers.VariantDataHelper;
import uk.co.thebodyshop.core.loyalty.points.calculation.strategies.LoyaltyPointsCalculationStrategy;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.strategies.impl.PricePerMetricCalculationStrategy;
import uk.co.thebodyshop.core.variant.solr.data.StockData;

/**
 * @author Marcin
 */
public class DefaultVariantDataHelper implements VariantDataHelper
{
	private static final Logger LOG = Logger.getLogger(DefaultVariantDataHelper.class);

	private static final String CANNOT_CONVERT_SIZE_VALUE_TO_A_DOUBLE = "Cannot convert size value to a Double :: ";

	private final CommerceStockService commerceStockService;

	private final CommercePriceService commercePriceService;

	private final PricePerMetricCalculationStrategy pricePerMetricCalculationStrategy;

	private final TbsPriceDataFactory priceDataFactory;

	private final CommonI18NService commonI18nService;

	private final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy;

	@Override
	public String getFormattedSizeValue(final TbsVariantProductModel variant)
	{
		if (StringUtils.isNotEmpty(variant.getSize()))
		{
			final StringBuilder variantSize = new StringBuilder(getFormattedSizeValue(variant.getSize()));
			if (Objects.nonNull(variant.getUnit()) && StringUtils.isNotEmpty(variant.getUnit().getCode()))
			{
				variantSize.append(variant.getUnit().getCode().toLowerCase());
			}
			return variantSize.toString();
		}
		return StringUtils.EMPTY;
	}

	private String getFormattedSizeValue(final String size)
	{
		try
		{
			final BigDecimal stripedVal = new BigDecimal(size).stripTrailingZeros();
			return stripedVal.toPlainString();
		}
		catch (final Exception ex)
		{
			LOG.error("Invalid value found for size attribute :: " + size);
			return size;
		}
	}

	@Override
	public PriceData getFormattedVariantPrice(final VariantProductModel variantModel)
	{
		final PriceInformation priceInformation = getCommercePriceService().getFromPriceForProduct(variantModel);

		if (Objects.nonNull(priceInformation))
		{
			return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceInformation.getPriceValue().getValue()), priceInformation.getPriceValue().getCurrencyIso());
		}

		return null;
	}

	@Override
	public StockData getVariantStockStatus(final VariantProductModel variantModel, final BaseStoreModel baseStore)
	{
		final StockData stockData = new StockData();
		if (Objects.nonNull(baseStore) && getCommerceStockService().isStockSystemEnabled(baseStore))
		{
			final StockLevelStatus stockLevelStatus = getCommerceStockService().getStockLevelStatusForProductAndBaseStore(variantModel, baseStore);
			if (stockLevelStatus != null)
			{
				stockData.setStockLevelStatus(stockLevelStatus.getCode());
			}
			else
			{
				stockData.setStockLevelStatus(StockLevelStatus.OUTOFSTOCK.getCode());
			}
		}
		else
		{
			stockData.setStockLevelStatus(StockLevelStatus.INSTOCK.getCode());
		}
		return stockData;
	}

	@Override
	public String getPricePerMetric(final VariantProductModel variantModel, final BaseStoreModel baseStore, final BigDecimal price)
	{
		return getPricePerMetricCalculationStrategy().calculatePricePerMetric(variantModel, baseStore, price);
	}

	@Override
	public Double getSizeForSort(final TbsVariantProductModel variantModel)
	{
		try
		{
			return Double.valueOf(variantModel.getSize());
		}
		catch (final NumberFormatException nfe)
		{
			LOG.error(CANNOT_CONVERT_SIZE_VALUE_TO_A_DOUBLE + variantModel.getSize());
		}
		return Double.valueOf(0.00);
	}

	@Override
	public Integer getLoyaltyPoints(final TbsVariantProductModel variantModel, final BaseStoreModel baseStore, final BigDecimal price)
	{
		return getLoyaltyPointsCalculationStrategy().calculateLoyaltyPoints(variantModel, baseStore, price);
	}

	public DefaultVariantDataHelper(final CommerceStockService commerceStockService, final CommercePriceService commercePriceService, final PricePerMetricCalculationStrategy pricePerMetricCalculationStrategy, final TbsPriceDataFactory priceDataFactory,
			final CommonI18NService commonI18nService, final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy)
	{
		this.commerceStockService = commerceStockService;
		this.commercePriceService = commercePriceService;
		this.pricePerMetricCalculationStrategy = pricePerMetricCalculationStrategy;
		this.priceDataFactory = priceDataFactory;
		this.commonI18nService = commonI18nService;
		this.loyaltyPointsCalculationStrategy = loyaltyPointsCalculationStrategy;
	}

	/**
	 * @return the commerceStockService
	 */
	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	/**
	 * @return the commercePriceService
	 */
	protected CommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	/**
	 * @return the pricePerMetricCalculationStrategy
	 */
	protected PricePerMetricCalculationStrategy getPricePerMetricCalculationStrategy()
	{
		return pricePerMetricCalculationStrategy;
	}

	/**
	 * @return the priceDataFactory
	 */
	protected TbsPriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * @return the commonI18nService
	 */
	protected CommonI18NService getCommonI18nService()
	{
		return commonI18nService;
	}

	/**
	 * @return the loyaltyPointsCalculationStrategy
	 */
	protected LoyaltyPointsCalculationStrategy getLoyaltyPointsCalculationStrategy()
	{
		return loyaltyPointsCalculationStrategy;
	}
}
