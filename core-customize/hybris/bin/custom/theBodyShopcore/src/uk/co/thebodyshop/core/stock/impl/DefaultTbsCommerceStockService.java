/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.stock.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.stock.TbsCommerceStockService;
import uk.co.thebodyshop.core.stock.processor.StockProcessorFactory;

/**
 * @author prateek.goel
 */
public class DefaultTbsCommerceStockService extends DefaultCommerceStockService implements TbsCommerceStockService
{

	private StockProcessorFactory stockProcessorFactory;

	@Override
	public StockLevelStatus getStockLevelStatusForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(baseStore, "baseStore cannot be null");

		return getStockService().getProductStatus(product, baseStore.getWarehouses());
	}

	@Override
	public Long getStockLevelForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(baseStore, "baseStore cannot be null");

		return getCommerceStockLevelCalculationStrategy().calculateAvailability(
				getStockService().getStockLevels(product, baseStore.getWarehouses()));
	}

	/**
	 * Set Default Warehouse as Preferable if it is having limited stock needed for entry Otherwise set any other non-default warehouse which is having the same. If no warehouse is satisfying the stock
	 * for entry then will pick the default warehouse as preferable warehouse for entry
	 */
	@Override
	public boolean isExtendedStockAvailable(final AbstractOrderEntryModel entry, final WarehouseModel warehouse)
	{
		final StockLevelModel stock = getStockService().getStockLevel(entry.getProduct(), warehouse);
		if (null != stock)
		{
			final int quantity = entry.getQuantity().intValue();
			final int available = stock.getAvailable();
			return quantity > available && quantity <= (available + stock.getExtendedStock());
		}
		return false;
	}

	@Override
	public void processStock(final AbstractOrderModel order) throws InsufficientStockLevelException
	{
		getStockProcessorFactory().getStockProcessStrategy(order).reserve(order);

	}

	@Override
	public void releaseStock(final AbstractOrderModel order) throws InsufficientStockLevelException
	{
		getStockProcessorFactory().getStockProcessStrategy(order).release(order);

	}

	@Override
	public void releaseStock(final ConsignmentModel consignment) throws InsufficientStockLevelException
	{
		getStockProcessorFactory().getStockProcessStrategy(consignment.getOrder()).release(consignment);

	}

	@Override
	public void fulfillStock(final ConsignmentModel consignment, final boolean reset) throws InsufficientStockLevelException
	{
		getStockProcessorFactory().getStockProcessStrategy(consignment.getOrder()).fulfill(consignment, reset);

	}

	@Override
	public Long getProductStockLevelForStore(final ProductModel product, final Long stockLevel, final BaseStoreModel store)
	{
		Long storeStockLevel =  stockLevel;
		if(product instanceof TbsVariantProductModel)
		{
			final TbsVariantProductModel variantProduct = (TbsVariantProductModel)product;
			final Integer productStockBuffer = variantProduct.getStoreStockBuffer();
			if(null != productStockBuffer)
			{
				storeStockLevel = storeStockLevel - productStockBuffer;
			}
			else
			{
				storeStockLevel = storeStockLevel - store.getStoreStockProductBuffer();
			}
		}
		return storeStockLevel > 0 ? storeStockLevel : Long.valueOf(0);
	}

	protected StockProcessorFactory getStockProcessorFactory()
	{
		return stockProcessorFactory;
	}

	public void setStockProcessorFactory(final StockProcessorFactory stockProcessorFactory)
	{
		this.stockProcessorFactory = stockProcessorFactory;
	}

}
