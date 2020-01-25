/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.strategies.DeliveryModeRestrictionStrategy;

/**
 * @author prateek.goel
 */
public class HazmatDeliveryModeRestrictionStrategy implements DeliveryModeRestrictionStrategy
{

	@Override
	public boolean filter(final List<DeliveryModeModel> deliveryModes, final AbstractOrderModel order)
	{
		final boolean isHazardProductAvailable = order.getEntries().stream().filter(isHazardEntry()).findAny().isPresent();
		if (isHazardProductAvailable && CollectionUtils.isNotEmpty(deliveryModes))
		{
			final List<DeliveryModeModel> restrictedDeliveryModes = new ArrayList<>();
			for (final DeliveryModeModel deliveryMode : deliveryModes)
			{
				if (deliveryMode.isRestrictedForHazardousProducts())
				{
					restrictedDeliveryModes.add(deliveryMode);
				}
			}
			if (CollectionUtils.isNotEmpty(restrictedDeliveryModes))
			{
				deliveryModes.removeAll(restrictedDeliveryModes);
				return true;
			}
		}
		return false;
	}

	public static Predicate<AbstractOrderEntryModel> isHazardEntry()
	{
		return entry -> null != entry.getProduct() && entry.getProduct() instanceof VariantProductModel && BooleanUtils.isTrue(((VariantProductModel) entry.getProduct()).getErpHazardous());
	}

}
