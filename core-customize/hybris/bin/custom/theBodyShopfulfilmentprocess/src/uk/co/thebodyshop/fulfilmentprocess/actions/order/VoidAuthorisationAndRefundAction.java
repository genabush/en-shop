/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.order;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.fulfilmentprocess.service.VoidAuthorizeService;
import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;

/**
 * @author Jagadeesh
 */
public class VoidAuthorisationAndRefundAction extends AbstractAction<OrderProcessModel>
{

	private static final Logger LOG= LoggerFactory.getLogger(VoidAuthorisationAndRefundAction.class);

	private final VoidAuthorizeService voidAuthorizeService;

	private final LoyaltyService loyaltyService;

	public enum Transition
	{
		OK, NOK, WAIT;

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
	public String execute(final OrderProcessModel order) throws RetryLaterException, Exception
	{
		final OrderModel orderModel =order.getOrder();
		getLoyaltyService().updateLoyaltyVouchersStatus(orderModel.getAppliedLoyaltyVouchers(), BenefitStatus.ACTIVE);

		final boolean result = getVoidAuthorizeService().voidAuthorize(orderModel);
		if (!result)
		{
			return Transition.NOK.toString();
		}
		if (wasVoidAuthorizeSuccesfull(orderModel))
		{
			orderModel.setPaymentStatus(PaymentStatus.CANCELLED);
			getModelService().save(orderModel);
			return Transition.OK.toString();
		}
		return Transition.WAIT.toString();
	}

	private boolean wasVoidAuthorizeSuccesfull(final OrderModel order) throws Exception
	{
		for (final PaymentTransactionModel transaction : order.getPaymentTransactions())
		{
			boolean hasVoidTransaction = false;
			boolean hasConfirmedVoidTransaction = false;
			for (final PaymentTransactionEntryModel entry : transaction.getEntries())
			{
				if (entry.getType().equals(PaymentTransactionType.CANCEL))
				{
					hasVoidTransaction = true;
					if (TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()) && TransactionStatusDetails.SUCCESFULL.name().equals(entry.getTransactionStatusDetails()))
					{
						hasConfirmedVoidTransaction = true;
					}
				}
			}
			if (hasVoidTransaction)
			{
				if (!hasConfirmedVoidTransaction)
				{
					return false;
				}
			}
		}
		return true;
	}

	public VoidAuthorisationAndRefundAction(final VoidAuthorizeService voidAuthorizeService, final LoyaltyService loyaltyService)
	{
		this.voidAuthorizeService = voidAuthorizeService;
		this.loyaltyService = loyaltyService;
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	/**
	 * @return the voidAuthorizeService
	 */
	protected VoidAuthorizeService getVoidAuthorizeService()
	{
		return voidAuthorizeService;
	}

	/**
	 * @return the loyaltyService
	 */
	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}
}