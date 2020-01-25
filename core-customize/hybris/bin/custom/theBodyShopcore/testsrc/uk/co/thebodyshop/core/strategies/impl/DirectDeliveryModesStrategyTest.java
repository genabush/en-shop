/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DirectDeliveryModesStrategyTest
{
	private static final String CLICK_AND_COLLECT = "CLICK_AND_COLLECT";

	private static final String COLLECTIONPOINT_DELIVERY_MODE = "collectionpoint-delivery-mode";

	private static final String STANDARD_DELIVERY_MODE = "standard-delivery-mode";

	@Mock
	private DeliveryModeModel deliveryMode1;

	@Mock
	private AbstractOrderModel order;

	@Mock
	private DeliveryModeModel deliveryMode2;

	@Mock
	private DeliveryModeModel deliveryMode3;

	@InjectMocks
	private DirectDeliveryModesStrategy directDeliveryModesStrategy;

	final List<DeliveryModeModel> deliveryModeList = new ArrayList<>();

	@Before
	public void setUp()
	{
		when(deliveryMode1.getCollectionPoint()).thenReturn(Boolean.TRUE);
		when(deliveryMode1.getCode()).thenReturn(COLLECTIONPOINT_DELIVERY_MODE);

		when(deliveryMode2.getCode()).thenReturn(STANDARD_DELIVERY_MODE);

		when(deliveryMode3.getForCollectInStore()).thenReturn(Boolean.TRUE);
		when(deliveryMode3.getCode()).thenReturn(CLICK_AND_COLLECT);

		deliveryModeList.add(deliveryMode1);
		deliveryModeList.add(deliveryMode2);
		deliveryModeList.add(deliveryMode3);
	}

	@Test
	public void testDirectOrder()
	{
		when(order.getFulfillmentMethod()).thenReturn(FulfillmentMethodEnum.DIRECT);
		directDeliveryModesStrategy.filter(deliveryModeList, order);
		Assert.assertTrue(deliveryModeList.size() == 1);
		Assert.assertTrue(deliveryModeList.iterator().next().getCode().equalsIgnoreCase(STANDARD_DELIVERY_MODE));
	}
}
