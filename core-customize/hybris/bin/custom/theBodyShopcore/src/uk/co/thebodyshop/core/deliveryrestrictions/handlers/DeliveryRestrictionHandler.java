/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers;

import java.util.Collection;

import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public interface DeliveryRestrictionHandler<T extends DeliveryRestrictionModel>
{

	boolean isDeliveryModeRestricted(AddressModel address, Collection<T> deliveryRestrictions);
}
