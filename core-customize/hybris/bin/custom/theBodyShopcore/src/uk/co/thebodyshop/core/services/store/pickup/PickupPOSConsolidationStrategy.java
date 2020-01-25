/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.store.pickup;

import java.util.List;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

/**
 * @author prateek.goel
 */
public interface PickupPOSConsolidationStrategy
{
	List<PointOfServiceData> getConsolidationOptions(AbstractOrderModel abstractOrder, List<PointOfServiceModel> pointOfServiceList);

	public boolean checkAllStockAvailableAtPointOfService(final List<AbstractOrderEntryModel> entries, final PointOfServiceModel pos);
}
