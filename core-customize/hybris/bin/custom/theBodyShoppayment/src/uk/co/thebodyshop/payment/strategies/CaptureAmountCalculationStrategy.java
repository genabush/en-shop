/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.strategies;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

/**
 * @author Marcin
 */
public interface CaptureAmountCalculationStrategy
{
	/**
	 * Calculates the order capture amount based on the order consignments shipped status If all consignments order entries were successfully shipped capture will be set to order total with tax If some
	 * consignments were shipped capture will be calculates based on the formula (orderEntryTotal/quantity) * shipped quantity
	 *
	 * @param order
	 *          - current order
	 * @param consignmentModel
	 *          - current consignment
	 * @return calculated consignment capture amount
	 */
	double calculateCaptureAmount(final OrderModel order, final ConsignmentModel consignmentModel);
}
