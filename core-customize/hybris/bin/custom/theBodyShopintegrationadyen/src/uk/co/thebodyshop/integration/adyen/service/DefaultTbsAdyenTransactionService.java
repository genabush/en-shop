/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_PROVIDER;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adyen.model.checkout.PaymentsResponse;
import com.adyen.v6.model.NotificationItemModel;
import com.adyen.v6.service.DefaultAdyenTransactionService;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import uk.co.thebodyshop.core.calculation.strategies.OutstandingAmountCalculationStrategy;

public class DefaultTbsAdyenTransactionService extends DefaultAdyenTransactionService implements TbsAdyenTransactionService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTbsAdyenTransactionService.class);

	private static final String FORMATTED_TIME_FOR_TRANS_CODE = "yyyyMMddHHmmss";

	private OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy;

	@Override
	public PaymentTransactionModel authorizeOrderModel(final AbstractOrderModel abstractOrderModel,
			final String merchantTransactionCode, final String pspReference)
	{
		//First save the transactions to the CartModel < AbstractOrderModel
		final PaymentTransactionModel paymentTransactionModel = createPaymentTransaction(merchantTransactionCode, pspReference,
				abstractOrderModel);

		getModelService().save(paymentTransactionModel);

		final PaymentTransactionEntryModel authorisedTransaction = createAuthorizationPaymentTransactionEntryModel(
				paymentTransactionModel, merchantTransactionCode, abstractOrderModel, TransactionStatus.ACCEPTED);

		LOG.info("Saving AUTH transaction entry with psp reference: " + pspReference);
		getModelService().save(authorisedTransaction);
		getModelService().refresh(paymentTransactionModel); //refresh is needed by order-process
		return paymentTransactionModel;
	}

	@Override
	public PaymentTransactionModel markOrderAsAuthorizationPending(final AbstractOrderModel abstractOrderModel,
			final String merchantTransactionCode, final String pspReference)
	{
		//First save the transactions to the CartModel < AbstractOrderModel
		final PaymentTransactionModel paymentTransactionModel = createPaymentTransaction(merchantTransactionCode, pspReference,
				abstractOrderModel);

		getModelService().save(paymentTransactionModel);

		final PaymentTransactionEntryModel authorisedTransaction = createAuthorizationPaymentTransactionEntryModel(
				paymentTransactionModel, merchantTransactionCode, abstractOrderModel, TransactionStatus.AUTHORIZATION_PENDING);

		LOG.info("Saving AUTH transaction entry with psp reference: " + pspReference);
		getModelService().save(authorisedTransaction);
		getModelService().refresh(paymentTransactionModel); //refresh is needed by order-process
		return paymentTransactionModel;
	}

	@Override
	public void createPaymentRedirectTransaction(final AbstractOrderModel abstractOrderModel,
			final PaymentsResponse paymentsResponse)
	{
		final int paymentTransactionsNumber = (CollectionUtils.isEmpty(abstractOrderModel.getPaymentTransactions()))
				? abstractOrderModel.getPaymentTransactions().size()
				: 0;
		final String pspReference = abstractOrderModel.getCode() + getFormattedTimeForTransactionCode() + paymentTransactionsNumber;
		final PaymentTransactionModel paymentTransactionModel = createPaymentTransaction(abstractOrderModel.getCode(), pspReference,
				abstractOrderModel);
		paymentTransactionModel.setAdyenPaymentData(paymentsResponse.getPaymentData());
		if (null != paymentsResponse.getRedirect() && null != paymentsResponse.getRedirect().getData())
		{
			paymentTransactionModel.setAdyenPaymentMD(paymentsResponse.getRedirect().getData().get("MD"));
		}
		paymentTransactionModel.setOrder(null);
		paymentTransactionModel.setPaymentRedirectOrder(abstractOrderModel);
		getModelService().save(paymentTransactionModel);
		final PaymentTransactionEntryModel paymentRedirectTransaction = createPaymentRedirectTransactionEntryModel(
				paymentTransactionModel, abstractOrderModel);
		getModelService().save(paymentRedirectTransaction);
		getModelService().refresh(paymentTransactionModel);
	}

	private PaymentTransactionEntryModel createPaymentRedirectTransactionEntryModel(
			final PaymentTransactionModel paymentTransaction, final AbstractOrderModel abstractOrderModel)
	{
		final PaymentTransactionEntryModel transactionEntryModel = getModelService().create(PaymentTransactionEntryModel.class);
		final String code = paymentTransaction.getRequestId() + "_" + paymentTransaction.getEntries().size();
		transactionEntryModel.setType(PaymentTransactionType.PAYMENT_REDIRECT);
		transactionEntryModel.setPaymentTransaction(paymentTransaction);
		transactionEntryModel.setRequestId(paymentTransaction.getRequestId());
		transactionEntryModel.setRequestToken(abstractOrderModel.getCode());
		transactionEntryModel.setCode(code);
		transactionEntryModel.setTime(DateTime.now().toDate());
		transactionEntryModel.setTransactionStatus(TransactionStatus.PAYMENT_REDIRECT.name());
		transactionEntryModel.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		transactionEntryModel.setAmount(getOutstandingAmountCalculationStrategy().getOutstandingAmount(abstractOrderModel));
		transactionEntryModel.setCurrency(abstractOrderModel.getCurrency());
		return transactionEntryModel;
	}

	private String getFormattedTimeForTransactionCode()
	{
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(FORMATTED_TIME_FOR_TRANS_CODE);
		final LocalDateTime localDateTime = LocalDateTime.now();
		return dtf.format(localDateTime);
	}

	@Override
	public PaymentTransactionModel getLatestRedirectPaymentTransaction(final AbstractOrderModel abstractOrderModel)
	{
		if (CollectionUtils.isEmpty(abstractOrderModel.getPaymentRedirectTransactions()))
		{
			return null;
		}
		final List<PaymentTransactionModel> paymentRedirectTransactions = abstractOrderModel.getPaymentRedirectTransactions()
				.stream().sorted(Comparator.comparing(PaymentTransactionModel::getCreationtime).reversed())
				.collect(Collectors.toList());
		final Optional<PaymentTransactionModel> transaction = paymentRedirectTransactions.stream()
				.filter(paymentTransaction -> isValidPaymentRedirectTransaction(paymentTransaction)).findFirst();
		return (transaction.isPresent()) ? transaction.get() : null;
	}

	private boolean isValidPaymentRedirectTransaction(final PaymentTransactionModel paymentTransaction)
	{
		final Optional<PaymentTransactionEntryModel> transactionEntry = paymentTransaction.getEntries().stream()
				.filter(paymentTransactionEntry -> paymentTransactionEntry.getType().equals(PaymentTransactionType.PAYMENT_REDIRECT))
				.findFirst();
		return (transactionEntry.isPresent()) ? true : false;
	}

	@Override
	public PaymentTransactionModel getPaymentTransactionForMd(final AbstractOrderModel abstractOrderModel, final String md)
	{
		final Optional<PaymentTransactionModel> transaction = abstractOrderModel.getPaymentRedirectTransactions().stream()
				.filter(paymentTransaction -> isValidPaymentRedirectTransaction(paymentTransaction, md)).findFirst();
		return (transaction.isPresent()) ? transaction.get() : null;
	}

	private boolean isValidPaymentRedirectTransaction(final PaymentTransactionModel paymentTransaction, final String md)
	{
		if (paymentTransaction.getAdyenPaymentMD().equals(md))
		{
			final Optional<PaymentTransactionEntryModel> transactionEntry = paymentTransaction.getEntries().stream()
					.filter(
							paymentTransactionEntry -> paymentTransactionEntry.getType().equals(PaymentTransactionType.PAYMENT_REDIRECT))
					.findFirst();
			return (transactionEntry.isPresent()) ? true : false;
		}
		return false;
	}

	@Override
	public void handlePaymentRedirectFailure(final PaymentTransactionModel paymentRedirectTransaction)
	{
		if (Objects.nonNull(paymentRedirectTransaction) && CollectionUtils.isNotEmpty(paymentRedirectTransaction.getEntries()))
		{
			for (final PaymentTransactionEntryModel paymentEntry : paymentRedirectTransaction.getEntries())
			{
				paymentEntry.setTransactionStatus(TransactionStatus.PAYMENT_REDIRECT_FAILURE.name());
				paymentEntry.setTransactionStatusDetails(TransactionStatusDetails.PAYMENT_REDIRECT_FAILURE.name());
				getModelService().save(paymentEntry);
			}
		}
		getModelService().save(paymentRedirectTransaction);
	}

	private PaymentTransactionModel createPaymentTransaction(final String merchantCode, final String pspReference,
			final AbstractOrderModel abstractOrderModel)
	{
		final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
		paymentTransactionModel.setCode(pspReference);
		paymentTransactionModel.setRequestId(pspReference);
		paymentTransactionModel.setRequestToken(merchantCode);
		paymentTransactionModel.setPaymentProvider(PAYMENT_PROVIDER);
		paymentTransactionModel.setOrder(abstractOrderModel);
		paymentTransactionModel.setCurrency(abstractOrderModel.getCurrency());
		paymentTransactionModel.setInfo(abstractOrderModel.getPaymentInfo());
		paymentTransactionModel
				.setPlannedAmount(getOutstandingAmountCalculationStrategy().getOutstandingAmount(abstractOrderModel));

		return paymentTransactionModel;
	}

	private PaymentTransactionEntryModel createAuthorizationPaymentTransactionEntryModel(
			final PaymentTransactionModel paymentTransaction, final String merchantCode, final AbstractOrderModel abstractOrderModel,
			final TransactionStatus transactionStatus)
	{
		final PaymentTransactionEntryModel transactionEntryModel = getModelService().create(PaymentTransactionEntryModel.class);

		final String code = paymentTransaction.getRequestId() + "_" + paymentTransaction.getEntries().size();

		transactionEntryModel.setType(PaymentTransactionType.AUTHORIZATION);
		transactionEntryModel.setPaymentTransaction(paymentTransaction);
		transactionEntryModel.setRequestId(paymentTransaction.getRequestId());
		transactionEntryModel.setRequestToken(merchantCode);
		transactionEntryModel.setCode(code);
		transactionEntryModel.setTime(DateTime.now().toDate());
		transactionEntryModel.setTransactionStatus(transactionStatus.name());
		transactionEntryModel.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		transactionEntryModel.setAmount(getOutstandingAmountCalculationStrategy().getOutstandingAmount(abstractOrderModel));
		transactionEntryModel.setCurrency(abstractOrderModel.getCurrency());

		return transactionEntryModel;
	}

	public boolean isNotAuthorizedTransaction(final PaymentTransactionModel paymentTransaction)
	{
		for (final PaymentTransactionEntryModel entry : paymentTransaction.getEntries())
		{
			if (entry.getType().equals(PaymentTransactionType.AUTHORIZATION))
			{
				if (TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
				{
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public PaymentTransactionEntryModel createAuthorizedTransactionFromNotification(final PaymentTransactionModel paymentTransaction, final NotificationItemModel notificationItemModel)
	{
		final PaymentTransactionEntryModel transactionEntryModel = createFromModificationNotification(paymentTransaction, notificationItemModel);
		transactionEntryModel.setType(PaymentTransactionType.AUTHORIZATION);
		return transactionEntryModel;
	}

	/**
	 * @return the outstandingAmountCalculationStrategy
	 */
	public OutstandingAmountCalculationStrategy getOutstandingAmountCalculationStrategy()
	{
		return outstandingAmountCalculationStrategy;
	}

	/**
	 * @param outstandingAmountCalculationStrategy
	 *           the outstandingAmountCalculationStrategy to set
	 */
	public void setOutstandingAmountCalculationStrategy(
			final OutstandingAmountCalculationStrategy outstandingAmountCalculationStrategy)
	{
		this.outstandingAmountCalculationStrategy = outstandingAmountCalculationStrategy;
	}
}
