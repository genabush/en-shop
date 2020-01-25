/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.stock.impl;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTbsCommerceStockServiceTest
{

	@Mock
	private WarehouseModel warehouse;

	@Mock
	private StockService stockService;

	@Mock
	private StockLevelModel stockLevel;

	@Mock
	private AbstractOrderEntryModel entry;

	@Mock
	private ProductModel product;

	@InjectMocks
	private DefaultTbsCommerceStockService tbsCommerceStockService;

	@Before
	public void setUp()
	{
		when(entry.getProduct()).thenReturn(product);
		when(entry.getQuantity()).thenReturn(Long.valueOf(2));
		when(stockService.getStockLevel(product, warehouse)).thenReturn(stockLevel);
	}

	@Test
	public void testIfExtendedStockAvailable()
	{
		when(stockLevel.getAvailable()).thenReturn(1);
		when(stockLevel.getExtendedStock()).thenReturn(3);
		Assert.assertTrue(tbsCommerceStockService.isExtendedStockAvailable(entry, warehouse));

	}

	@Test
	public void testNoExtendedStockPresent()
	{
		when(stockLevel.getAvailable()).thenReturn(3);
		Assert.assertFalse(tbsCommerceStockService.isExtendedStockAvailable(entry, warehouse));
	}

}
