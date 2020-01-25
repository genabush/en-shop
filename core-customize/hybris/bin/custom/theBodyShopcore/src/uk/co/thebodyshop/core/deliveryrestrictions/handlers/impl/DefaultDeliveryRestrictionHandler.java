/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import java.util.Collection;

import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.deliveryrestrictions.handlers.DeliveryRestrictionHandler;
import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class DefaultDeliveryRestrictionHandler implements DeliveryRestrictionHandler<DeliveryRestrictionModel>
{

	private CompositeFieldRestrictionHandlers handler;

	@Override
	public boolean isDeliveryModeRestricted(final AddressModel address, final Collection<DeliveryRestrictionModel> deliveryRestrictions)
	{
		for (final DeliveryRestrictionModel deliveryRestriction : deliveryRestrictions)
		{
			if (handler.isAddressRestricted(address, deliveryRestriction))
			{
				return true;
			}
		}
		return false;
	}

	protected CompositeFieldRestrictionHandlers getHandler()
	{
		return handler;
	}

	public void setHandler(final CompositeFieldRestrictionHandlers handler)
	{
		this.handler = handler;
	}

}
