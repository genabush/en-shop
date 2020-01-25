/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import uk.co.thebodyshop.core.strategies.OrderHoldStrategy;
import uk.co.thebodyshop.fulfilmentprocess.actions.order.CheckOrderHoldAction;


/**
 * @author vasanthramprakasam
 */
@UnitTest
public class CheckOrderHoldActionTest
{
	private CheckOrderHoldAction checkOrderHoldAction;
	@Mock
	private OrderHoldStrategy orderHoldStrategy;
   @Mock
	private TaskService taskService;
	@Mock
	private TimeService timeService;
	@Mock
	private ModelService modelService;
	@Spy
	private OrderProcessModel orderProcessModel;
	@Spy
	private OrderModel orderModel;
	@Spy
	private TaskModel taskModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		checkOrderHoldAction = new CheckOrderHoldAction(orderHoldStrategy,taskService,timeService);
		checkOrderHoldAction.setModelService(modelService);
		orderProcessModel.setOrder(orderModel);
		taskModel = new TaskModel();
		when(modelService.create((Class) any())).thenReturn(taskModel);
		when(timeService.getCurrentTime()).thenReturn(new Date());
		doNothing().when(taskService).scheduleTask(any(TaskModel.class));
		doNothing().when(modelService).save(any(TaskModel.class));
	}

	@Test
	public void testHoldOrder() throws RetryLaterException, Exception
	{
		when(orderHoldStrategy.shouldOrderBeHeld(orderModel)).thenReturn(true);
		when(orderHoldStrategy.getOrderHoldDuration(orderModel)).thenReturn(2);
		checkOrderHoldAction.execute(orderProcessModel);
		verify(orderHoldStrategy,times(1)).holdOrder(orderModel);
		verify(modelService,times(1)).create((Class)any());
		verify(modelService,times(1)).save(taskModel);
		verify(taskService,times(1)).scheduleTask(taskModel);
	}

	@Test
	public void testReleaseOrder() throws RetryLaterException, Exception
	{
		when(orderHoldStrategy.shouldOrderBeHeld(orderModel)).thenReturn(false);
		checkOrderHoldAction.execute(orderProcessModel);
		verify(orderHoldStrategy,never()).holdOrder(orderModel);
		verify(modelService,never()).create((Class)any());
		verify(modelService,never()).save(taskModel);
		verify(taskService,never()).scheduleTask(taskModel);
		verify(orderHoldStrategy,times(1)).releaseOrder(orderModel);
	}
}
