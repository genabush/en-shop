/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies;

import java.util.List;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

/**
 * @author prateek.goel
 */
public interface DeliveryModeRestrictionStrategy
{
	public boolean filter(List<DeliveryModeModel> deliveryModes, AbstractOrderModel order);

}
