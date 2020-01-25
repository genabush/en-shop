/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.order.impl;

import java.util.List;

import de.hybris.platform.acceleratorservices.order.impl.DefaultAcceleratorCheckoutService;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.services.order.TbsAcceleratorCheckoutService;
import uk.co.thebodyshop.core.services.store.pickup.PickupPOSConsolidationStrategy;

/**
 * @author prateek.goel
 */
public class DefaultTbsAcceleratorCheckoutService extends DefaultAcceleratorCheckoutService implements TbsAcceleratorCheckoutService
{

	private PickupPOSConsolidationStrategy pickupPOSConsolidationStrategy;

	@Override
	public List<PointOfServiceData> getConsolidatedPickupOptions(final AbstractOrderModel abstractOrder, final List<PointOfServiceModel> posList)
	{
		return getPickupPOSConsolidationStrategy().getConsolidationOptions(abstractOrder, posList);
	}

	@Override
	public void updateCisOrders(final PointOfServiceModel pos, final boolean increment, final boolean decrement)
	{
		Integer cisOpenOrders = pos.getCisOpenOrders();
		if (null == cisOpenOrders)
		{
			cisOpenOrders = Integer.valueOf(0);
		}
		if (increment)
		{
			pos.setCisOpenOrders(Integer.valueOf(cisOpenOrders.intValue() + 1));
		}
		if (decrement)
		{
			pos.setCisOpenOrders(Integer.valueOf(cisOpenOrders.intValue() - 1));
		}
		getModelService().save(pos);
		getModelService().refresh(pos);

	}

	@Override
	public boolean isCollectInStoreOrder(final AbstractOrderModel order)
	{
		return null != order.getDeliveryPointOfService() && FulfillmentMethodEnum.COLLECTINSTORE == order.getFulfillmentMethod();
	}

	protected PickupPOSConsolidationStrategy getPickupPOSConsolidationStrategy()
	{
		return pickupPOSConsolidationStrategy;
	}

	public void setPickupPOSConsolidationStrategy(final PickupPOSConsolidationStrategy pickupPOSConsolidationStrategy)
	{
		this.pickupPOSConsolidationStrategy = pickupPOSConsolidationStrategy;
	}

}
