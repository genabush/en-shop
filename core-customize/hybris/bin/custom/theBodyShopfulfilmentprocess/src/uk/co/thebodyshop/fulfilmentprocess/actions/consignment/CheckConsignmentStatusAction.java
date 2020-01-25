/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;

/**
 * @author vasanthramprakasam
 */
public class CheckConsignmentStatusAction extends AbstractAction<ConsignmentProcessModel>
{
	public enum Transition
	{
		SHIPPED, CANCELLED, WAIT;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<>();

			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final ConsignmentProcessModel consignmentProcess) throws RetryLaterException, Exception
	{
		// Check Order status and payment status and return NOK
		final ConsignmentModel consignment = consignmentProcess.getConsignment();

		Assert.notNull(consignment, "consignment can't be null");

		final ConsignmentStatus consignmentStatus = consignment.getStatus();

		if (consignmentStatus != null && (consignmentStatus.equals(ConsignmentStatus.SHIPPED) || consignmentStatus.equals(ConsignmentStatus.PART_SHIPPED) || consignmentStatus.equals(ConsignmentStatus.PICKUP_COMPLETE)))
		{
			return Transition.SHIPPED.toString();
		}

		if (consignmentStatus != null && (consignmentStatus.equals(ConsignmentStatus.CANCELLED) || consignmentStatus.equals(ConsignmentStatus.NOT_SHIPPED)))
		{
			return Transition.CANCELLED.toString();
		}

		return Transition.WAIT.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
