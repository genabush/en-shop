/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.stock.processor;

import java.util.Arrays;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.strategies.impl.TbsStockAvailabilityCalculationStrategy;

/**
 * @author prateek.goel
 */
public class StockLevelProcessStrategy implements StockProcessStrategy
{

	private StockService stockService;

	private ModelService modelService;

	private TbsStockAvailabilityCalculationStrategy tbsStockAvailabilityCalculationStrategy;

	@Override
	public void reserve(final AbstractOrderModel abstractOrderModel) throws InsufficientStockLevelException
	{
		if (CollectionUtils.isNotEmpty(abstractOrderModel.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
			{
				reserve(entry);
			}
		}
	}

	protected void reserve(final AbstractOrderEntryModel entry) throws InsufficientStockLevelException
	{
		final AbstractOrderModel order = entry.getOrder();
		final ProductModel product = entry.getProduct();

		final BaseStoreModel baseStore = order.getStore();
		final WarehouseModel warehousesForBaseStore = baseStore.getWarehouses().iterator().next();

		final StockLevelModel stockLevel = getStockService().getStockLevel(product, warehousesForBaseStore);

		final int stockToReserve = entry.getQuantity().intValue();

		final int availableStock = (int) getTbsStockAvailabilityCalculationStrategy().calculateAvailability(Arrays.asList(stockLevel)).longValue();

		if (stockToReserve <= availableStock)
		{
			try
			{
				getStockService().reserve(product, stockLevel.getWarehouse(), stockToReserve, "Reserving stock quantity [" + stockToReserve + "] for order with code [" + order.getCode() + "]");
			}
			catch (final InsufficientStockLevelException e)
			{
				throw new InsufficientStockLevelException("Found less stock for warehouse " + stockLevel.getWarehouse().getCode());
			}
		}
		else
		{
			throw new InsufficientStockLevelException(product.getName() + " out of stock");
		}
	}

	@Override
	public void release(final AbstractOrderModel order) throws InsufficientStockLevelException
	{
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			release(entry.getOrder().getStore(), entry.getProduct(), entry.getQuantity());
		}
	}

	@Override
	public void release(final ConsignmentModel consignment) throws InsufficientStockLevelException
	{
		for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
		{
			final AbstractOrderEntryModel entry = consignmentEntry.getOrderEntry();
			release(entry.getOrder().getStore(), entry.getProduct(), consignmentEntry.getQuantity());
		}
	}

	protected void release(final BaseStoreModel baseStore, final ProductModel product, final Long quantity) throws InsufficientStockLevelException
	{
		final WarehouseModel warehousesForBaseStore = baseStore.getWarehouses().iterator().next();

		final StockLevelModel stockLevel = getStockService().getStockLevel(product, warehousesForBaseStore);

		final int amountToRelease = quantity.intValue();
		if (amountToRelease > 0)
		{
			if (stockLevel != null && amountToRelease <= stockLevel.getReserved())
			{
				getStockService().release(product, stockLevel.getWarehouse(), amountToRelease, "Releasing stock quantity [" + amountToRelease + "]. This was triggered from the stock check task");
			}
			else
			{
				throw new InsufficientStockLevelException("Unable to release stock for product:" + product.getCode() + " quantity remaining for release:" + amountToRelease);
			}
		}
	}

	@Override
	public void fulfill(final ConsignmentModel consignment, final boolean reset) throws InsufficientStockLevelException
	{
		for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
		{
			fulfill(consignment.getWarehouse(), entry.getOrderEntry().getProduct(), entry.getQuantity(), reset);
		}

	}

	private void fulfill(final WarehouseModel warehouseModel, final ProductModel product, final Long quantity, final boolean reset) throws InsufficientStockLevelException
	{

		final StockLevelModel stockLevel = getStockService().getStockLevel(product, warehouseModel);

		final int fulfilledAmount = quantity.intValue();

		if (stockLevel != null)
		{
			if (reset)
			{
				stockLevel.setReserved(stockLevel.getReserved() + fulfilledAmount);
				stockLevel.setFulfilled(stockLevel.getFulfilled() - fulfilledAmount);
			}
			else
			{
				stockLevel.setReserved(stockLevel.getReserved() - fulfilledAmount);
				stockLevel.setFulfilled(stockLevel.getFulfilled() + fulfilledAmount);
			}
			getModelService().save(stockLevel);
		}
		else
		{
			throw new InsufficientStockLevelException("Stock Level is not present for product [" + product.getCode() + "]");
		}
	}

	@Override
	public boolean applicable(final AbstractOrderModel abstractOrderModel)
	{
		return BooleanUtils.isTrue(FulfillmentMethodEnum.COLLECTION == abstractOrderModel.getFulfillmentMethod()) || BooleanUtils.isTrue(FulfillmentMethodEnum.DIRECT == abstractOrderModel.getFulfillmentMethod());
	}

	protected StockService getStockService()
	{
		return stockService;
	}

	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TbsStockAvailabilityCalculationStrategy getTbsStockAvailabilityCalculationStrategy()
	{
		return tbsStockAvailabilityCalculationStrategy;
	}

	public void setTbsStockAvailabilityCalculationStrategy(final TbsStockAvailabilityCalculationStrategy tbsStockAvailabilityCalculationStrategy)
	{
		this.tbsStockAvailabilityCalculationStrategy = tbsStockAvailabilityCalculationStrategy;
	}

}
