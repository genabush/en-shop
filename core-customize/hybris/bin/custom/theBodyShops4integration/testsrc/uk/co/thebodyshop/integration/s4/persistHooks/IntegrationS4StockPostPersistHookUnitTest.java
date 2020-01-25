/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.persistHooks;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.impl.StockLevelDao;

import uk.co.thebodyshop.core.event.PhaseInOutStockEvent;
import uk.co.thebodyshop.integration.s4.model.IntegrationS4StockModel;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class IntegrationS4StockPostPersistHookUnitTest
{
	@InjectMocks
	private IntegrationS4StockPostPersistHook integrationS4StockPostPersistHook;

	@Mock
	private ModelService modelService;

	@Mock
	private SearchRestrictionService searchRestrictionService;

	@Mock
	private WarehouseService warehouseService;

	@Mock
	private StockLevelDao stockLevelDao;

	@Mock
	private EventService eventService;

	@Mock
	private IntegrationS4StockModel integrationS4StockModel;

	@Mock
	private WarehouseModel warehouseModel;

	@Mock
	private StockLevelModel stockLevel;

	@Before
	public void setUp()
	{
		when(modelService.create(StockLevelModel.class)).thenReturn(stockLevel);

		when(integrationS4StockModel.getProductCode()).thenReturn("p001234v");
		when(integrationS4StockModel.getWarehouseCode()).thenReturn("DC01");

		when(warehouseService.getWarehouseForCode("DC01")).thenReturn(warehouseModel);

		when(stockLevelDao.findStockLevel("p001234v", warehouseModel)).thenReturn(stockLevel);

		when(stockLevel.getWarehouse()).thenReturn(warehouseModel);
	}

	@Test
	public void testNewStockLevelRecordCreated()
	{
		when(stockLevelDao.findStockLevel("p001234v", warehouseModel)).thenReturn(null);
		integrationS4StockPostPersistHook.execute(integrationS4StockModel);
		verify(modelService).create(StockLevelModel.class);
		verify(stockLevel).setProductCode("p001234v");
		verify(stockLevel).setWarehouse(warehouseModel);
	}

	@Test
	public void testExistingStockLevelRecord()
	{
		integrationS4StockPostPersistHook.execute(integrationS4StockModel);
		verify(modelService, times(0)).create(StockLevelModel.class);
	}

	@Test
	public void testEventForPipoSentWhenZeroStock()
	{
		when(integrationS4StockModel.getAvailable()).thenReturn(0);
		integrationS4StockPostPersistHook.execute(integrationS4StockModel);
		verify(eventService).publishEvent(any(PhaseInOutStockEvent.class));
	}

	@Test
	public void testEventForPipoIsNotSentWhenInStock()
	{
		when(integrationS4StockModel.getAvailable()).thenReturn(1);
		integrationS4StockPostPersistHook.execute(integrationS4StockModel);
		verify(eventService, times(0)).publishEvent(any(PhaseInOutStockEvent.class));
	}

}
