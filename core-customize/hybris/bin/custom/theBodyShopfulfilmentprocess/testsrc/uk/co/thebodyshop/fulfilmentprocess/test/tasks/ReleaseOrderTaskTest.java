/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.test.tasks;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import uk.co.thebodyshop.core.strategies.OrderHoldStrategy;
import uk.co.thebodyshop.fulfilmentprocess.tasks.order.ReleaseOrderTask;


/**
 * @author vasanthramprakasam
 */
public class ReleaseOrderTaskTest
{
	private ReleaseOrderTask releaseOrderTask;
	@Mock
	private OrderHoldStrategy orderHoldStrategy;
	@Mock
	private TaskService taskService;
	@Mock
	private TaskModel taskModel;
	@Mock
	private OrderModel orderModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		releaseOrderTask = new ReleaseOrderTask(orderHoldStrategy);
		when(taskModel.getContextItem()).thenReturn(orderModel);
		doNothing().when(orderHoldStrategy).triggerOrderReleasedEvent(orderModel);
	}

	@Test
	public void testReleaseOrderTask()
	{
		when(orderHoldStrategy.shouldOrderBeHeld(orderModel)).thenReturn(false);
		releaseOrderTask.run(taskService,taskModel);
		verify(orderHoldStrategy,times(1)).triggerOrderReleasedEvent(orderModel);
	}

	@Test(expected = RetryLaterException.class)
	public void testThrowsRetryExceptionIfOrderStillInHold()
	{
		when(orderHoldStrategy.shouldOrderBeHeld(orderModel)).thenReturn(true);
		releaseOrderTask.run(taskService,taskModel);
	}
}
