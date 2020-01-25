/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.request.CaptureRequest;
import de.hybris.platform.payment.commands.result.CaptureResult;
import de.hybris.platform.payment.commands.result.VoidResult;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.util.MathUtils;
import uk.co.thebodyshop.integration.svs.model.GiftCardModel;
import uk.co.thebodyshop.integration.svs.services.SvsService;
import uk.co.thebodyshop.integrations.svs.data.GiftCardCaptureData;
import uk.co.thebodyshop.payment.constants.TheBodyShoppaymentConstants;
import uk.co.thebodyshop.payment.exceptions.PaymentMethodException;
import uk.co.thebodyshop.payment.request.TbsVoidRequest;
import uk.co.thebodyshop.payment.services.TbsPaymentService;

/**
 * @author Marcin
 */
public class DefaultTbsPaymentService extends DefaultPaymentServiceImpl implements TbsPaymentService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTbsPaymentService.class);

	private static final String ACCEPTED = "ACCEPTED";

	private SvsService svsService;

	private ModelService modelService;

	@Override
	public boolean isFullOrderCapture(final OrderModel order)
	{
		return order.getConsignments().stream().allMatch(consignmentModel -> consignmentModel.getConsignmentEntries().stream().allMatch(entry -> Objects.nonNull(entry.getOrderEntry()) && entry.getShippedQuantity() == entry.getQuantity()));
	}

	@Override
	public GiftCardCaptureData captureSecondaryPayment(final OrderModel order, final double amountRemaining)
	{
		final GiftCardCaptureData giftCardCaptureData = new GiftCardCaptureData();
		giftCardCaptureData.setCaptureSuccesfull(true);
		try
		{
			// secondary payments
			giftCardCaptureData.setAmountRemaining(captureGiftCardAmount(order, amountRemaining));

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Amount remaining [{}] aftering capturing gift cards for order [{}]", amountRemaining, order.getCode());
			}
		}
		catch (final PaymentMethodException e)
		{
			LOG.error("An exception occured trying to capture the gift card amount", e);

			giftCardCaptureData.setCaptureSuccesfull(false);
			giftCardCaptureData.setAmountRemaining(getRemainingAmoutAfterSecondaryPayments(order, amountRemaining));
		}
		return giftCardCaptureData;
	}

	@Override
	public boolean capturePrimaryPayment(double amountRemaining, final OrderModel order)
	{
		if (amountRemaining > 0.0)
		{
			// primary payment of outstanding amount
			final List<PaymentTransactionModel> authPaymentTransactions = getPrimaryAuthPaymentTransactions(order.getPaymentTransactions());

			if (CollectionUtils.isNotEmpty(authPaymentTransactions))
			{
				for (final PaymentTransactionModel authPaymentTransaction : authPaymentTransactions)
				{
					final Double capturedAmount = capturePrimaryPaymentTransaction(order, amountRemaining, authPaymentTransaction);
					if (null == capturedAmount)
					{
						return false;
					}
					else
					{
						amountRemaining = amountRemaining - capturedAmount.doubleValue();
					}
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			// if order has primary payments than reverse auth
			if (CollectionUtils.isNotEmpty(getPrimaryAuthPaymentTransactions(order.getPaymentTransactions())))
			{
				reverseAuthUnusedPrimaryPayment(order);
			}
		}
		return true;
	}

	@Override
	public Double capturePrimaryPaymentTransaction(final OrderModel order, final double amountRemaining, final PaymentTransactionModel paymentTransaction)
	{
		if (paymentTransaction.getPaymentProvider().equalsIgnoreCase(TheBodyShoppaymentConstants.ADYEN_PAYMENT_PROVIDER))
		{
			final PaymentTransactionEntryModel txnEntry = capture(paymentTransaction, amountRemaining);

			if (TransactionStatus.ACCEPTED.name().equals(txnEntry.getTransactionStatus()))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("The payment transaction has been captured. Order: " + order.getCode() + ". Txn: " + paymentTransaction.getCode());
				}
				return txnEntry.getAmount().setScale(2).doubleValue();
			}
			else
			{
				LOG.error("The payment transaction capture has failed. Order: " + order.getCode() + ". Txn: " + paymentTransaction.getCode());
				return null;
			}
		}
		return Double.valueOf(0.0);
	}

	@Override
	public PaymentTransactionEntryModel capture(final PaymentTransactionModel transaction, final double amountRemaining)
	{
		PaymentTransactionEntryModel authTransactionEntry = null;
		final Iterator<PaymentTransactionEntryModel> transactionEntriesIterator = transaction.getEntries().iterator();
		while (transactionEntriesIterator.hasNext())
		{
			final PaymentTransactionEntryModel paymentTransactionEnytry = transactionEntriesIterator.next();
			if (paymentTransactionEnytry.getType().equals(PaymentTransactionType.AUTHORIZATION))
			{
				authTransactionEntry = paymentTransactionEnytry;
				break;
			}
		}
		if (authTransactionEntry == null)
		{
			throw new AdapterException("Could not capture without authorization");
		}
		else
		{
			final BigDecimal calculatedCaptureAmount = getPaymentEntryCalculatedCaptureAmount(amountRemaining, authTransactionEntry);
			final PaymentTransactionType transactionType = PaymentTransactionType.CAPTURE;
			final String newEntryCode = super.getNewPaymentTransactionEntryCode(transaction, transactionType);
			final CaptureResult result = super.getCardPaymentService().capture(new CaptureRequest(newEntryCode, transaction.getRequestId(), transaction.getRequestToken(), Currency.getInstance(authTransactionEntry.getCurrency().getIsocode()),
					calculatedCaptureAmount, transaction.getPaymentProvider(), authTransactionEntry.getSubscriptionID()));
			return createCapturePaymentEntry(transaction, transactionType, newEntryCode, result);
		}
	}

	private BigDecimal getPaymentEntryCalculatedCaptureAmount(final double amountRemaining, final PaymentTransactionEntryModel authTransactionEntry)
	{
		BigDecimal calculatedCaptureAmount = BigDecimal.ZERO;
		if (amountRemaining < authTransactionEntry.getAmount().doubleValue())
		{
			calculatedCaptureAmount = BigDecimal.valueOf(amountRemaining).setScale(2);
		}
		else
		{
			calculatedCaptureAmount = authTransactionEntry.getAmount().setScale(2);
		}
		return calculatedCaptureAmount;
	}

	private PaymentTransactionEntryModel createCapturePaymentEntry(final PaymentTransactionModel transaction, final PaymentTransactionType transactionType, final String newEntryCode, final CaptureResult result)
	{
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel) this.getModelService().create(PaymentTransactionEntryModel.class);
		entry.setAmount(result.getTotalAmount());
		if (result.getCurrency() != null)
		{
			entry.setCurrency(this.getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
		}
		entry.setType(transactionType);
		entry.setRequestId(result.getRequestId());
		entry.setRequestToken(result.getRequestToken());
		entry.setTime(result.getRequestTime() == null ? new Date() : result.getRequestTime());
		entry.setPaymentTransaction(transaction);
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setCode(newEntryCode);
		this.getModelService().save(entry);
		return entry;
	}

	@Override
	public double captureGiftCardAmount(final OrderModel order, final double amount)
	{
		boolean successGiftCardCapture = true;
		double capturedAmount = 0d;
		if (amount > 0d)
		{
			final Double giftCardApprovedAmount = order.getAmountGiftCards();
			if (giftCardApprovedAmount != null && giftCardApprovedAmount.doubleValue() > 0d)
			{
				for (final GiftCardModel giftCard : order.getGiftCards())
				{
					final double remainingAmount = MathUtils.round(amount - capturedAmount);
					if (remainingAmount > 0)
					{
						final double giftCardCaptureAmount = remainingAmount >= giftCard.getAmountAppliedForOrder().doubleValue() ? giftCard.getAmountAppliedForOrder().doubleValue() : remainingAmount;
						LOG.info("Capture request received for giftcard :: " + giftCard.getGiftCardNumber() + " for order " + order.getCode());
						final boolean success = getSvsService().capture(giftCard, order, giftCardCaptureAmount);
						if (success)
						{
							LOG.info("Capture status: ACCEPTED for giftcard :: " + giftCard.getGiftCardNumber() + " for order " + order.getCode());
							capturedAmount += giftCardCaptureAmount;
						}
						else
						{
							LOG.info("Capture status: REJECTED for giftcard :: " + giftCard.getGiftCardNumber() + " for order " + order.getCode());
							successGiftCardCapture = false;
						}
					}
					else
					{
						LOG.info("Reverse Auth for giftcard :: " + giftCard.getGiftCardNumber() + " for order " + order.getCode());
						getSvsService().reverseAuthorise(giftCard, order);
					}
				}
			}
		}
		else
		{
			reverseGftCardPayment(order);
		}
		if (successGiftCardCapture == false)
		{
			throw new PaymentMethodException("UnableToCaptureGiftCardPayment");
		}
		return MathUtils.round(amount - capturedAmount);
	}

	@Override
	public double getRemainingAmoutAfterSecondaryPayments(final OrderModel order, double amountRemaining)
	{
		final Double giftCardApprovedAmount = order.getAmountGiftCards();
		if (giftCardApprovedAmount != null)
		{
			if (amountRemaining <= giftCardApprovedAmount.doubleValue())
			{
				amountRemaining = 0.0;
			}
			else
			{
				amountRemaining = amountRemaining - giftCardApprovedAmount.doubleValue();
			}
		}
		return amountRemaining;
	}

	@Override
	public List<PaymentTransactionModel> getPrimaryAuthPaymentTransactions(final List<PaymentTransactionModel> paymentTransactions)
	{
		final List<PaymentTransactionModel> primaryPaymentTransactions = new ArrayList<>();
		for (final PaymentTransactionModel paymentTransaction : paymentTransactions)
		{
			if (paymentTransaction.getPaymentProvider().equalsIgnoreCase(TheBodyShoppaymentConstants.ADYEN_PAYMENT_PROVIDER) || paymentTransaction.getPaymentProvider().equalsIgnoreCase(TheBodyShoppaymentConstants.PAYPAL_PAYMENT_PROVIDER))
			{
				for (final PaymentTransactionEntryModel paymentEntryModel : paymentTransaction.getEntries())
				{
					if (PaymentTransactionType.AUTHORIZATION.equals(paymentEntryModel.getType()))
					{
						if (TransactionStatus.ACCEPTED.toString().equals(paymentEntryModel.getTransactionStatus()))
						{
							primaryPaymentTransactions.add(paymentTransaction);
						}
					}
				}
			}
		}
		return primaryPaymentTransactions;
	}

	@Override
	public List<PaymentTransactionEntryModel> getPrimaryFailedCapturePaymentTransactionEntries(final List<PaymentTransactionModel> paymentTransactions)
	{
		final List<PaymentTransactionEntryModel> failedCaptureEntries = new ArrayList<>();
		for (final PaymentTransactionModel paymentTransaction : paymentTransactions)
		{
			if (paymentTransaction.getPaymentProvider().equalsIgnoreCase(TheBodyShoppaymentConstants.ADYEN_PAYMENT_PROVIDER) || paymentTransaction.getPaymentProvider().equalsIgnoreCase(TheBodyShoppaymentConstants.PAYPAL_PAYMENT_PROVIDER))
			{
				for (final PaymentTransactionEntryModel paymentEntryModel : paymentTransaction.getEntries())
				{
					if (PaymentTransactionType.CAPTURE.equals(paymentEntryModel.getType()))
					{
						if (TransactionStatus.ERROR.toString().equals(paymentEntryModel.getTransactionStatus()) || TransactionStatus.REJECTED.toString().equals(paymentEntryModel.getTransactionStatus()))
						{
							failedCaptureEntries.add(paymentEntryModel);
						}
					}
				}
			}
		}
		return failedCaptureEntries;
	}

	@Override
	public void reverseGftCardPayment(final OrderModel order)
	{
		final Double giftCardApprovedAmount = order.getAmountGiftCards();
		if (giftCardApprovedAmount != null && giftCardApprovedAmount.doubleValue() > 0d)
		{
			for (final GiftCardModel giftCard : order.getGiftCards())
			{
				getSvsService().reverseAuthorise(giftCard, order);
			}
		}
	}

	@Override
	public void reverseAuthUnusedPrimaryPayment(final OrderModel order)
	{
		for (final PaymentTransactionModel paymentTransactionModel : getPrimaryAuthPaymentTransactions(order.getPaymentTransactions()))
		{
			if (paymentTransactionModel.getPaymentProvider().equalsIgnoreCase(TheBodyShoppaymentConstants.ADYEN_PAYMENT_PROVIDER) || paymentTransactionModel.getPaymentProvider().equalsIgnoreCase(TheBodyShoppaymentConstants.PAYPAL_PAYMENT_PROVIDER))
			{
				reversePrimaryPaymentAuth(order, paymentTransactionModel);
			}
		}
	}

	@Override
	public PaymentTransactionEntryModel cancel(final PaymentTransactionEntryModel transaction)
	{
		final BaseStoreModel store = transaction.getPaymentTransaction().getOrder().getStore();
		final PaymentTransactionType transactionType = PaymentTransactionType.CANCEL;
		final String newEntryCode = this.getNewPaymentTransactionEntryCode(transaction.getPaymentTransaction(), transactionType);
		final TbsVoidRequest voidRequest = new TbsVoidRequest(newEntryCode, transaction.getRequestId(), transaction.getRequestToken(), transaction.getPaymentTransaction().getPaymentProvider(), transaction.getCurrency() == null ? null : Currency.getInstance(transaction.getCurrency().getIsocode()), transaction.getAmount(), store);
		final VoidResult result = this.getCardPaymentService().voidCreditOrCapture(voidRequest);
		final PaymentTransactionEntryModel entry = (PaymentTransactionEntryModel)this.getModelService().create(PaymentTransactionEntryModel.class);
		if (result.getCurrency() != null)
		{
			entry.setCurrency(this.getCommonI18NService().getCurrency(result.getCurrency().getCurrencyCode()));
		}

		entry.setType(transactionType);
		entry.setTime(result.getRequestTime() == null ? new Date() : result.getRequestTime());
		entry.setPaymentTransaction(transaction.getPaymentTransaction());
		entry.setRequestId(result.getRequestId());
		entry.setAmount(result.getAmount());
		entry.setRequestToken(result.getRequestToken());
		entry.setTransactionStatus(result.getTransactionStatus().toString());
		entry.setTransactionStatusDetails(result.getTransactionStatusDetails().toString());
		entry.setCode(newEntryCode);
		this.getModelService().save(entry);
		return entry;
	}

	/**
	 * @param order
	 * @param paymentTransactionModel
	 */
	private void reversePrimaryPaymentAuth(final OrderModel order, final PaymentTransactionModel paymentTransactionModel)
	{
		PaymentTransactionEntryModel authTransactionEntry = null;
		if (paymentTransactionModel != null)
		{
			authTransactionEntry = getSuccesfullAuthorizationEntry(paymentTransactionModel.getEntries());
		}
		if (Objects.isNull(authTransactionEntry))
		{
			LOG.error("Processing error - missing or ambiguous transaction entries.");
			order.setStatus(OrderStatus.PROCESSING_ERROR);
			getModelService().save(order);
			return;
		}
		final List<PaymentTransactionEntryModel> transactionEntries = new ArrayList<>();
		transactionEntries.addAll(paymentTransactionModel.getEntries());
		final PaymentTransactionEntryModel txnResultEntry = cancel(authTransactionEntry);

		if (TransactionStatus.ACCEPTED.name().equals(txnResultEntry.getTransactionStatus()))
		{
			LOG.debug("Reverse Authorise successful.");
		}
		else
		{
			LOG.error("Processing error - Reverse command failed.");
		}
		transactionEntries.add(txnResultEntry);
		paymentTransactionModel.setEntries(transactionEntries);
		getModelService().save(order);
		getModelService().save(paymentTransactionModel);
	}

	public PaymentTransactionEntryModel getSuccesfullAuthorizationEntry(final List<PaymentTransactionEntryModel> paymentTransactions)
	{
		if (paymentTransactions != null)
		{
			for (final PaymentTransactionEntryModel entry : paymentTransactions)
			{
				if (PaymentTransactionType.AUTHORIZATION.equals(entry.getType()) && ACCEPTED.equals(entry.getTransactionStatus()))
				{
					return entry;
				}
			}
		}
		return null;
	}

	/**
	 * @return the svsService
	 */
	public SvsService getSvsService()
	{
		return svsService;
	}

	/**
	 * @param svsService
	 *          the svsService to set
	 */
	public void setSvsService(final SvsService svsService)
	{
		this.svsService = svsService;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *          the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
