/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.stock.TbsCommerceStockService;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RestrictedDeliveryModesStrategyTest
{

	@Mock
	private DeliveryModeModel deliveryMode1;

	@Mock
	private AbstractOrderModel order;

	@Mock
	private AbstractOrderEntryModel entry1;

	@Mock
	private DeliveryModeModel deliveryMode2;

	@Mock
	private AbstractOrderEntryModel entry2;

	@Mock
	private WarehouseModel warehouse;

	@Mock
	private BaseStoreModel baseStoreModel;

	@Mock
	private TbsCommerceStockService tbsCommerceStockService;

	@InjectMocks
	private RestrictedDeliveryModesStrategy restrictedDeliveryModesStrategy;

	final List<DeliveryModeModel> deliveryModeList = new ArrayList<>();

	final List<WarehouseModel> warehouseList = new ArrayList<>();

	@Before
	public void setUp()
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		when(order.getStore()).thenReturn(baseStoreModel);
		entries.add(entry1);
		entries.add(entry2);
		warehouseList.add(warehouse);
		when(baseStoreModel.getWarehouses()).thenReturn(warehouseList);
		when(warehouse.getRestrictedDeliveryModes()).thenReturn(Arrays.asList(deliveryMode1));
		deliveryModeList.add(deliveryMode1);
		deliveryModeList.add(deliveryMode2);

		when(order.getEntries()).thenReturn(entries);
	}

	@Test
	public void testExtendedStockAvailable()
	{
		when(tbsCommerceStockService.isExtendedStockAvailable(entry1, warehouse)).thenReturn(true);
		when(tbsCommerceStockService.isExtendedStockAvailable(entry2, warehouse)).thenReturn(false);
		Assert.assertTrue(restrictedDeliveryModesStrategy.filter(deliveryModeList, order));
	}

	@Test
	public void tesExtendedStockNotAvailable()
	{
		when(tbsCommerceStockService.isExtendedStockAvailable(entry1, warehouse)).thenReturn(false);
		when(tbsCommerceStockService.isExtendedStockAvailable(entry2, warehouse)).thenReturn(false);
		Assert.assertFalse(restrictedDeliveryModesStrategy.filter(deliveryModeList, order));
	}

}
