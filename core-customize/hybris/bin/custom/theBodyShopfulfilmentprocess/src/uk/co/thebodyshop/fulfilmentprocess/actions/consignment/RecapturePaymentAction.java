/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import org.apache.log4j.Logger;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.payment.strategies.RecapturePrimaryPaymentStrategy;

/**
 * @author Marcin
 */
public class RecapturePaymentAction extends AbstractSimpleDecisionAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(RecapturePaymentAction.class);

	private RecapturePrimaryPaymentStrategy recapturePrimaryPaymentStrategy;

	@Override
	public Transition executeAction(final ConsignmentProcessModel consignmentProcess) throws RetryLaterException, Exception
	{
		final OrderModel orderModel = consignmentProcess.getParentProcess().getOrder();
		if (recapturePrimaryPaymentStrategy.capturePrimaryPayment(orderModel))
		{
			return Transition.OK;
		}
		return Transition.NOK;
	}

	/**
	 * @return the recapturePrimaryPaymentStrategy
	 */
	protected RecapturePrimaryPaymentStrategy getRecapturePrimaryPaymentStrategy()
	{
		return recapturePrimaryPaymentStrategy;
	}

	/**
	 * @param recapturePrimaryPaymentStrategy
	 *          the recapturePrimaryPaymentStrategy to set
	 */
	public void setRecapturePrimaryPaymentStrategy(final RecapturePrimaryPaymentStrategy recapturePrimaryPaymentStrategy)
	{
		this.recapturePrimaryPaymentStrategy = recapturePrimaryPaymentStrategy;
	}
}
