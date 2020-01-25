/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class CountryFieldRestrictionsHandler extends AbstractFieldDeliveryRestrictionHandler
{

	@Override
	public boolean isAddressRestricted(final AddressModel address, final DeliveryRestrictionModel deliveryRestriction)
	{
		final CountryModel country = deliveryRestriction.getCountry();
		return address != null && isDeliveryRestrictionNotEmpty(deliveryRestriction) && equalsTrimIgnoreCase(country.getIsocode(), address.getCountry().getIsocode());
	}

	@Override
	public boolean isDeliveryRestrictionNotEmpty(final DeliveryRestrictionModel deliveryRestriction)
	{
		final CountryModel country = deliveryRestriction.getCountry();
		return country != null;
	}
}
