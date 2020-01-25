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
public class UKPartialPostcodeRestrictionHandler extends AbstractFieldDeliveryRestrictionHandler<DeliveryModeDeliveryRestrictionModel>
{

	// UK Postcode have the first 2-4 numbers for the area and the last 3 for the specific part.
	private static final int MAX_LENGTH_POSTCODE_AREA = 4;

	private static final int LENGTH_SPECIFIC_POSTCODE_PART = 3;

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
		return StringUtils.length(postcode) <= MAX_LENGTH_POSTCODE_AREA;
	}

	private boolean match(final String restrictionPostcode, final String addressPostcode)
	{
		if (StringUtils.length(addressPostcode) > LENGTH_SPECIFIC_POSTCODE_PART)
		{
			final String addressPartial = addressPostcode.trim().substring(0, addressPostcode.length() - LENGTH_SPECIFIC_POSTCODE_PART);
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
