/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.loyalty.discount.calculation.strategy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Krishna
 */

@UnitTest
public class OrderEntryLoyaltyDiscountCalculationStrategyTest
{
	@InjectMocks
	private OrderEntryLoyaltyDiscountCalculationStrategy strategy;

	@Mock
	private ModelService modelService;

	@Mock
	private CartModel cart;

	private List<AbstractOrderEntryModel> entries;

	@Mock
	private AbstractOrderEntryModel abstractOrderEntry1;

	@Mock
	private AbstractOrderEntryModel abstractOrderEntry2;

	@Mock
	private TbsVariantProductModel product1;

	@Mock
	private TbsVariantProductModel product2;

	@Mock
	private CurrencyModel currency;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		when(cart.getLoyaltyVoucherDiscount()).thenReturn(10.0);
		when(cart.getCurrency()).thenReturn(currency);
		when(currency.getDigits().intValue()).thenReturn(2);

		when(abstractOrderEntry1.getProduct()).thenReturn(product1);
		when(abstractOrderEntry1.getTotalPrice()).thenReturn(20.0);

		when(abstractOrderEntry2.getProduct()).thenReturn(product2);
		when(abstractOrderEntry2.getTotalPrice()).thenReturn(30.0);

		when(product1.getLoyalty()).thenReturn(Boolean.TRUE);
		when(product2.getLoyalty()).thenReturn(Boolean.TRUE);

		entries = new ArrayList<>();
		entries.add(abstractOrderEntry1);
		entries.add(abstractOrderEntry2);
		when(cart.getEntries()).thenReturn(entries);

	}

	@Test
	public void testCalculateOrderEntryLoyaltyDiscount()
	{
		when(cart.getEntries().get(0).getLoyaltyDiscount().doubleValue()).thenReturn(4.0);
		when(cart.getEntries().get(1).getLoyaltyDiscount().doubleValue()).thenReturn(6.0);
		strategy.calculateOrderEntryLoyaltyDiscount(cart);
		assertEquals(4.0, cart.getEntries().get(0).getLoyaltyDiscount().doubleValue(), 0.0f);
		assertEquals(6.0, cart.getEntries().get(1).getLoyaltyDiscount().doubleValue(), 0.0f);
	}

	@Test
	public void testCalculateOrderEntryLoyaltyDiscountWhenLoyaltyNotEnabled()
	{
		when(cart.getEntries().get(0).getLoyaltyDiscount().doubleValue()).thenReturn(10.0);
		when(cart.getEntries().get(1).getLoyaltyDiscount().doubleValue()).thenReturn(0.0);
		when(product2.getLoyalty()).thenReturn(Boolean.FALSE);
		strategy.calculateOrderEntryLoyaltyDiscount(cart);
		assertEquals(10.0, cart.getEntries().get(0).getLoyaltyDiscount().doubleValue(), 0.0f);
		assertEquals(0.0, cart.getEntries().get(1).getLoyaltyDiscount().doubleValue(), 0.0f);
	}
}
