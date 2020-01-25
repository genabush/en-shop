/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryModeDeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class USPartialPostcodeRestrictionHandler extends AbstractFieldDeliveryRestrictionHandler<DeliveryModeDeliveryRestrictionModel>
{

	// USA 5 numbers + specific number
	private static final int LENGTH_AREA = 5;

	@Override
	public boolean isAddressRestricted(final AddressModel address, final DeliveryModeDeliveryRestrictionModel deliveryRestriction)
	{
		if (isPartialRestriction(deliveryRestriction.getPostcode()) && address != null)
		{
			return match(deliveryRestriction.getPostcode(), address.getPostalcode());
		}
		return false;
	}

	private boolean isPartialRestriction(final String postcode)
	{
		return StringUtils.length(postcode) == LENGTH_AREA;
	}

	private boolean match(final String restrictionPostcode, final String addressPostcode)
	{
		final String trimmedAddressPostcode = StringUtils.trimToEmpty(addressPostcode);
		if (StringUtils.length(trimmedAddressPostcode) > LENGTH_AREA)
		{
			final String addressPartial = trimmedAddressPostcode.substring(0, LENGTH_AREA);
			return equalsTrimIgnoreCase(addressPartial, restrictionPostcode);
		}
		return false;
	}

	@Override
	public boolean isDeliveryRestrictionNotEmpty(final DeliveryModeDeliveryRestrictionModel deliveryRestriction)
	{

		return true;
	}
}
