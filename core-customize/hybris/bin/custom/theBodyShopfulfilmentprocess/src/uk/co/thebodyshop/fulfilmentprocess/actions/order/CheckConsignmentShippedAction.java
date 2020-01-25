/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.order;

import java.util.HashSet;
import java.util.Set;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;

/**
 * @author Balakrishna
 **/
public class CheckConsignmentShippedAction extends AbstractAction<OrderProcessModel>
{

	@Override
	public String execute(final OrderProcessModel orderProcessModel) throws RetryLaterException, Exception
	{
		final OrderModel orderModel = orderProcessModel.getOrder();
		if (OrderStatus.REFUNDED.equals(orderModel.getStatus()))
		{
			return Transition.REFUNDED.toString();
		}
		final boolean isNotShipped = orderModel.getConsignments().stream()
				.allMatch(consignment -> (ConsignmentStatus.CANCELLED.equals(consignment.getStatus()) || ConsignmentStatus.NOT_SHIPPED.equals(consignment.getStatus()) || ConsignmentStatus.NOT_PICKED.equals(consignment.getStatus())));

		return isNotShipped ? Transition.CANCELLED.toString() : Transition.OK.toString();
	}

	@Override
	public Set<String> getTransitions() {
		return Transition.getStringValues();
	}

	public enum Transition {
		OK, CANCELLED, REFUNDED;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<>();

			for (final CheckConsignmentShippedAction.Transition transition : CheckConsignmentShippedAction.Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}
}
