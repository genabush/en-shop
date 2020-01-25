/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class CityFieldRestrictionsHandler extends AbstractFieldDeliveryRestrictionHandler
{

	@Override
	public boolean isAddressRestricted(final AddressModel address, final DeliveryRestrictionModel deliveryRestriction)
	{
		final String cityRestriction = deliveryRestriction.getCity();
		return address != null && isDeliveryRestrictionNotEmpty(deliveryRestriction) && equalsTrimIgnoreCase(cityRestriction, address.getTown());
	}

	@Override
	public boolean isDeliveryRestrictionNotEmpty(final DeliveryRestrictionModel deliveryRestriction)
	{
		final String cityRestriction = deliveryRestriction.getCity();
		return isNotEmpty(cityRestriction);
	}
}
