/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;

/**
 * @author vasanthramprakasam
 */
public class CheckConsignmentClickAndCollectAction extends AbstractAction<ConsignmentProcessModel>
{

	private static final Logger LOG = LoggerFactory.getLogger(CheckConsignmentClickAndCollectAction.class);

	private List<ConsignmentStatus> consignmentStatusesToWait;

	public enum Transition
	{
		WAIT_SHIPMENT, WAIT_STATUS, COLLECTED, NOT_COLLECTED, CANCELLED, ERROR, PICKED, NOT_PICKED;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final ConsignmentProcessModel consignmentProcess) throws RetryLaterException, Exception
	{
		final ConsignmentModel consignment = consignmentProcess.getConsignment();

		final ConsignmentStatus consignmentStatus = consignment.getStatus();

		if (consignmentStatus.equals(ConsignmentStatus.READY_FOR_PICKING))
		{
			return Transition.WAIT_SHIPMENT.toString();
		}
		if (getConsignmentStatusesToWait().contains(consignmentStatus))
		{
			return Transition.WAIT_STATUS.toString();
		}
		if (consignmentStatus.equals(ConsignmentStatus.PICKUP_COMPLETE) || consignmentStatus.equals(ConsignmentStatus.PART_PICKED))
		{
			return Transition.PICKED.toString();
		}
		if (consignmentStatus.equals(ConsignmentStatus.NOT_PICKED))
		{
			return Transition.NOT_PICKED.toString();
		}
		if (consignmentStatus.equals(ConsignmentStatus.COLLECTED))
		{
			return Transition.COLLECTED.toString();
		}
		if (consignmentStatus.equals(ConsignmentStatus.NOT_COLLECTED))
		{
			return Transition.NOT_COLLECTED.toString();
		}
		if (consignmentStatus.equals(ConsignmentStatus.CANCELLED))
		{
			return Transition.CANCELLED.toString();
		}

		LOG.error("Consignment with code [{}] not in the correct status", consignment.getCode());

		return Transition.ERROR.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return CheckConsignmentClickAndCollectAction.Transition.getStringValues();
	}

	public List<ConsignmentStatus> getConsignmentStatusesToWait()
	{
		return consignmentStatusesToWait;
	}

	@Required
	public void setConsignmentStatusesToWait(final List<ConsignmentStatus> consignmentStatusesToWait)
	{
		this.consignmentStatusesToWait = consignmentStatusesToWait;
	}
}
