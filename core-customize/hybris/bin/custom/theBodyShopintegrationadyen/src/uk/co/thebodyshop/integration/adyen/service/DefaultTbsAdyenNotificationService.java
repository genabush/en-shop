/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import org.apache.log4j.Logger;

import com.adyen.model.notification.NotificationRequestItem;
import com.adyen.v6.constants.Adyenv6coreConstants;
import com.adyen.v6.model.NotificationItemModel;
import com.adyen.v6.service.DefaultAdyenNotificationService;
import com.google.common.collect.Iterables;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

/**
 * @author Marcin
 */
public class DefaultTbsAdyenNotificationService extends DefaultAdyenNotificationService implements TbsAdyenNotificationService
{
	/**
	 *
	 */
	private static final String PROCESS_EVENT_ADYEN_CANCELLED = "AdyenCancelled";

	private static final Logger LOG = Logger.getLogger(DefaultTbsAdyenNotificationService.class);

	private TbsAdyenTransactionService tbsAdyenTransactionService;

	private TbsAdyenBusinessProcessService tbsAdyenBusinessProcessService;

	@Override
	public void processNotification(final NotificationItemModel notificationItemModel)
	{
		PaymentTransactionModel paymentTransaction;
		switch (notificationItemModel.getEventCode())
		{
			case NotificationRequestItem.EVENT_CODE_CAPTURE:
				paymentTransaction = getPaymentTransactionRepository().getTransactionModel(notificationItemModel.getOriginalReference());
				processCapturedEvent(notificationItemModel, paymentTransaction);
				break;
			case NotificationRequestItem.EVENT_CODE_AUTHORISATION:
				paymentTransaction = getPaymentTransactionRepository().getTransactionModel(notificationItemModel.getPspReference());
				if (paymentTransaction == null)
				{
					LOG.error("Unable to find payment transaction with request id :: " + notificationItemModel.getPspReference());
					break;
				}
				if (getTbsAdyenTransactionService().isNotAuthorizedTransaction(paymentTransaction))
				{
					processAuthorisationEvent(notificationItemModel, paymentTransaction);
				}
				else
				{
					LOG.warn("Authorisation already processed " + paymentTransaction.getRequestId());
				}
				break;
			case NotificationRequestItem.EVENT_CODE_CANCEL_OR_REFUND:
				paymentTransaction = getPaymentTransactionRepository().getTransactionModel(notificationItemModel.getOriginalReference());
				processCancelEvent(notificationItemModel, paymentTransaction);
				break;
			case NotificationRequestItem.EVENT_CODE_REFUND:
				processRefundEvent(notificationItemModel);
				break;
		}
	}

	@Override
	public PaymentTransactionEntryModel processAuthorisationEvent(final NotificationItemModel notificationItemModel, final PaymentTransactionModel paymentTransactionModel)
	{
		final PaymentTransactionEntryModel paymentTransactionEntryModel = getTbsAdyenTransactionService().createAuthorizedTransactionFromNotification(paymentTransactionModel, notificationItemModel);
		LOG.debug("Saving Authorization transaction entry for pspreference ::  " + notificationItemModel.getPspReference());
		getModelService().save(paymentTransactionEntryModel);
		final OrderModel orderModel = (OrderModel) paymentTransactionModel.getOrder();
		getTbsAdyenBusinessProcessService().triggerOrderProcessEvent(orderModel, Adyenv6coreConstants.PROCESS_EVENT_ADYEN_AUTHORIZED);

		return paymentTransactionEntryModel;
	}

	@Override
	public PaymentTransactionEntryModel processCapturedEvent(final NotificationItemModel notificationItemModel, final PaymentTransactionModel paymentTransactionModel)
	{
		if (paymentTransactionModel == null)
		{
			LOG.debug("Parent transaction is null");
			return null;
		}
		final PaymentTransactionEntryModel paymentTransactionEntryModel = getAdyenTransactionService().createCapturedTransactionFromNotification(paymentTransactionModel, notificationItemModel);
		LOG.debug("Saving Captured transaction entry");
		getModelService().save(paymentTransactionEntryModel);
		final OrderModel orderModel = (OrderModel) paymentTransactionModel.getOrder();
		// TODO set first consignment status for now,review later when using multiple consignments
		final ConsignmentModel consignment = Iterables.getFirst(orderModel.getConsignments(), null);
		getTbsAdyenBusinessProcessService().triggerConsignmentProcessEvent(consignment, Adyenv6coreConstants.PROCESS_EVENT_ADYEN_CAPTURED);
		return paymentTransactionEntryModel;
	}

	@Override
	public PaymentTransactionEntryModel processCancelEvent(final NotificationItemModel notificationItemModel, final PaymentTransactionModel paymentTransactionModel)
	{
		final PaymentTransactionEntryModel paymentTransactionEntryModel = super.processCancelEvent(notificationItemModel, paymentTransactionModel);
		final OrderModel orderModel = (OrderModel) paymentTransactionModel.getOrder();
		// TODO set first consignment status for now,review later when using multiple consignments
		final ConsignmentModel consignment = Iterables.getFirst(orderModel.getConsignments(), null);
		getTbsAdyenBusinessProcessService().triggerConsignmentProcessEvent(consignment, PROCESS_EVENT_ADYEN_CANCELLED);
		getTbsAdyenBusinessProcessService().triggerOrderProcessEvent(orderModel, PROCESS_EVENT_ADYEN_CANCELLED);
		return paymentTransactionEntryModel;
	}

	/**
	 * @return the tbsAdyenTransactionService
	 */
	public TbsAdyenTransactionService getTbsAdyenTransactionService()
	{
		return tbsAdyenTransactionService;
	}

	/**
	 * @param tbsAdyenTransactionService the tbsAdyenTransactionService to set
	 */
	public void setTbsAdyenTransactionService(final TbsAdyenTransactionService tbsAdyenTransactionService)
	{
		this.tbsAdyenTransactionService = tbsAdyenTransactionService;
	}

	/**
	 * @return the tbsAdyenBusinessProcessService
	 */
	public TbsAdyenBusinessProcessService getTbsAdyenBusinessProcessService()
	{
		return tbsAdyenBusinessProcessService;
	}

	/**
	 * @param tbsAdyenBusinessProcessService
	 *          the tbsAdyenBusinessProcessService to set
	 */
	public void setTbsAdyenBusinessProcessService(final TbsAdyenBusinessProcessService tbsAdyenBusinessProcessService)
	{
		this.tbsAdyenBusinessProcessService = tbsAdyenBusinessProcessService;
	}

}