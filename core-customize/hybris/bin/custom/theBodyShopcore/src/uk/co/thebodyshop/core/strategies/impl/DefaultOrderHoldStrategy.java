/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.strategies.OrderHoldStrategy;

/**
 * @author vasanthramprakasam
 */
public class DefaultOrderHoldStrategy implements OrderHoldStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultOrderHoldStrategy.class);

	private static final String ORDER_RELEASED_EVENT_NAME = "_ReleasedEvent";

	private final TimeService timeService;

	private final ModelService modelService;

	private final BusinessProcessService businessProcessService;

	public DefaultOrderHoldStrategy(final TimeService timeService, final ModelService modelService, final BusinessProcessService businessProcessService)
	{
		this.timeService = timeService;
		this.modelService = modelService;
		this.businessProcessService = businessProcessService;
	}

	@Override
	public Integer getOrderHoldDuration(final OrderModel order)
	{
		Assert.notNull(order, "Order cannot be null");
		Assert.notNull(order.getStore(), "Store of the order cannot be null");

		final BaseStoreModel baseStore = order.getStore();
		final Integer orderHoldDuration = baseStore.getOrderHoldDuration();

		if (orderHoldDuration == null)
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("No order hold duration configured on store [{}], order will not be held", baseStore.getUid());
			}
		}

		return orderHoldDuration;
	}

	@Override
	public boolean shouldOrderBeHeld(final OrderModel order)
	{
		final Integer orderHoldDuration = getOrderHoldDuration(order);

		if (orderHoldDuration == null)
		{
			return false;
		}

		final Date orderCreationTime = order.getCreationtime();

		return Minutes.minutesBetween(new DateTime(orderCreationTime), new DateTime(getTimeService().getCurrentTime())).getMinutes() < orderHoldDuration;
	}

	@Override
	public void holdOrder(final OrderModel order)
	{
		order.setStatus(OrderStatus.ORDER_HELD);
		getModelService().save(order);
	}

	@Override
	public void releaseOrder(final OrderModel order)
	{
		order.setStatus(OrderStatus.ORDER_RELEASED);
		getModelService().save(order);
	}

	@Override
	public void triggerOrderReleasedEvent(final OrderModel order)
	{
		getBusinessProcessService().triggerEvent(order.getCode() + ORDER_RELEASED_EVENT_NAME);
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}
}
