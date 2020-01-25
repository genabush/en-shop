/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.strategies.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import uk.co.thebodyshop.payment.strategies.CaptureAmountCalculationStrategy;

/**
 * @author Marcin
 */
public class DefaultCaptureAmountCalculationStrategy implements CaptureAmountCalculationStrategy
{
	@Override
	public double calculateCaptureAmount(final OrderModel orderModel, final ConsignmentModel consignmentModel)
	{
		final boolean allShipped = consignmentModel.getConsignmentEntries().stream().allMatch(entry -> Objects.nonNull(entry.getOrderEntry()) && entry.getShippedQuantity() == entry.getQuantity());

		// Full capture scenario
		if (allShipped)
		{
			// assumption is that for TBS each order will only have one consignment
			return getOrderTotalWithTax(orderModel).doubleValue();
		}
		BigDecimal partialCaptureAmount = BigDecimal.ZERO;
		for (final ConsignmentEntryModel consignmentEntry : consignmentModel.getConsignmentEntries())
		{
			if (consignmentEntry.getQuantity().equals(consignmentEntry.getShippedQuantity()))
			{
				partialCaptureAmount = partialCaptureAmount.add(BigDecimal.valueOf(consignmentEntry.getOrderEntry().getTotalPrice()));
			}
			else
			{
				final BigDecimal singleEntryPrice = BigDecimal.valueOf(consignmentEntry.getOrderEntry().getTotalPrice()).divide(BigDecimal.valueOf(consignmentEntry.getQuantity())).setScale(2, RoundingMode.HALF_UP);
				partialCaptureAmount = partialCaptureAmount.add(singleEntryPrice.multiply(BigDecimal.valueOf(consignmentEntry.getShippedQuantity()).setScale(2)));
			}
		}
		partialCaptureAmount = partialCaptureAmount.add(BigDecimal.valueOf(orderModel.getDeliveryCost())).setScale(2);
		return partialCaptureAmount.doubleValue();
	}

	private BigDecimal getOrderTotalWithTax(final OrderModel orderModel)
	{
		if (Objects.isNull(orderModel.getTotalPrice()))
		{
			return BigDecimal.ZERO;
		}
		BigDecimal totalPrice = BigDecimal.valueOf(orderModel.getTotalPrice().doubleValue());
		if (Boolean.TRUE.equals(orderModel.getNet()) && totalPrice.compareTo(BigDecimal.ZERO) != 0 && orderModel.getTotalTax() != null)
		{
			totalPrice = totalPrice.add(BigDecimal.valueOf(orderModel.getTotalTax().doubleValue()));
		}
		return totalPrice;
	}
}
