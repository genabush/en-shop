/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.delivery.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.commerceservices.delivery.impl.DefaultDeliveryService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import uk.co.thebodyshop.core.delivery.TbsDeliveryService;
import uk.co.thebodyshop.core.deliveryrestrictions.error.DeliveryRestrictionError;
import uk.co.thebodyshop.core.strategies.DeliveryModeRestrictionStrategy;

/**
 * @author prateek.goel
 */
public class DefaultTbsDeliveryService extends DefaultDeliveryService implements TbsDeliveryService
{
	private List<DeliveryModeRestrictionStrategy> deliveryModeRestrictionStrategies;

	private Map<String, String> deliveryModeStrategyErrorMapping;

	@Override
	public List<DeliveryModeModel> getSupportedDeliveryModeListForOrder(final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");
		final List<DeliveryModeModel> deliveryModes = getDeliveryModeLookupStrategy().getSelectableDeliveryModesForOrder(abstractOrder);
		for (final DeliveryModeRestrictionStrategy strategy : getDeliveryModeRestrictionStrategies())
		{
			strategy.filter(deliveryModes, abstractOrder);
		}
		sortDeliveryModes(deliveryModes, abstractOrder);
		return deliveryModes;
	}

	@Override
	public Map<DeliveryRestrictionError, List<DeliveryModeModel>> getDeliveryModesWithDeliveryRestrictionError(final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");
		final Map<DeliveryRestrictionError, List<DeliveryModeModel>> deliveryModesWithDeliveryRestrictionError = new HashMap<>();
		final List<DeliveryModeModel> deliveryModes = getDeliveryModeLookupStrategy().getSelectableDeliveryModesForOrder(abstractOrder);
		final List<String> restrictionErrorList = new ArrayList<>();
		final DeliveryRestrictionError deliveryRestrictionError = new DeliveryRestrictionError();
		for (final DeliveryModeRestrictionStrategy strategy : getDeliveryModeRestrictionStrategies())
		{
			final boolean filtered = strategy.filter(deliveryModes, abstractOrder);
			if (filtered)
			{
				final String error = getDeliveryModeStrategyErrorMapping().get(strategy.getClass().getSimpleName());
				if (StringUtils.isNotBlank(error))
				{
					restrictionErrorList.add(error);
				}
			}
		}
		sortDeliveryModes(deliveryModes, abstractOrder);
		deliveryRestrictionError.setErrorList(restrictionErrorList);
		deliveryModesWithDeliveryRestrictionError.put(deliveryRestrictionError, deliveryModes);
		return deliveryModesWithDeliveryRestrictionError;
	}

	protected List<DeliveryModeRestrictionStrategy> getDeliveryModeRestrictionStrategies()
	{
		return deliveryModeRestrictionStrategies;
	}

	public void setDeliveryModeRestrictionStrategies(final List<DeliveryModeRestrictionStrategy> deliveryModeRestrictionStrategies)
	{
		this.deliveryModeRestrictionStrategies = deliveryModeRestrictionStrategies;
	}

	protected Map<String, String> getDeliveryModeStrategyErrorMapping()
	{
		return deliveryModeStrategyErrorMapping;
	}

	public void setDeliveryModeStrategyErrorMapping(final Map<String, String> deliveryModeStrategyErrorMapping)
	{
		this.deliveryModeStrategyErrorMapping = deliveryModeStrategyErrorMapping;
	}

}
