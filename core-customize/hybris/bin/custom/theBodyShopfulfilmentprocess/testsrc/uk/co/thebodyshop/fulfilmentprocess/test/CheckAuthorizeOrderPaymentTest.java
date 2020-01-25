/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.test;

import java.util.Arrays;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.fulfilmentprocess.actions.order.CheckAuthorizeOrderPaymentAction;


@UnitTest
public class CheckAuthorizeOrderPaymentTest
{
	@Mock
	private ModelService modelService;

	@InjectMocks
	private final CheckAuthorizeOrderPaymentAction checkAuthorizeOrderPayment = new CheckAuthorizeOrderPaymentAction();

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExecuteActionOK() throws RetryLaterException, Exception
	{
		final OrderProcessModel businessProcessModel = new OrderProcessModel();

		final OrderModel order = new OrderModel();
		final PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
		final PaymentTransactionEntryModel entry = new PaymentTransactionEntryModel();
		entry.setType(PaymentTransactionType.AUTHORIZATION);
		entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		paymentTransaction.setEntries(Arrays.asList(entry));
		businessProcessModel.setOrder(order);
		order.setPaymentTransactions(Arrays.asList(paymentTransaction));

		Assertions.assertThat(checkAuthorizeOrderPayment.execute(businessProcessModel)).isEqualTo(Transition.OK.name());
	}

	@Test
	public void testExecuteActionNOK() throws RetryLaterException, Exception
	{
		final OrderProcessModel businessProcessModel = new OrderProcessModel();

		final OrderModel order = new OrderModel();
		final PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
		final PaymentTransactionEntryModel entry = new PaymentTransactionEntryModel();
		entry.setType(PaymentTransactionType.AUTHORIZATION);
		entry.setTransactionStatus(TransactionStatus.REJECTED.name());
		paymentTransaction.setEntries(Arrays.asList(entry));
		businessProcessModel.setOrder(order);
		order.setPaymentTransactions(Arrays.asList(paymentTransaction));
		Assertions.assertThat(checkAuthorizeOrderPayment.execute(businessProcessModel)).isEqualTo(Transition.NOK.name());
	}

	@Test
	public void testExecuteActionPending() throws RetryLaterException, Exception
	{
		final OrderProcessModel businessProcessModel = new OrderProcessModel();

		final OrderModel order = new OrderModel();
		final PaymentTransactionModel paymentTransaction = new PaymentTransactionModel();
		final PaymentTransactionEntryModel entry = new PaymentTransactionEntryModel();
		entry.setType(PaymentTransactionType.AUTHORIZATION);
		entry.setTransactionStatus(TransactionStatus.AUTHORIZATION_PENDING.name());
		paymentTransaction.setEntries(Arrays.asList(entry));
		businessProcessModel.setOrder(order);
		order.setPaymentTransactions(Arrays.asList(paymentTransaction));
		Assertions.assertThat(checkAuthorizeOrderPayment.execute(businessProcessModel)).isEqualTo(CheckAuthorizeOrderPaymentAction.Transition.PENDING.name());

	}
}
