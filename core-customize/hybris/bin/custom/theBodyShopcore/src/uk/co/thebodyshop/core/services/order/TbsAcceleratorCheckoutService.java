/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.order;

import java.util.List;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

/**
 * @author prateek.goel
 */
public interface TbsAcceleratorCheckoutService
{

	List<PointOfServiceData> getConsolidatedPickupOptions(AbstractOrderModel abstractOrder, List<PointOfServiceModel> posList);

	void updateCisOrders(PointOfServiceModel pos, boolean increment, boolean decrement);

	boolean isCollectInStoreOrder(AbstractOrderModel order);

}
