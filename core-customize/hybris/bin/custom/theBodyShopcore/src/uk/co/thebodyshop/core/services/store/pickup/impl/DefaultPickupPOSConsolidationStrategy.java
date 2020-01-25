/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.store.pickup.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import uk.co.thebodyshop.core.services.store.pickup.PickupPOSConsolidationStrategy;
import uk.co.thebodyshop.core.stock.TbsCommerceStockService;

/**
 * @author prateek.goel
 */
public class DefaultPickupPOSConsolidationStrategy implements PickupPOSConsolidationStrategy
{

	private TbsCommerceStockService tbsCommerceStockService;

	@Override
	public List<PointOfServiceData> getConsolidationOptions(final AbstractOrderModel abstractOrder, final List<PointOfServiceModel> pointOfServiceList)
	{

		final List<AbstractOrderEntryModel> entries = abstractOrder.getEntries();
		if (CollectionUtils.isNotEmpty(entries))
		{
			return processPotentialPickupPoints(pointOfServiceList, entries);
		}
		return null;
	}

	protected List<PointOfServiceData> processPotentialPickupPoints(final List<PointOfServiceModel> posList, final List<AbstractOrderEntryModel> entries)
	{
		final List<PointOfServiceData> consolidatedPOS = new ArrayList<>();
		for (final PointOfServiceModel pos : posList)
		{
			if (checkAllStockAvailableAtPointOfService(entries, pos))
			{
				final PointOfServiceData pointOfService = new PointOfServiceData();
				pointOfService.setName(pos.getName());
				consolidatedPOS.add(pointOfService);
			}
		}
		return consolidatedPOS;
	}

	@Override
	public boolean checkAllStockAvailableAtPointOfService(final List<AbstractOrderEntryModel> entries, final PointOfServiceModel pos)
	{
		for (final AbstractOrderEntryModel entry : entries)
		{
			if (!checkStockAvailableAtPointOfService(entry, pos))
			{
				return false;
			}
		}
		return true;
	}

	protected boolean checkStockAvailableAtPointOfService(final AbstractOrderEntryModel entry, final PointOfServiceModel posModel)
	{
		final ProductModel product = entry.getProduct();
		final Long stockLevel = getTbsCommerceStockService().getProductStockLevelForStore(product, getTbsCommerceStockService().getStockLevelForProductAndPointOfService(product, posModel), entry.getOrder().getStore());

		return stockLevel == null || stockLevel.intValue() >= entry.getQuantity();
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
