/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.factories.TbsPriceDataFactory;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.strategies.impl.PricePerMetricCalculationStrategy;

/**
 * @author Krishna
 */
@UnitTest
public class PricePerMetricCalculationStrategyTest
{
	@InjectMocks
	private PricePerMetricCalculationStrategy strategy;

	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private TbsPriceDataFactory priceDataFactory;

	@Mock
	private TbsBaseProductModel baseProduct;

	@Mock
	private TbsVariantProductModel variantProduct;

	@Mock
	private BaseStoreModel baseStore;

	@Mock
	private UnitModel unit;

	@Mock
	private CurrencyModel currency;

	@Mock
	private LanguageModel language;

	private Map<UnitModel, Double> measurementMap;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		measurementMap = new HashMap<>();
		measurementMap.put(unit, 100.0);

		final Locale english = Locale.ENGLISH;
		final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(english);
		final DecimalFormatSymbols symbols = currencyFormat.getDecimalFormatSymbols(); // does cloning
		symbols.setCurrencySymbol("£");
		currencyFormat.setDecimalFormatSymbols(symbols);

		when(baseStore.getPricePerMetricToggle()).thenReturn(Boolean.TRUE);
		when(variantProduct.getUnit()).thenReturn(unit);
		when(baseStore.getMeasurementUnitTypeMap()).thenReturn(measurementMap);
		when(commonI18NService.getCurrentCurrency()).thenReturn(currency);
		when(commonI18NService.getCurrentLanguage()).thenReturn(language);
		when(commerceCommonI18NService.getLocaleForLanguage(language)).thenReturn(english);
		when(priceDataFactory.createCurrencyFormat(english, currency)).thenReturn(currencyFormat);
		when(unit.getCode()).thenReturn("ml");
		when(variantProduct.getSize()).thenReturn("200.0");
	}

	@Test
	public void testWhenProductIsNotAVariant()
	{
		final String pricePerMetric = strategy.calculatePricePerMetric(baseProduct, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(StringUtils.EMPTY, pricePerMetric);
	}

	@Test
	public void testWhenPricePerMetricTurnedOff()
	{
		when(baseStore.getPricePerMetricToggle()).thenReturn(Boolean.FALSE);
		final String pricePerMetric = strategy.calculatePricePerMetric(variantProduct, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(StringUtils.EMPTY, pricePerMetric);
	}

	@Test
	public void testCalculatePricePerMetric()
	{
		final String pricePerMetric = strategy.calculatePricePerMetric(variantProduct, baseStore, BigDecimal.valueOf(10.0));
		assertEquals("£5.00/100 ml", pricePerMetric);
	}

	@Test
	public void testWhenUnitMeasurementMapIsEmpty()
	{
		when(baseStore.getMeasurementUnitTypeMap()).thenReturn(Collections.emptyMap());
		final String pricePerMetric = strategy.calculatePricePerMetric(variantProduct, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(StringUtils.EMPTY, pricePerMetric);
	}
}
