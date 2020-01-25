/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.store.pickup.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import uk.co.thebodyshop.core.stock.TbsCommerceStockService;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPickupPOSConsolidationStrategyTest
{

	@InjectMocks
	private DefaultPickupPOSConsolidationStrategy defaultPickupPOSConsolidationStrategy;

	@Mock
	private TbsCommerceStockService tbscommerceStockService;

	@Mock
	private AbstractOrderModel order;

	private final AbstractOrderModel abstractOrder = new AbstractOrderModel();
	private final List<PointOfServiceModel> posList = new ArrayList<>();

	@Before
	public void setUp()
	{
		final ProductModel product = new ProductModel();
		product.setCode("p1");
		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setProduct(product);
		entry.setQuantity(2L);
		entry.setOrder(abstractOrder);
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(entry);
		abstractOrder.setEntries(entries);
		final PointOfServiceModel pos = new PointOfServiceModel();
		pos.setName("POS1");
		posList.add(pos);
	}

	@Test
	public void testIfStockAvailableforPOS()
	{
		when(tbscommerceStockService.getStockLevelForProductAndPointOfService(Mockito.anyObject(), Mockito.anyObject())).thenReturn(3L);
		when(tbscommerceStockService.getProductStockLevelForStore(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(3L);
		final List<PointOfServiceData> posDataList = defaultPickupPOSConsolidationStrategy.getConsolidationOptions(abstractOrder, posList);
		Assert.assertTrue(CollectionUtils.isNotEmpty(posDataList));
	}

	@Test
	public void testIfStockNotAvailableforPOS()
	{
		when(tbscommerceStockService.getStockLevelForProductAndPointOfService(Mockito.anyObject(), Mockito.anyObject())).thenReturn(1L);
		when(tbscommerceStockService.getProductStockLevelForStore(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(1L);
		final List<PointOfServiceData> posDataList = defaultPickupPOSConsolidationStrategy.getConsolidationOptions(abstractOrder, posList);
		Assert.assertFalse(CollectionUtils.isNotEmpty(posDataList));
	}

	@Test
	public void testIfEntryNotAvailable()
	{
		abstractOrder.setEntries(null);
		final List<PointOfServiceData> posDataList = defaultPickupPOSConsolidationStrategy.getConsolidationOptions(abstractOrder, posList);
		Assert.assertFalse(CollectionUtils.isNotEmpty(posDataList));
	}


}
