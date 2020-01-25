/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;

import uk.co.thebodyshop.core.daos.TbsOrderDao;
import uk.co.thebodyshop.core.services.TbsOrderService;
/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultTbsOrderServiceTest
{
	private TbsOrderService tbsOrderService;
	@Mock
	private TbsOrderDao tbsOrderDao;
	@Mock
	private OrderModel orderModel;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		tbsOrderService = new DefaultTbsOrderService(tbsOrderDao);
		when(tbsOrderDao.findOrderByCode("code")).thenReturn(orderModel);
	}

	@Test
	public void testGetOrder()
	{
		tbsOrderService.getOrderForCode("code");
		verify(tbsOrderDao,times(1)).findOrderByCode("code");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetOrderNullCode()
	{
		tbsOrderService.getOrderForCode(null);
		verify(tbsOrderDao,never()).findOrderByCode(anyString());
	}
}
