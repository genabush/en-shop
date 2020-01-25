/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class RegionFieldRestrictionsHandler extends AbstractFieldDeliveryRestrictionHandler
{

	@Override
	public boolean isAddressRestricted(final AddressModel address, final DeliveryRestrictionModel deliveryRestriction)
	{
		final RegionModel region = deliveryRestriction.getRegion();
		return address != null && isDeliveryRestrictionNotEmpty(deliveryRestriction) && (address.getRegion() == null || equalsTrimIgnoreCase(region.getIsocode(), address.getRegion().getIsocode()));
	}

	@Override
	public boolean isDeliveryRestrictionNotEmpty(final DeliveryRestrictionModel deliveryRestriction)
	{
		final RegionModel region = deliveryRestriction.getRegion();
		return region != null;
	}
}
