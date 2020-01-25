/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.deliveryrestrictions.manager.RestrictionsManager;
import uk.co.thebodyshop.core.strategies.DeliveryModeRestrictionStrategy;

/**
 * @author prateek.goel
 */
public class AddressDeliveryModeRestrictionStrategy implements DeliveryModeRestrictionStrategy
{

	private RestrictionsManager restrictionsManager;

	@Override
	public boolean filter(final List<DeliveryModeModel> deliveryModes, final AbstractOrderModel order)
	{
		final AddressModel deliveryAddress = order.getDeliveryAddress();
		if (CollectionUtils.isNotEmpty(deliveryModes) && null != deliveryAddress)
		{
			final List<DeliveryModeModel> restrictedDeliveryModes = getRestrictionsManager().handleDeliveryModeRestrictions(deliveryAddress, deliveryModes);
			if (CollectionUtils.isNotEmpty(restrictedDeliveryModes))
			{
				deliveryModes.removeAll(restrictedDeliveryModes);
				return true;
			}
		}
		return false;
	}

	protected RestrictionsManager getRestrictionsManager()
	{
		return restrictionsManager;
	}

	public void setRestrictionsManager(final RestrictionsManager restrictionsManager)
	{
		this.restrictionsManager = restrictionsManager;
	}

}
