package uk.co.thebodyshop.core.stock.processor;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

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
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.stock.TbsCommerceStockService;
import uk.co.thebodyshop.core.strategies.impl.TbsStockAvailabilityCalculationStrategy;

/**
 * @author prateek.goel
 */
public class StoreStockLevelProcessStrategy implements StockProcessStrategy
{

	private StockService stockService;

	private ModelService modelService;

	private TbsStockAvailabilityCalculationStrategy tbsStockAvailabilityCalculationStrategy;

	private TbsCommerceStockService tbsCommerceStockService;

	@Override
	public void reserve(final AbstractOrderModel abstractOrderModel) throws InsufficientStockLevelException
	{
		if (CollectionUtils.isNotEmpty(abstractOrderModel.getEntries()))
		{
			final PointOfServiceModel pointOfService = abstractOrderModel.getDeliveryPointOfService();
			if (pointOfService == null)
			{
				throw new InsufficientStockLevelException("Unable to retrieve point of service for the order");
			}
			for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
			{
				reserve(entry, pointOfService);
			}
		}
	}

	protected void reserve(final AbstractOrderEntryModel entry, final PointOfServiceModel pointOfService) throws InsufficientStockLevelException
	{
		//reduce store stock level
		final ProductModel product = entry.getProduct();
		StockLevelModel storeStockLevel = null;
		final List<WarehouseModel> warehouseList = pointOfService.getWarehouses();
		if (CollectionUtils.isNotEmpty(warehouseList))
		{
			storeStockLevel = getStockService().getStockLevel(product, warehouseList.iterator().next());
		}
		if (storeStockLevel == null)
		{
			throw new InsufficientStockLevelException(String.format("Cannot process order. Unable to retrieve store stock for %s at %s.", product.getCode(), pointOfService.getName()));
		}
		final int availableStock = (int) getTbsCommerceStockService().getProductStockLevelForStore(product, getTbsStockAvailabilityCalculationStrategy().calculateAvailability(Arrays.asList(storeStockLevel)), entry.getOrder().getStore()).longValue();
		if (availableStock >= entry.getQuantity().intValue())
		{
			storeStockLevel.setReserved(storeStockLevel.getReserved() + entry.getQuantity().intValue());
			getModelService().save(storeStockLevel);
		}
		else
		{
			throw new InsufficientStockLevelException(String.format("Insufficient stock for product %s at store %s.", product.getCode(), pointOfService.getName()));
		}
	}

	@Override
	public void release(final AbstractOrderModel order) throws InsufficientStockLevelException
	{
		if (order != null)
		{
			//release store stock level
			if (CollectionUtils.isNotEmpty(order.getEntries()))
			{
				//get pos from order
				final PointOfServiceModel pointOfService = order.getDeliveryPointOfService();
				if (null != pointOfService)
				{
					for (final AbstractOrderEntryModel entry : order.getEntries())
					{
						release(entry, pointOfService);
					}
				}
			}
		}
	}

	@Override
	public void release(final ConsignmentModel consignment) throws InsufficientStockLevelException
	{
		final PointOfServiceModel pointOfService = consignment.getDeliveryPointOfService();
		for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
		{
			final AbstractOrderEntryModel entry = consignmentEntry.getOrderEntry();
			release(consignmentEntry, pointOfService);
		}
	}

	protected void release(final ConsignmentEntryModel consignmentEntry, final PointOfServiceModel pointOfService) throws InsufficientStockLevelException
	{
		final List<WarehouseModel> warehouseList = pointOfService.getWarehouses();
		if (CollectionUtils.isNotEmpty(warehouseList))
		{
			final AbstractOrderEntryModel entry = consignmentEntry.getOrderEntry();
			final StockLevelModel storeStockLevel = getStockService().getStockLevel(entry.getProduct(), warehouseList.iterator().next());
			release(storeStockLevel, consignmentEntry.getQuantity().intValue());
		}
	}

	protected void release(final AbstractOrderEntryModel entry, final PointOfServiceModel pointOfService) throws InsufficientStockLevelException
	{
		//release store stock capacity by one
		StockLevelModel storeStockLevel = null;
		final List<WarehouseModel> warehouseList = pointOfService.getWarehouses();
		if (CollectionUtils.isNotEmpty(warehouseList))
		{
			storeStockLevel = getStockService().getStockLevel(entry.getProduct(), warehouseList.iterator().next());
		}
		release(storeStockLevel, entry.getQuantity().intValue());
	}

	protected void release(final StockLevelModel storeStockLevel, final int quantity) throws InsufficientStockLevelException
	{
		if (storeStockLevel == null)
		{
			throw new InsufficientStockLevelException("Stock is not available to release");
		}
		storeStockLevel.setReserved(storeStockLevel.getReserved() - quantity);
		getModelService().save(storeStockLevel);
	}

	@Override
	public void fulfill(final ConsignmentModel consignment, final boolean reset) throws InsufficientStockLevelException
	{
		for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
		{
			final PointOfServiceModel pos = consignment.getDeliveryPointOfService();
			final WarehouseModel warehouse = pos.getWarehouses().iterator().next();
			fulfill(warehouse, entry.getOrderEntry().getProduct(), entry.getQuantity(), reset);
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
		return FulfillmentMethodEnum.COLLECTINSTORE == abstractOrderModel.getFulfillmentMethod();
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

	protected TbsCommerceStockService getTbsCommerceStockService()
	{
		return tbsCommerceStockService;
	}

	public void setTbsCommerceStockService(final TbsCommerceStockService tbsCommerceStockService)
	{
		this.tbsCommerceStockService = tbsCommerceStockService;
	}

}
