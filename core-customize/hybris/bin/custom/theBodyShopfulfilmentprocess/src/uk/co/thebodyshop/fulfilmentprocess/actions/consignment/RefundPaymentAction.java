/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import uk.co.thebodyshop.core.enums.ConsignmentEntryStatus;
import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;

/**
 * @author Marcin
 */
public class RefundPaymentAction extends AbstractAction<ConsignmentProcessModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(RefundPaymentAction.class);

	private final LoyaltyService loyaltyService;

	public enum Transition
	{
		OK, NOK;

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
	public String execute(final ConsignmentProcessModel consignmentProcess)
	{
		if (LOG.isInfoEnabled())
		{
			LOG.info("Executing consigment refund action for consignment process with code [{}]", consignmentProcess.getCode());
		}

		final ConsignmentModel consignmentModel = consignmentProcess.getConsignment();

		consignmentModel.setStatus(ConsignmentStatus.REFUNDED);
		getModelService().save(consignmentModel);

		for (final ConsignmentEntryModel consignmentEntry : consignmentModel.getConsignmentEntries())
		{
			consignmentEntry.setStatus(ConsignmentEntryStatus.REFUNDED);
			getModelService().save(consignmentEntry);
		}

		final OrderModel order = consignmentProcess.getParentProcess().getOrder();
		order.setStatus(OrderStatus.REFUNDED);
		getModelService().save(order);
		getLoyaltyService().updateLoyaltyVouchersStatus(order.getAppliedLoyaltyVouchers(), BenefitStatus.ACTIVE);

		return Transition.OK.toString();
	}

	public RefundPaymentAction(final LoyaltyService loyaltyService)
	{
		this.loyaltyService = loyaltyService;
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	/**
	 * @return the loyaltyService
	 */
	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}
}
