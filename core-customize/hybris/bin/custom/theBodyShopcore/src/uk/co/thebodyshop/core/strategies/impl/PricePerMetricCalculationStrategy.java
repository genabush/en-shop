/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.factories.TbsPriceDataFactory;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Krishna
 */
public class PricePerMetricCalculationStrategy
{
	private static final String FORWARD_SLASH = "/";
	private static final String SPACE = " ";

	private final CommercePriceService commercePriceService;
	private final TbsPriceDataFactory priceDataFactory;
	private final I18NService i18NService;
	private final CommonI18NService commonI18NService;
	private final CommerceCommonI18NService commerceCommonI18NService;

	public String calculatePricePerMetric(final ProductModel product, final BaseStoreModel baseStore, final BigDecimal price)
	{
		if (product instanceof TbsVariantProductModel && Boolean.TRUE.equals(baseStore.getPricePerMetricToggle()))
		{
			return calculateAndFormatPricePerMetric((TbsVariantProductModel) product, baseStore, price);
		}
		return StringUtils.EMPTY;
	}

	private String calculateAndFormatPricePerMetric(final TbsVariantProductModel productModel, final BaseStoreModel baseStore, final BigDecimal price)
	{
		final Double productPrice = Double.valueOf(price.doubleValue());
		final Double productSize = Double.valueOf(productModel.getSize());

		final Map<UnitModel, Double> unitTypeMap = baseStore.getMeasurementUnitTypeMap();
		if (null != unitTypeMap && null != unitTypeMap.get(productModel.getUnit()))
		{
			final Double baseUnitValue = unitTypeMap.get(productModel.getUnit());
			final CurrencyModel currency = getCommonI18NService().getCurrentCurrency();
			final Locale locale = getCommerceCommonI18NService().getLocaleForLanguage(getCommonI18NService().getCurrentLanguage());

			final NumberFormat currencyFormat = getPriceDataFactory().createCurrencyFormat(locale, currency);
			return currencyFormat.format((productPrice / productSize) * baseUnitValue) + FORWARD_SLASH + baseUnitValue.intValue() + SPACE + productModel.getUnit().getCode();
		}

		return StringUtils.EMPTY;
	}

	public PricePerMetricCalculationStrategy(final CommercePriceService commercePriceService, final TbsPriceDataFactory priceDataFactory, final I18NService i18NService, final CommonI18NService commonI18NService,
			final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commercePriceService = commercePriceService;
		this.priceDataFactory = priceDataFactory;
		this.i18NService = i18NService;
		this.commonI18NService = commonI18NService;
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected CommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	protected TbsPriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}
}
