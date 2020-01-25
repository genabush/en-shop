/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.strategies.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Iterables;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.payment.services.TbsPaymentService;
import uk.co.thebodyshop.payment.strategies.RecapturePrimaryPaymentStrategy;

/**
 * @author Marcin
 */
public class DefaultRecapturePrimaryPaymentStrategy implements RecapturePrimaryPaymentStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultRecapturePrimaryPaymentStrategy.class);

	private final TbsPaymentService paymentService;

	private final ModelService modelService;

	@Override
	public boolean capturePrimaryPayment(final OrderModel orderModel)
	{
		boolean primaryPaymentCaptured = true;
		try
		{
			final List<PaymentTransactionEntryModel> failedCaptureEntries = getPaymentService().getPrimaryFailedCapturePaymentTransactionEntries(orderModel.getPaymentTransactions());
			if (CollectionUtils.isNotEmpty(failedCaptureEntries))
			{
				for (final PaymentTransactionEntryModel failedCaptureEntry : failedCaptureEntries)
				{
					final PaymentTransactionModel failedCaptureTransaction = failedCaptureEntry.getPaymentTransaction();
					final PaymentTransactionEntryModel capturePaymentEntry = getPaymentService().capture(failedCaptureTransaction, failedCaptureEntry.getAmount().setScale(2).doubleValue());
					if (TransactionStatus.ACCEPTED.name().equals(capturePaymentEntry.getTransactionStatus()))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("The payment transaction has been captured. Order: " + orderModel.getCode() + ". Txn: " + failedCaptureTransaction.getCode());
						}
					}
					else
					{
						primaryPaymentCaptured = false;
						LOG.error("The payment transaction capture has failed. Order: " + orderModel.getCode() + ". Txn: " + failedCaptureTransaction.getCode());
					}
				}
			}
			else
			{
				LOG.error("No failed primary payment transaction entry was found for order: " + orderModel.getCode());
				primaryPaymentCaptured = false;
			}
			markOrderWithCaptureInfo(orderModel, primaryPaymentCaptured);
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			handleRecaptureFailure(orderModel);
			primaryPaymentCaptured = false;
		}
		return primaryPaymentCaptured;
	}

	private void markOrderWithCaptureInfo(final OrderModel orderModel, final boolean primaryPaymentCaptured)
	{
		if (primaryPaymentCaptured)
		{
			LOG.info("Succesfull re-attempt CAPTURE for order :: " + orderModel.getCode());
			handleRecaptureSuccess(orderModel);
		}
		else
		{
			LOG.error("Unsuccesfull re-attempt CAPTURE for order :: " + orderModel.getCode());
			handleRecaptureFailure(orderModel);
		}
	}

	private void handleRecaptureSuccess(final OrderModel orderModel)
	{
		setOrderStatusForCaptureStatus(orderModel, true);

	}

	private void handleRecaptureFailure(final OrderModel orderModel)
	{
		setOrderStatusForCaptureStatus(orderModel, false);
	}

	private void setOrderStatusForCaptureStatus(final OrderModel order, final boolean capturedPrimaryPayment)
	{
		if (capturedPrimaryPayment)
		{
			// TODO set first consignment status for now,review later when using multiple consignments
			final ConsignmentModel consignment = Iterables.getFirst(order.getConsignments(), null);
			setOrderAndPaymentStatus(order, OrderStatus.PAYMENT_CAPTURE_CHECK_REQUIRED, PaymentStatus.CAPTURE_PENDING);
			setConsignmentStatus(consignment, ConsignmentStatus.PAYMENT_CAPTURE_CHECK_REQUIRED);
		}
		else
		{
			setOrderAndPaymentStatus(order, OrderStatus.PAYMENT_FAILED, PaymentStatus.NOTPAID);
		}
	}

	private void setOrderAndPaymentStatus(final OrderModel order, final OrderStatus orderStatus, final PaymentStatus paymentStatus)
	{
		order.setPaymentStatus(paymentStatus);
		order.setStatus(orderStatus);
		getModelService().save(order);
	}

	private void setConsignmentStatus(final ConsignmentModel consignment, final ConsignmentStatus consignmentStatus)
	{
		if(consignment != null)
		{
			consignment.setStatus(consignmentStatus);
			getModelService().save(consignment);
		}
	}

	public DefaultRecapturePrimaryPaymentStrategy(final TbsPaymentService paymentService, final ModelService modelService)
	{
		this.paymentService = paymentService;
		this.modelService = modelService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the paymentService
	 */
	protected TbsPaymentService getPaymentService()
	{
		return paymentService;
	}
}
