/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.delivery;

import java.util.List;
import java.util.Map;

import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import uk.co.thebodyshop.core.deliveryrestrictions.error.DeliveryRestrictionError;

/**
 * @author prateek.goel
 */
public interface TbsDeliveryService extends DeliveryService
{

	Map<DeliveryRestrictionError, List<DeliveryModeModel>> getDeliveryModesWithDeliveryRestrictionError(final AbstractOrderModel abstractOrder);

}
