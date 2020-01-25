/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.deliveryrestrictions.handlers.FieldRestrictionsHandler;
import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryModeDeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class PostCodeFieldRestrictionsHandler extends AbstractFieldDeliveryRestrictionHandler<DeliveryModeDeliveryRestrictionModel>
{

	private BaseStoreService baseStoreService;

	private Map<String, FieldRestrictionsHandler> partialPostcodeHandlerMap;

	@Override
	public boolean isAddressRestricted(final AddressModel address, final DeliveryModeDeliveryRestrictionModel deliveryRestriction)
	{
		if (isDeliveryRestrictionNotEmpty(deliveryRestriction) && address != null)
		{
			return equalsTrimIgnoreCase(deliveryRestriction.getPostcode(), address.getPostalcode()) || checkPartialPostcode(address, deliveryRestriction);
		}
		return false;
	}

	private boolean checkPartialPostcode(final AddressModel address, final DeliveryModeDeliveryRestrictionModel deliveryRestriction)
	{
		final String baseStoreUID = getBaseStoreService().getCurrentBaseStore().getUid();
		if (getPartialPostcodeHandlerMap().containsKey(baseStoreUID))
		{
			return getPartialPostcodeHandlerMap().get(baseStoreUID).isAddressRestricted(address, deliveryRestriction);
		}
		return false;
	}

	protected Map<String, FieldRestrictionsHandler> getPartialPostcodeHandlerMap()
	{
		return partialPostcodeHandlerMap;
	}

	public void setPartialPostcodeHandlerMap(final Map<String, FieldRestrictionsHandler> partialPostcodeHandlerMap)
	{
		this.partialPostcodeHandlerMap = partialPostcodeHandlerMap;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	public boolean isDeliveryRestrictionNotEmpty(final DeliveryModeDeliveryRestrictionModel deliveryRestriction)
	{

		return StringUtils.isNotEmpty(deliveryRestriction.getPostcode());
	}
}
