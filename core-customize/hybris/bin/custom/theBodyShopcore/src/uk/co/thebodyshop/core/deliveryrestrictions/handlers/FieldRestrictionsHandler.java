/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers;

import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public interface FieldRestrictionsHandler<T extends DeliveryRestrictionModel>
{

	boolean isAddressRestricted(AddressModel address, T deliveryRestriction);

	boolean isDeliveryRestrictionNotEmpty(T deliveryRestriction);

}
