/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.factories;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.enums.CurrencySymbolPosition;

/**
 * @author Krishna
 */
public class TbsPriceDataFactory extends DefaultPriceDataFactory
{
	private final ConcurrentMap<String, NumberFormat> currencyFormats = new ConcurrentHashMap<>();

	private static final String SPACE = " ";

	private final BaseStoreService baseStoreService;

	public TbsPriceDataFactory(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	protected NumberFormat createNumberFormat(final Locale locale, final CurrencyModel currency)
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();

		if (baseStore == null)
		{
			return super.createNumberFormat(locale, currency);
		}

		final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		final Map<String, CurrencySymbolPosition> positionMap = baseStore.getCurrencySymbolPositionMap();

		if (null != positionMap && null != positionMap.get(getCommonI18NService().getCurrentLanguage().getIsocode()))
		{
			final CurrencySymbolPosition position = positionMap.get(getCommonI18NService().getCurrentLanguage().getIsocode());
			if (CurrencySymbolPosition.AFTER_PRICE.equals(position))
			{
				currencyFormat.setPositivePrefix("");
				currencyFormat.setNegativePrefix("");
				currencyFormat.setPositiveSuffix(SPACE + currency.getSymbol());
				currencyFormat.setNegativeSuffix(SPACE + currency.getSymbol());
			}
			else if (CurrencySymbolPosition.BEFORE_PRICE.equals(position))
			{
				currencyFormat.setPositivePrefix(currency.getSymbol());
				currencyFormat.setNegativePrefix(currency.getSymbol());
				currencyFormat.setPositiveSuffix("");
				currencyFormat.setNegativeSuffix("");
			}
		}

		adjustDigits(currencyFormat, currency);
		adjustSymbol(currencyFormat, currency);

		return currencyFormat;
	}

	/*
	 * Explicitly override super implementation to make it public.
	 */
	@Override
	public NumberFormat createCurrencyFormat(final Locale locale, final CurrencyModel currency)
	{
		final String key = locale.getLanguage() + "_" + locale.getISO3Country() + "_" + currency.getIsocode();

		NumberFormat numberFormat = currencyFormats.get(key);
		if (numberFormat == null)
		{
			final NumberFormat currencyFormat = createNumberFormat(locale, currency);
			numberFormat = currencyFormats.putIfAbsent(key, currencyFormat);
			if (numberFormat == null)
			{
				numberFormat = currencyFormat;
			}
		}
		// don't allow multiple references
		return (NumberFormat) numberFormat.clone();
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}
}
