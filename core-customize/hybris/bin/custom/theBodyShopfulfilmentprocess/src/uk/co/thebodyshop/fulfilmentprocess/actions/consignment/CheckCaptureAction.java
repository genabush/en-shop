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
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.core.services.order.TbsAcceleratorCheckoutService;
import uk.co.thebodyshop.payment.services.TbsPaymentService;

/**
 * @author Marcin
 */
public class CheckCaptureAction extends AbstractAction<ConsignmentProcessModel>
{
	private static final String CAPTURED = "CAPTURED";

	private static final String NOT_CAPTURED = "NOT_CAPTURED";

	private static final String VOID_CONFIRMATION_REQUIRED = "VOID_CONFIRMATION_REQUIRED";

	private static final String VOID_SUCCESAFULL = "VOID_SUCCESAFULL";

	private static final Logger LOG = LoggerFactory.getLogger(CheckCaptureAction.class);

	private TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService;

	private TbsPaymentService tbsPaymentService;

	public enum Transition
	{
		OK, WAIT, NOK, VOID;

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
		final OrderModel order = consignmentProcess.getParentProcess().getOrder();
		final ConsignmentModel consignment = consignmentProcess.getConsignment();

		try
		{
			final String orderPaymentStatus = getOrderPaymentStatus(order);
			if (CAPTURED.equals(orderPaymentStatus) || VOID_SUCCESAFULL.equals(orderPaymentStatus))
			{
				return processCaptureResult(order, consignment, true);
			}
			else if (VOID_CONFIRMATION_REQUIRED.equals(orderPaymentStatus))
			{
				return Transition.VOID.toString();
			}
			{
				return Transition.WAIT.toString();
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Unable to capture amount for order :: " + order.getCode(), ex);
			return processCaptureResult(order, consignment, false);
		}
	}

	private String getOrderPaymentStatus(final OrderModel order) throws Exception
	{
		for (final PaymentTransactionModel transaction : order.getPaymentTransactions())
		{
			boolean transactionCaptured = false;
			boolean hasVoidTransaction = false;
			boolean hasConfirmedVoidTransaction = false;
			for (final PaymentTransactionEntryModel entry : transaction.getEntries())
			{
				if (entry.getType().equals(PaymentTransactionType.CAPTURE) || entry.getType().equals(PaymentTransactionType.PARTIAL_CAPTURE))
				{
					if (TransactionStatus.REJECTED.name().contentEquals(entry.getTransactionStatus()))
					{
						throw new Exception("Found REJECTED Capture transaction for order :: " + order.getCode());
					}
					if (TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()) && TransactionStatusDetails.SUCCESFULL.name().equals(entry.getTransactionStatusDetails()))
					{
						transactionCaptured = true;
					}
				}
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
				if (hasConfirmedVoidTransaction)
				{
					return VOID_SUCCESAFULL;
				}
				else
				{
					return VOID_CONFIRMATION_REQUIRED;
				}
			}
			if (!transactionCaptured)
			{
				return NOT_CAPTURED;
			}
		}
		return CAPTURED;
	}

	private String processCaptureResult(final OrderModel order, final ConsignmentModel consignmentModel, final boolean succesfullCapture)
	{
		if (succesfullCapture)
		{
			final boolean clickAndCollect = getTbsAcceleratorCheckoutService().isCollectInStoreOrder(order);
			if (clickAndCollect)
			{
				setConsignmentStatus(consignmentModel, ConsignmentStatus.AWAIT_FOR_COLLECTION);
			}
			else
			{
				setConsignmentStatus(consignmentModel, ConsignmentStatus.PAYMENT_CAPTURED);
			}
			return setOrderCaptureSuccess(order, consignmentModel);
		}
		else
		{
			setConsignmentStatus(consignmentModel, ConsignmentStatus.PAYMENT_NOT_CAPTURED);
			setPaymentStatus(order, PaymentStatus.NOTPAID);
			setOrderStatus(order,OrderStatus.PAYMENT_NOT_CAPTURED);
			return Transition.NOK.toString();
		}
	}

	private void setConsignmentStatus(final ConsignmentModel consignmentModel, final ConsignmentStatus paymentCaptured)
	{
		consignmentModel.setStatus(paymentCaptured);
		getModelService().save(consignmentModel);
	}

	private String setOrderCaptureSuccess(final OrderModel order, final ConsignmentModel consignmentModel)
	{
		if (getTbsPaymentService().isFullOrderCapture(order))
		{
			setPaymentStatus(order, PaymentStatus.PAID);
		}
		else
		{
			setPaymentStatus(order, PaymentStatus.PARTPAID);
		}
		return Transition.OK.toString();
	}

	private void setPaymentStatus(final OrderModel order, final PaymentStatus paymentStatus)
	{
		order.setPaymentStatus(paymentStatus);
		getModelService().save(order);
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	/**
	 * @return the tbsAcceleratorCheckoutService
	 */
	public TbsAcceleratorCheckoutService getTbsAcceleratorCheckoutService()
	{
		return tbsAcceleratorCheckoutService;
	}

	/**
	 * @param tbsAcceleratorCheckoutService
	 *          the tbsAcceleratorCheckoutService to set
	 */
	public void setTbsAcceleratorCheckoutService(final TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService)
	{
		this.tbsAcceleratorCheckoutService = tbsAcceleratorCheckoutService;
	}

	/**
	 * @return the tbsPaymentService
	 */
	public TbsPaymentService getTbsPaymentService()
	{
		return tbsPaymentService;
	}

	/**
	 * @param tbsPaymentService the tbsPaymentService to set
	 */
	public void setTbsPaymentService(final TbsPaymentService tbsPaymentService)
	{
		this.tbsPaymentService = tbsPaymentService;
	}
}
