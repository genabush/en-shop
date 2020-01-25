/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.order;

import java.util.HashSet;
import java.util.Set;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractAction;

import uk.co.thebodyshop.integration.svs.model.GiftCardPaymentInfoModel;

/**
 * This action implements payment authorization using {@link CreditCardPaymentInfoModel}. Any other payment model could
 * be implemented here, or in a separate action, if the process flow differs.
 */
public class CheckAuthorizeOrderPaymentAction extends AbstractAction<OrderProcessModel>
{
	public enum Transition
	{
		OK, PENDING, NOK;

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
	public String execute(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		if (order != null)
		{
			if (order.getPaymentInfo() instanceof GiftCardPaymentInfoModel)
			{
				return Transition.OK.toString();
			}
			else
			{
				return assignStatusForOrder(order);
			}
		}
		return Transition.NOK.toString();
	}

	/**
	 * Sets the status for given order in case on of its {@link PaymentTransactionEntryModel} matches proper
	 * {@link PaymentTransactionType} and {@link TransactionStatus}.
	 * @param order {@link OrderModel}
	 * @return {@link Transition}
	 */
	protected String assignStatusForOrder(final OrderModel order)
	{
		for (final PaymentTransactionModel transaction : order.getPaymentTransactions())
		{
			boolean transactionAuthorised = false;
			boolean transactionWithAuthPending = false;
			for (final PaymentTransactionEntryModel entry : transaction.getEntries())
			{
				if (entry.getType().equals(PaymentTransactionType.AUTHORIZATION))
				{
					if (TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
					{
						transactionAuthorised = true;
					}
					if (TransactionStatus.AUTHORIZATION_PENDING.name().equals(entry.getTransactionStatus()))
					{
						transactionWithAuthPending = true;
					}
				}
			}
			if (!transactionAuthorised)
			{
				return processUnauthorizedOrder(order, transactionWithAuthPending);
			}
		}
		order.setStatus(OrderStatus.PAYMENT_AUTHORIZED);
		return Transition.OK.toString();
	}

	/**
	 * @param order
	 * @param transactionWithAuthPending
	 * @return
	 */
	private String processUnauthorizedOrder(final OrderModel order, final boolean transactionWithAuthPending)
	{
		if (transactionWithAuthPending)
		{
			return getTransitionForOrder(order, OrderStatus.PAYMENT_PENDING, Transition.PENDING);
		}
		else
		{
			return getTransitionForOrder(order, OrderStatus.PAYMENT_NOT_AUTHORIZED, Transition.NOK);
		}
	}

	/**
	 * @param order
	 * @return
	 */
	private String getTransitionForOrder(final OrderModel order, final OrderStatus orderStatus, final Transition transition)
	{
		order.setStatus(orderStatus);
		modelService.save(order);
		return transition.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
