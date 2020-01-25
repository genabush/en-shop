/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.loyalty.points.calculation.strategies;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.DiscountValue;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Krishna
 */
@UnitTest
public class LoyaltyPointsCalculationStrategyTest
{
	@InjectMocks
	private LoyaltyPointsCalculationStrategy strategy;

	@Mock
	private BaseStoreModel baseStore;

	@Mock
	private TbsVariantProductModel product;

	@Mock
	private CartModel cart;

	@Mock
	private CurrencyModel currency;

	private List<AbstractOrderEntryModel> entries;

	@Mock
	private AbstractOrderEntryModel entry;

	@Mock
	private DiscountValue discounValue;

	private List<DiscountValue> globalDiscountValues;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(product.getLoyalty()).thenReturn(Boolean.TRUE);
		when(baseStore.getLybcEnabled()).thenReturn(Boolean.TRUE);
		when(baseStore.getLoyaltyCurrencyUnit()).thenReturn(1.0);
		when(baseStore.getLoyaltyPointsMultiplier()).thenReturn(1.0);
		when(baseStore.getLoyaltyPointsRate()).thenReturn(10.0);

		when(cart.getCurrency()).thenReturn(currency);
		when(entry.getTotalPrice()).thenReturn(20.0);
		when(entry.getProduct()).thenReturn(product);
		entries = new ArrayList<>();
		entries.add(entry);
		when(cart.getEntries()).thenReturn(entries);
		when(discounValue.getValue()).thenReturn(10.0);
		globalDiscountValues = new ArrayList<>();
		globalDiscountValues.add(discounValue);
		when(cart.getGlobalDiscountValues()).thenReturn(globalDiscountValues);
	}

	@Test
	public void testCalculateLoyaltyPoints()
	{
		final Integer points = strategy.calculateLoyaltyPoints(product, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(100, points.intValue());
	}

	@Test
	public void testZeroPointsWhenStoreDoesNotSupportLybc()
	{
		when(baseStore.getLybcEnabled()).thenReturn(Boolean.FALSE);
		final Integer points = strategy.calculateLoyaltyPoints(product, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(0, points.intValue());
	}

	@Test
	public void testZeroPointsWhenProductDoesNotSupportLybc()
	{
		when(product.getLoyalty()).thenReturn(Boolean.FALSE);
		final Integer points = strategy.calculateLoyaltyPoints(product, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(0, points.intValue());
	}

	@Test
	public void testCalculateLoyaltyPointsWithMultiplier()
	{
		when(baseStore.getLoyaltyPointsMultiplier()).thenReturn(2.0);
		final Integer points = strategy.calculateLoyaltyPoints(product, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(200, points.intValue());
	}

	@Test
	public void testCalculateLoyaltyPointsWithLargerCurrencyUnit()
	{
		when(baseStore.getLoyaltyCurrencyUnit()).thenReturn(2.0);
		final Integer points = strategy.calculateLoyaltyPoints(product, baseStore, BigDecimal.valueOf(10.0));
		assertEquals(50, points.intValue());
	}

	@Test
	public void testCalculateCartLoyaltyPoints()
	{
		final Integer points = strategy.calculate(cart, baseStore);
		assertEquals(100, points.intValue());
	}

	@Test
	public void testWhenStoreDoesNotSupportLybc()
	{
		when(baseStore.getLybcEnabled()).thenReturn(Boolean.FALSE);
		final Integer points = strategy.calculate(cart, baseStore);
		assertEquals(0, points.intValue());
	}

	@Test
	public void testWhenProductDoesNotSupportLybc()
	{
		when(product.getLoyalty()).thenReturn(Boolean.FALSE);
		final Integer points = strategy.calculate(cart, baseStore);
		assertEquals(0, points.intValue());
	}

	@Test
	public void testCalculateCartLoyaltyPointsWithMultiplier()
	{
		when(baseStore.getLoyaltyPointsMultiplier()).thenReturn(2.0);
		final Integer points = strategy.calculate(cart, baseStore);
		assertEquals(200, points.intValue());
	}

	@Test
	public void testCalculateCartLoyaltyPointsWithLargerCurrencyUnit()
	{
		when(baseStore.getLoyaltyCurrencyUnit()).thenReturn(2.0);
		final Integer points = strategy.calculate(cart, baseStore);
		assertEquals(50, points.intValue());
	}

	@Test
	public void testCalculateCartLoyaltyPointsWithoutGlobalDiscounts()
	{
		when(discounValue.getValue()).thenReturn(0.0);
		final Integer points = strategy.calculate(cart, baseStore);
		assertEquals(200, points.intValue());
	}
}
