/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.daos;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import uk.co.thebodyshop.core.daos.impl.DefaultTbsOrderDao;


/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultTbsOrderDaoTest
{
	private TbsOrderDao dao;
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private OrderModel orderModel;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		dao = new DefaultTbsOrderDao(flexibleSearchService);
		when(flexibleSearchService.searchUnique(any())).thenReturn(orderModel);
	}

	@Test
	public void testGetOrder()
	{
		dao.findOrderByCode("code");
		verify(flexibleSearchService,times(1)).searchUnique(any());
	}

}
