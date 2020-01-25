/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.actions.order;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.hybris.platform.core.enums.OrderStatus;
import org.joda.time.DateTime;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import uk.co.thebodyshop.core.strategies.OrderHoldStrategy;


/**
 * @author vasanthramprakasam
 */
public class CheckOrderHoldAction extends AbstractAction<OrderProcessModel>
{

	private static final String RELEASE_TASK_RUNNER = "releaseOrderTaskRunner";

	private OrderHoldStrategy orderHoldStrategy;

	private TaskService taskService;

	private TimeService timeService;

	public CheckOrderHoldAction(OrderHoldStrategy orderHoldStrategy, TaskService taskService,
			TimeService timeService)
	{
		this.orderHoldStrategy = orderHoldStrategy;
		this.taskService = taskService;
		this.timeService = timeService;
	}

	public enum Transition
	{
		OK, HOLD,CANCELLED;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();
			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(OrderProcessModel orderProcessModel) throws RetryLaterException, Exception
	{
		final OrderModel order = orderProcessModel.getOrder();
		if(OrderStatus.CANCELLED.equals(order.getStatus()))
		{
			return Transition.CANCELLED.toString();
		}
		final boolean shouldOrderBeHeld = getOrderHoldStrategy().shouldOrderBeHeld(order);
		if (shouldOrderBeHeld)
		{
			getOrderHoldStrategy().holdOrder(order);
			scheduleReleaseOrderTask(order);
			return Transition.HOLD.toString();
		}
		getOrderHoldStrategy().releaseOrder(order);
		return Transition.OK.toString();
	}

	protected void scheduleReleaseOrderTask(final OrderModel order)
	{
		final Integer holdDuration = getOrderHoldStrategy().getOrderHoldDuration(order);
		if (holdDuration != null)
		{
			TaskModel releaseOrderTask = getModelService().create(TaskModel.class);
			releaseOrderTask.setRunnerBean(RELEASE_TASK_RUNNER);
			Date tobeScheduledTime = new DateTime(getTimeService().getCurrentTime()).plusMinutes(holdDuration).toDate();
			releaseOrderTask.setExecutionDate(tobeScheduledTime);
			releaseOrderTask.setContextItem(order);
			getModelService().save(releaseOrderTask);
			getTaskService().scheduleTask(releaseOrderTask);
		}
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	protected OrderHoldStrategy getOrderHoldStrategy()
	{
		return orderHoldStrategy;
	}

	protected TaskService getTaskService()
	{
		return taskService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}
}
