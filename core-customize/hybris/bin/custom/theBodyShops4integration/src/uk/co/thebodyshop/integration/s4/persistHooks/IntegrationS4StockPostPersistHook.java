/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.persistHooks;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.odata2services.odata.persistence.hook.PostPersistHook;
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
public class IntegrationS4StockPostPersistHook implements PostPersistHook
{
	private final ModelService modelService;
	private final SearchRestrictionService searchRestrictionService;
	private final WarehouseService warehouseService;
	private final StockLevelDao stockLevelDao;
	private final EventService eventService;

	public IntegrationS4StockPostPersistHook(final ModelService modelService, final SearchRestrictionService searchRestrictionService, final WarehouseService warehouseService, final StockLevelDao stockLevelDao, final EventService eventService)
	{
		this.modelService = modelService;
		this.searchRestrictionService = searchRestrictionService;
		this.warehouseService = warehouseService;
		this.stockLevelDao = stockLevelDao;
		this.eventService = eventService;
	}

	@Override
	public void execute(final ItemModel item)
	{
		if (item instanceof IntegrationS4StockModel)
		{
			final IntegrationS4StockModel model = (IntegrationS4StockModel) item;
			getSearchRestrictionService().disableSearchRestrictions();
			createOrUpdateStock(model);
			getSearchRestrictionService().enableSearchRestrictions();
		}
	}

	private void createOrUpdateStock(final IntegrationS4StockModel model)
	{
		final String productCode = model.getProductCode();

		final StockLevelModel stockLevel = retrieveStock(productCode, model.getWarehouseCode());
		updateStockLevel(model, stockLevel, false);

		if (model.getAvailable() == 0)
		{
			getEventService().publishEvent(new PhaseInOutStockEvent(productCode, stockLevel.getWarehouse()));
		}

		getModelService().remove(model);
	}

	private StockLevelModel retrieveStock(final String productCode, final String warehouseCode)
	{
		final WarehouseModel warehouse = getWarehouseService().getWarehouseForCode(warehouseCode);

		return retrieveStock(productCode, warehouse);
	}

	private StockLevelModel retrieveStock(final String productCode, final WarehouseModel warehouse)
	{
		if (warehouse == null)
		{
			return null;
		}

		StockLevelModel stockLevel = getStockLevelDao().findStockLevel(productCode, warehouse);

		if (null == stockLevel)
		{
			stockLevel = getModelService().create(StockLevelModel.class);
			stockLevel.setProductCode(productCode);
			stockLevel.setWarehouse(warehouse);
		}

		return stockLevel;
	}

	private void updateStockLevel(final IntegrationS4StockModel model, final StockLevelModel stockLevel, final boolean extended)
	{
		if (stockLevel != null)
		{
			stockLevel.setActiveEan(model.getActiveEan());
			stockLevel.setAvailable(model.getAvailable());
			stockLevel.setInStockStatus(InStockStatus.NOTSPECIFIED);
			stockLevel.setFulfilled(0);
			stockLevel.setExtendedStock(model.getExtended());
			getModelService().save(stockLevel);
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected SearchRestrictionService getSearchRestrictionService()
	{
		return searchRestrictionService;
	}

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	protected StockLevelDao getStockLevelDao()
	{
		return stockLevelDao;
	}

	protected EventService getEventService()
	{
		return eventService;
	}
}
