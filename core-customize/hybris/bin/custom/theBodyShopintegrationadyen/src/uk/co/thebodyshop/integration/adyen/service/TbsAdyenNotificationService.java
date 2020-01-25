/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import com.adyen.v6.model.NotificationItemModel;
import com.adyen.v6.service.AdyenNotificationService;

import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

/**
 *
 */
public interface TbsAdyenNotificationService extends AdyenNotificationService
{
	public PaymentTransactionEntryModel processAuthorisationEvent(final NotificationItemModel notificationItemModel, final PaymentTransactionModel paymentTransactionModel);

}
