/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.tasks.order;

import org.springframework.util.Assert;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import uk.co.thebodyshop.core.strategies.OrderHoldStrategy;


/**
 * @author vasanthramprakasam
 */
public class ReleaseOrderTask implements TaskRunner<TaskModel>
{
	private OrderHoldStrategy orderHoldStrategy;

	public ReleaseOrderTask(OrderHoldStrategy orderHoldStrategy)
	{
		this.orderHoldStrategy = orderHoldStrategy;
	}

	@Override
	public void run(TaskService taskService, TaskModel taskModel) throws RetryLaterException
	{
		final OrderModel orderModel = (OrderModel)taskModel.getContextItem();
		Assert.notNull("order","order cannot be null");
		if(getOrderHoldStrategy().shouldOrderBeHeld(orderModel))
		{
			//schedule for later
			throw new RetryLaterException("Order is still in hold time frame");
		}
		getOrderHoldStrategy().triggerOrderReleasedEvent(orderModel);
	}

	@Override
	public void handleError(TaskService taskService, TaskModel taskModel, Throwable throwable)
	{
		//Nothing for now
	}

	protected OrderHoldStrategy getOrderHoldStrategy()
	{
		return orderHoldStrategy;
	}
}
