/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.strategies;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import uk.co.thebodyshop.payment.strategies.impl.DefaultCaptureAmountCalculationStrategy;

/**
 * @author Marcin
 */
@UnitTest
public class CaptureAmountCalculationStrategyTest
{

	private DefaultCaptureAmountCalculationStrategy captureAmountCalculationStrategy;

	private OrderModel order;

	private Set<ConsignmentModel> consignments;

	private ConsignmentModel consignment;

	private Set<ConsignmentEntryModel> consignmentEntries;

	private ConsignmentEntryModel consignmentEntry1;

	private ConsignmentEntryModel consignmentEntry2;

	private OrderEntryModel orderEntry1;

	private OrderEntryModel orderEntry2;

	@Before
	public void setUp()
	{
		// MockitoAnnotations.initMocks(this);
		captureAmountCalculationStrategy = new DefaultCaptureAmountCalculationStrategy();
		order = new OrderModel();
		order.setDeliveryCost(Double.valueOf(0));
		consignment = new ConsignmentModel();
		consignmentEntries = new HashSet<>();
		consignments = new HashSet<>();
		consignmentEntry1 = new ConsignmentEntryModel();
		orderEntry1 = new OrderEntryModel();
		orderEntry1.setTotalPrice(Double.valueOf(30));
		consignmentEntry1.setOrderEntry(orderEntry1);
		consignmentEntry2 = new ConsignmentEntryModel();
		orderEntry2 = new OrderEntryModel();
		orderEntry2.setTotalPrice(Double.valueOf(60));
		consignmentEntry2.setOrderEntry(orderEntry2);
		consignmentEntries.add(consignmentEntry1);
		consignmentEntries.add(consignmentEntry2);
		consignment.setConsignmentEntries(consignmentEntries);
		consignments.add(consignment);
		order.setConsignments(consignments);
	}

	@Test
	public void testCalculateCaptureFullGrossOrderAmount()
	{
		order.setTotalPrice(Double.valueOf(90));
		order.setTotalTax(Double.valueOf(0));
		order.setNet(false);
		consignment.setStatus(ConsignmentStatus.SHIPPED);
		consignmentEntry1.setQuantity(Long.valueOf(1));
		consignmentEntry1.setShippedQuantity(Long.valueOf(1));
		consignmentEntry2.setQuantity(Long.valueOf(2));
		consignmentEntry2.setShippedQuantity(Long.valueOf(2));

		final double calculatedCapture = captureAmountCalculationStrategy.calculateCaptureAmount(order, consignment);
		assertEquals(calculatedCapture, 90, 0);
	}

	@Test
	public void testCalculateCaptureFullNetOrderAmount()
	{
		order.setTotalPrice(Double.valueOf(90));
		order.setTotalTax(Double.valueOf(10));
		order.setNet(true);
		consignment.setStatus(ConsignmentStatus.SHIPPED);
		consignmentEntry1.setQuantity(Long.valueOf(1));
		consignmentEntry1.setShippedQuantity(Long.valueOf(1));
		consignmentEntry2.setQuantity(Long.valueOf(2));
		consignmentEntry2.setShippedQuantity(Long.valueOf(2));

		final double calculatedCapture = captureAmountCalculationStrategy.calculateCaptureAmount(order, consignment);
		assertEquals(calculatedCapture, 100, 0);
	}

	@Test
	public void testCalculateCapturePartialOrderAmount()
	{
		order.setTotalPrice(Double.valueOf(90));
		order.setTotalTax(Double.valueOf(0));
		order.setNet(true);
		consignment.setStatus(ConsignmentStatus.NOT_SHIPPED);
		consignmentEntry1.setQuantity(Long.valueOf(1));
		consignmentEntry1.setShippedQuantity(Long.valueOf(1));
		consignmentEntry2.setQuantity(Long.valueOf(2));
		consignmentEntry2.setShippedQuantity(Long.valueOf(1));

		final double calculatedCapture = captureAmountCalculationStrategy.calculateCaptureAmount(order, consignment);
		assertEquals(calculatedCapture, 60, 0);
	}
}
