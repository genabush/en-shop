/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.strategies.DeliveryModeRestrictionStrategy;

/**
 * @author prateek.goel
 */
public class DirectDeliveryModesStrategy implements DeliveryModeRestrictionStrategy
{
	@Override
	public boolean filter(final List<DeliveryModeModel> deliveryModes, final AbstractOrderModel order)
	{
		final FulfillmentMethodEnum fulfillmentMethod = order.getFulfillmentMethod();
		if (CollectionUtils.isNotEmpty(deliveryModes) && FulfillmentMethodEnum.DIRECT == fulfillmentMethod)
		{
			final List<DeliveryModeModel> restrictedDeliveryModes = new ArrayList<>();

			for (final DeliveryModeModel deliveryMode : deliveryModes)
			{
				if (BooleanUtils.isTrue(deliveryMode.getCollectionPoint()) || BooleanUtils.isTrue(deliveryMode.getForCollectInStore()))
				{
					restrictedDeliveryModes.add(deliveryMode);
				}
			}
			if (CollectionUtils.isNotEmpty(restrictedDeliveryModes))
			{
				deliveryModes.removeAll(restrictedDeliveryModes);
				return true;
			}
		}
		return false;
	}
}
