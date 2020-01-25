/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.order;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.orderprocessing.events.PaymentFailedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.event.EventService;


public class SendPaymentFailedNotificationAction extends AbstractProceduralAction<BusinessProcessModel>
{
	private static final Logger LOG = Logger.getLogger(SendPaymentFailedNotificationAction.class);
	private EventService eventService;

	@Override
	public void executeAction(final BusinessProcessModel process)
	{
		OrderProcessModel currentOrderProcess = null;
		String orderCode = "";
		if (process instanceof OrderProcessModel)
		{
			currentOrderProcess = (OrderProcessModel) process;
			orderCode = ((OrderProcessModel) process).getOrder().getCode();
		}
		else if (process instanceof ConsignmentProcessModel)
		{
			currentOrderProcess = ((ConsignmentProcessModel) process).getParentProcess();
			orderCode = ((ConsignmentProcessModel) process).getParentProcess().getOrder().getCode();
		}

		if (LOG.isInfoEnabled())
		{
			LOG.info("Process: " + orderCode + " in step " + getClass());
		}
		getEventService().publishEvent(new PaymentFailedEvent(currentOrderProcess));
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
