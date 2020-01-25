/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.manager;

import java.util.List;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;

import uk.co.thebodyshop.core.deliveryrestrictions.error.ProductDeliveryRestrictionError;

/**
 * @author prateek.goel
 */
public interface RestrictionsManager
{

	List<DeliveryModeModel> handleDeliveryModeRestrictions(final AddressModel address, final List<DeliveryModeModel> deliveryModes);

	ProductDeliveryRestrictionError handleProductDeliveryModeRestrictions(final AddressModel address, final CartModel cart);

}
