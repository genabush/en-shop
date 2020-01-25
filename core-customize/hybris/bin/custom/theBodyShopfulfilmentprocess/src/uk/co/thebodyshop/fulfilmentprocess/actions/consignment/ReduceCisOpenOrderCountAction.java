/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import java.util.HashSet;
import java.util.Set;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.core.services.order.TbsAcceleratorCheckoutService;

/**
 * @author prateek.goel
 */
public class ReduceCisOpenOrderCountAction extends AbstractAction<ConsignmentProcessModel>
{

	private TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService;

	public enum Transition
	{
		COLLECTED, NOT_COLLECTED, ERROR;

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
	public String execute(final ConsignmentProcessModel process) throws RetryLaterException, Exception
	{
		final ConsignmentModel consignment = process.getConsignment();
		final ConsignmentStatus consignmentStatus = consignment.getStatus();
		final AbstractOrderModel order = consignment.getOrder();
		getTbsAcceleratorCheckoutService().updateCisOrders(order.getDeliveryPointOfService(), false, true);
		if (consignmentStatus.equals(ConsignmentStatus.COLLECTED))
		{
			return Transition.COLLECTED.toString();
		}
		if (consignmentStatus.equals(ConsignmentStatus.NOT_COLLECTED))
		{
			return Transition.NOT_COLLECTED.toString();
		}
		return Transition.ERROR.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	protected TbsAcceleratorCheckoutService getTbsAcceleratorCheckoutService()
	{
		return tbsAcceleratorCheckoutService;
	}

	public void setTbsAcceleratorCheckoutService(final TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService)
	{
		this.tbsAcceleratorCheckoutService = tbsAcceleratorCheckoutService;
	}
}