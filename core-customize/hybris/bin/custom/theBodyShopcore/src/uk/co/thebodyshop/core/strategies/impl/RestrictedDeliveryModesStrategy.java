/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.stock.TbsCommerceStockService;
import uk.co.thebodyshop.core.strategies.DeliveryModeRestrictionStrategy;

/**
 * @author prateek.goel
 */
public class RestrictedDeliveryModesStrategy implements DeliveryModeRestrictionStrategy
{

	private TbsCommerceStockService tbsCommerceStockService;

	@Override
	public boolean filter(final List<DeliveryModeModel> deliveryModes, final AbstractOrderModel order)
	{
		if (CollectionUtils.isNotEmpty(deliveryModes))
		{
			final BaseStoreModel baseStore = order.getStore();
			final WarehouseModel warehouse = baseStore.getWarehouses().iterator().next();
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				if (getTbsCommerceStockService().isExtendedStockAvailable(entry, warehouse))
				{
					final List<DeliveryModeModel> restrictedDeliveryModes = warehouse.getRestrictedDeliveryModes();
					if (CollectionUtils.isNotEmpty(restrictedDeliveryModes))
					{
						deliveryModes.removeAll(restrictedDeliveryModes);
						return true;
					}
				}
			}
		}
		return false;
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
