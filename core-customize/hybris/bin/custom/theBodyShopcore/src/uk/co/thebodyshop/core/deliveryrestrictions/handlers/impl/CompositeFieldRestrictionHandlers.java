/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.deliveryrestrictions.handlers.FieldRestrictionsHandler;
import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class CompositeFieldRestrictionHandlers implements FieldRestrictionsHandler
{

	private List<FieldRestrictionsHandler> restrictionsHandlers;

	@Override
	public boolean isAddressRestricted(final AddressModel address, final DeliveryRestrictionModel deliveryRestriction)
	{
		if (!this.isDeliveryRestrictionNotEmpty(deliveryRestriction))
		{
			return false;
		}
		final List<FieldRestrictionsHandler> listHandler = getRestrictionsHandlers();
		if (CollectionUtils.isNotEmpty(listHandler))
		{
			for (final FieldRestrictionsHandler handler : listHandler)
			{
				if (handler.isAddressRestricted(address, deliveryRestriction))
				{
					return true;
				}
			}
		}
		return false;
	}

	protected List<FieldRestrictionsHandler> getRestrictionsHandlers()
	{
		return restrictionsHandlers;
	}

	public void setRestrictionsHandlers(final List<FieldRestrictionsHandler> restrictionsHandlers)
	{
		this.restrictionsHandlers = restrictionsHandlers;
	}

	@Override
	public boolean isDeliveryRestrictionNotEmpty(final DeliveryRestrictionModel deliveryRestriction)
	{

		return deliveryRestriction != null;
	}

}
