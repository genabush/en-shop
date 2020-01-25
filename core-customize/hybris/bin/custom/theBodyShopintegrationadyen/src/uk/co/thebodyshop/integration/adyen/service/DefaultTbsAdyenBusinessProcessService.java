/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import org.apache.log4j.Logger;

import com.adyen.v6.service.DefaultAdyenBusinessProcessService;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

/**
 * @author Marcin
 */
public class DefaultTbsAdyenBusinessProcessService extends DefaultAdyenBusinessProcessService implements TbsAdyenBusinessProcessService
{
	private static final Logger LOG = Logger.getLogger(DefaultTbsAdyenBusinessProcessService.class);

	@Override
	public void triggerOrderProcessEvent(final OrderModel orderModel, final String event)
	{
		final String eventName = orderModel.getCode() + "_" + event;
		LOG.debug("Sending event:" + eventName);
		getBusinessProcessService().triggerEvent(eventName);
	}

	@Override
	public void triggerConsignmentProcessEvent(final ConsignmentModel consignmentModel, final String event)
	{
		if (null != consignmentModel)
		{
			final String eventName = consignmentModel.getCode() + "_" + event;
			LOG.debug("Sending event:" + eventName);
			getBusinessProcessService().triggerEvent(eventName);
		}
	}
}
