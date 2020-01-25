/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.strategies.impl.DefaultOrderHoldStrategy;


/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultOrderHoldStrategyTest
{
	@InjectMocks
	private DefaultOrderHoldStrategy strategy;
	@Mock
	private TimeService timeService;
	@Mock
	private ModelService modelService;
	@Mock
	private BusinessProcessService businessProcessService;
	@Mock
	private OrderModel orderModel;
	@Spy
	private BaseStoreModel baseStoreModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(orderModel.getStore()).thenReturn(baseStoreModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetOrderHoldDurationArguments()
	{
		when(orderModel.getStore()).thenReturn(null);
		strategy.getOrderHoldDuration(orderModel);
	}

	@Test
	public void testGetOrderHoldDuration()
	{
		baseStoreModel.setOrderHoldDuration(2);
		assertTrue(strategy.getOrderHoldDuration(orderModel) == 2);
	}

	@Test
	public void testShouldHoldOrder()
	{
		baseStoreModel.setOrderHoldDuration(2);
		when(orderModel.getCreationtime()).thenReturn(DateTime.now().minusMinutes(1).toDate());
		when(timeService.getCurrentTime()).thenReturn(new Date());
		assertTrue(strategy.shouldOrderBeHeld(orderModel));
	}

	@Test
	public void testShouldNotHoldOrder()
	{
		baseStoreModel.setOrderHoldDuration(2);
		when(orderModel.getCreationtime()).thenReturn(DateTime.now().minusMinutes(3).toDate());
		when(timeService.getCurrentTime()).thenReturn(new Date());
		assertFalse(strategy.shouldOrderBeHeld(orderModel));
	}

	@Test
	public void testHoldOrder()
	{
		doNothing().when(modelService).save(any(OrderModel.class));
		final ArgumentCaptor<OrderModel> captor = ArgumentCaptor.forClass(OrderModel.class);
		strategy.holdOrder(orderModel);
		verify(modelService, times(1)).save(captor.capture());
		verify(orderModel, times(1)).setStatus(OrderStatus.ORDER_HELD);
	}

	@Test
	public void testReleaseOrder()
	{
		doNothing().when(modelService).save(any(OrderModel.class));
		final ArgumentCaptor<OrderModel> captor = ArgumentCaptor.forClass(OrderModel.class);
		strategy.releaseOrder(orderModel);
		verify(modelService, times(1)).save(captor.capture());
	}

	@Test
	public void testTriggerOrderReleaseEvent()
	{
		doNothing().when(modelService).save(any(OrderModel.class));
		doNothing().when(businessProcessService).triggerEvent(anyString());
		strategy.triggerOrderReleasedEvent(orderModel);
		verify(businessProcessService,times(1)).triggerEvent(anyString());
	}
}
