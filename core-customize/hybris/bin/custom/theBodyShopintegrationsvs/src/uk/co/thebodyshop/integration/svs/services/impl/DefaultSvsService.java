/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.svs.svsxml.beans.Amount;
import com.svs.svsxml.beans.BalanceInquiryRequest;
import com.svs.svsxml.beans.BalanceInquiryResponse;
import com.svs.svsxml.beans.Card;
import com.svs.svsxml.beans.Merchant;
import com.svs.svsxml.beans.PreAuthCompleteRequest;
import com.svs.svsxml.beans.PreAuthCompleteResponse;
import com.svs.svsxml.beans.PreAuthRequest;
import com.svs.svsxml.beans.PreAuthResponse;
import com.svs.svsxml.beans.ReturnCode;
import com.svs.svsxml.service.SVSXMLWay;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.integration.svs.constants.TheBodyShopintegrationsvsConstants;
import uk.co.thebodyshop.integration.svs.model.GiftCardModel;
import uk.co.thebodyshop.integration.svs.services.SvsService;

public class DefaultSvsService implements SvsService
{
	private static final Logger LOG = Logger.getLogger(DefaultSvsService.class);

	private static final int RETRY_COUNT = 3;

	private static final String GIFT_CARD = "Gift Card";

	@Resource(name = "svsGiftCardWS")
	private SVSXMLWay svsGiftCardWS;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "svsGiftCardStanGenerator")
	private KeyGenerator svsGiftCardStanGenerator;

	@Resource(name = "svsGiftCardTransactionIDGenerator")
	private KeyGenerator svsGiftCardTransactionIDGenerator;

	@Resource(name = "paymentService")
	private PaymentService paymentService;

	private String getMerchantNumber(final String siteUid)
	{
		return configurationService.getConfiguration().getString(TheBodyShopintegrationsvsConstants.SVS_MERCHANT_NUMBER_PREFIX + siteUid);
	}

	private boolean getCheckForDuplicate()
	{
		return Boolean.valueOf(configurationService.getConfiguration().getString(TheBodyShopintegrationsvsConstants.SVS_CHECK_FOR_DUPLICATE));
	}

	private String getStore(final String siteUid)
	{
		return configurationService.getConfiguration().getString(TheBodyShopintegrationsvsConstants.SVS_STORE_PREFIX + siteUid);
	}

	private String getDivision()
	{
		return configurationService.getConfiguration().getString(TheBodyShopintegrationsvsConstants.SVS_DIVISION);
	}

	private String getRoutingID()
	{
		return configurationService.getConfiguration().getString(TheBodyShopintegrationsvsConstants.SVS_ROUTING_ID);
	}

	private Card getCard(final String giftCardNumber, final String pinNumber)
	{
		final Card card = new Card();
		card.setCardNumber(giftCardNumber);
		card.setPinNumber(pinNumber);
		return card;
	}

	private Merchant getMerchant()
	{
		return getMerchant(cmsSiteService.getCurrentSite());
	}

	private Merchant getMerchant(final CMSSiteModel site)
	{
		if (site != null)
		{
			final Merchant merchant = new Merchant();
			merchant.setMerchantNumber(getMerchantNumber(site.getUid()));
			merchant.setStoreNumber(getStore(site.getUid()));
			merchant.setDivision(getDivision());
			merchant.setMerchantName(site.getName());
			return merchant;
		}
		return null;
	}

	private Amount getAmount(final Double amountTransaction, final String currencyIsocode)
	{
		final Amount amount = new Amount();
		amount.setAmount(new BigDecimal(amountTransaction).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
		amount.setCurrency(currencyIsocode);
		return amount;
	}

	// ********** Check Balance
	@Override
	public BalanceInquiryResponse checkBalance(final String giftCardNumber, final String pinNumber, final String cartCode, final String currencyIsocode)
	{
		return balanceEnquiry(giftCardNumber, pinNumber, cartCode, currencyIsocode, 1);
	}

	// ********** Reverse Authorisation = Capture with amount 0.0
	@Override
	public boolean reverseAuthorise(final GiftCardModel giftCard, final AbstractOrderModel order)
	{
		return preAuthComplete(giftCard, order, getAmount(Double.valueOf(0.0), order.getCurrency().getIsocode()), PaymentTransactionType.CANCEL, 1);
	}

	// ********** Authorisation
	@Override
	public String authorise(final String giftCardNumber, final String pinNumber, final CartModel cartModel, final double requestedAmount, final String currencyIsocode)
	{
		return preAuth(giftCardNumber, pinNumber, cartModel, requestedAmount, currencyIsocode, 1, null);
	}

	// ********** Capture
	@Override
	public boolean capture(final GiftCardModel giftCard, final AbstractOrderModel order)
	{
		return preAuthComplete(giftCard, order, getAmount(giftCard.getAmountAppliedForOrder(), order.getCurrency().getIsocode()), PaymentTransactionType.CAPTURE, 1);
	}

	// ********** Capture
	@Override
	public boolean capture(final GiftCardModel giftCard, final AbstractOrderModel order, final double amount)
	{
		final Amount amountForCapture = new Amount();
		amountForCapture.setAmount(amount);
		amountForCapture.setCurrency(order.getCurrency().getIsocode());
		PaymentTransactionType transType;
		if (giftCard.getAmountAppliedForOrder().doubleValue() == amount)
		{
			transType = PaymentTransactionType.CAPTURE;
		}
		else
		{
			transType = PaymentTransactionType.PARTIAL_CAPTURE;
		}
		return preAuthComplete(giftCard, order, amountForCapture, transType, 1);
	}

	private BalanceInquiryResponse balanceEnquiry(final String giftCardNumber, final String pinNumber, final String cartCode, final String currencyIsocode, int retryCount)
	{
		final BalanceInquiryRequest request = createCheckBalanceRequest(giftCardNumber, pinNumber, cartCode, currencyIsocode);
		try
		{
			final BalanceInquiryResponse response = svsGiftCardWS.balanceInquiry(request);
			final ReturnCode returnCode = response.getReturnCode();

			if (returnCode != null)
			{
				if (returnCode.getReturnCode().equals(HOST_UNAVAILABLE) && retryCount < RETRY_COUNT)
				{
					return balanceEnquiry(giftCardNumber, pinNumber, cartCode, currencyIsocode, ++retryCount);
				}
				return response;
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	private String preAuth(final String giftCardNumber, final String pinNumber, final CartModel cartModel, final double requestedAmount, final String currencyIsocode, int retryCount, final String transactionId)
	{
		final PreAuthRequest request = createPreAuthRequest(giftCardNumber, pinNumber, cartModel, requestedAmount, currencyIsocode, transactionId);
		try
		{
			final PreAuthResponse response = svsGiftCardWS.preAuth(request);
			final ReturnCode returnCode = response.getReturnCode();

			if (returnCode != null)
			{
				if (returnCode.getReturnCode().equals(HOST_UNAVAILABLE) && retryCount < RETRY_COUNT)
				{
					return preAuth(giftCardNumber, pinNumber, cartModel, requestedAmount, currencyIsocode, ++retryCount, request.getTransactionID());
				}

				return addAuthPaymentTranactionRecord(response, cartModel, requestedAmount);
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return SvsService.ERROR;
	}

	private boolean preAuthComplete(final GiftCardModel giftCard, final AbstractOrderModel order, final Amount amount, final PaymentTransactionType transType, int retryCount)
	{
		final PaymentTransactionModel paymentTransaction = getGiftCardTransaction(order, giftCard.getGiftCardNumber());
		final PreAuthCompleteRequest request = createPreAuthCompleteRequest(giftCard, order, amount);
		try
		{
			final PreAuthCompleteResponse response = svsGiftCardWS.preAuthComplete(request);
			final ReturnCode returnCode = response.getReturnCode();

			if (returnCode != null)
			{
				if (returnCode.getReturnCode().equals(HOST_UNAVAILABLE) && retryCount < RETRY_COUNT)
				{
					return preAuthComplete(giftCard, order, amount, transType, ++retryCount);
				}
				else if (returnCode.getReturnCode().equals(HOST_UNAVAILABLE))
				{
					// set for retry
					if (isCaptureTransaction(transType))
					{
						paymentTransaction.setPaymentRetryRequired(Boolean.TRUE);
						paymentTransaction.setPaymentFailureReason(HOST_UNAVAILABLE);
						modelService.save(paymentTransaction);
					}
				}
				else
				{
					if (isCaptureTransaction(transType))
					{
						if (!returnCode.getReturnCode().equals(APPROVED))
						{
							paymentTransaction.setPaymentFailureReason(returnCode.getReturnCode() + returnCode.getReturnDescription());
						}
						paymentTransaction.setPaymentRetryRequired(Boolean.FALSE);
						modelService.save(paymentTransaction);
					}
				}

				addCaptureTransactionRecord(response, order, transType);
				return returnCode.getReturnCode().equals(APPROVED);
			}
		}
		catch (final Exception e)
		{
			final String failureReason = e.getClass().getName();
			createPaymentCaptureFailureEntry(order, amount, transType, paymentTransaction, failureReason);
			// capture failed - eligible for retry
			LOG.error(e.getMessage(), e);
		}
		return false;
	}

	private boolean isCaptureTransaction(final PaymentTransactionType transType)
	{
		return transType != null && (transType.equals(PaymentTransactionType.CAPTURE) || transType.equals(PaymentTransactionType.PARTIAL_CAPTURE));
	}

	private void createPaymentCaptureFailureEntry(final AbstractOrderModel order, final Amount amount, final PaymentTransactionType transType, final PaymentTransactionModel paymentTransaction, final String failureReason)
	{
		if (paymentTransaction != null)
		{
			paymentTransaction.setPaymentRetryRequired(Boolean.TRUE);
			paymentTransaction.setPaymentFailureReason(failureReason);
			modelService.save(paymentTransaction);

			final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);
			entry.setType(transType);
			entry.setTime(new Date());
			entry.setCode(paymentService.getNewPaymentTransactionEntryCode(paymentTransaction, transType));
			entry.setCurrency(order.getCurrency());
			entry.setAmount(new BigDecimal(amount.getAmount()).setScale(2, RoundingMode.HALF_EVEN));
			entry.setTransactionStatus(TransactionStatus.ERROR.name());
			entry.setTransactionStatusDetails(TransactionStatus.ERROR.name());

			final List<PaymentTransactionEntryModel> entries = new ArrayList<>(paymentTransaction.getEntries());
			entries.add(entry);
			paymentTransaction.setOrder(order);
			paymentTransaction.setEntries(entries);

			modelService.save(entry);
			modelService.save(paymentTransaction);
			modelService.save(order);
		}

	}

	private PreAuthCompleteRequest createPreAuthCompleteRequest(final GiftCardModel giftCard, final AbstractOrderModel orderModel, final Amount amount)
	{
		final PaymentTransactionModel paymentTransaction = getGiftCardTransaction(orderModel, giftCard.getGiftCardNumber());
		final PreAuthCompleteRequest preAuthCompleteRequest = new PreAuthCompleteRequest();
		preAuthCompleteRequest.setCard(getCard(giftCard.getGiftCardNumber(), giftCard.getPinNumber()));

		preAuthCompleteRequest.setTransactionAmount(amount);
		preAuthCompleteRequest.setMerchant(getMerchant((CMSSiteModel) orderModel.getSite()));

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		preAuthCompleteRequest.setDate(sdf.format(new Date()));
		if (orderModel instanceof OrderModel)
		{
			preAuthCompleteRequest.setInvoiceNumber(((OrderModel) orderModel).getCartCode());
		}
		else
		{
			preAuthCompleteRequest.setInvoiceNumber(orderModel.getCode());
		}
		preAuthCompleteRequest.setRoutingID(getRoutingID());
		if (!getCheckForDuplicate())
		{
			preAuthCompleteRequest.setStan(giftCard.getStan());
		}
		else
		{
			String transactionId = getAuthTransactionId(paymentTransaction);
			if (StringUtils.isEmpty(transactionId))
			{
				transactionId = giftCard.getTransactionID();
			}
			preAuthCompleteRequest.setTransactionID(transactionId);
		}
		preAuthCompleteRequest.setCheckForDuplicate(getCheckForDuplicate());
		return preAuthCompleteRequest;
	}

	private BalanceInquiryRequest createCheckBalanceRequest(final String giftCardNumber, final String pinNumber, final String cartCode, final String currencyIsocode)
	{
		final BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
		balanceInquiryRequest.setAmount(getAmount(0.0, currencyIsocode));
		balanceInquiryRequest.setCard(getCard(giftCardNumber, pinNumber));
		balanceInquiryRequest.setMerchant(getMerchant());

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		balanceInquiryRequest.setDate(sdf.format(new Date()));
		balanceInquiryRequest.setInvoiceNumber(cartCode);
		balanceInquiryRequest.setRoutingID(getRoutingID());
		if (!getCheckForDuplicate())
		{
			balanceInquiryRequest.setStan(svsGiftCardStanGenerator.generate().toString());
		}
		else
		{
			balanceInquiryRequest.setTransactionID(inverse(getMerchant().getMerchantNumber(), 2) + svsGiftCardTransactionIDGenerator.generate().toString());
		}
		balanceInquiryRequest.setCheckForDuplicate(getCheckForDuplicate());
		return balanceInquiryRequest;
	}

	private PreAuthRequest createPreAuthRequest(final String giftCardNumber, final String pinNumber, final CartModel cartModel, final double requestedAmount, final String currencyIsocode, final String transactionId)
	{
		final PreAuthRequest preAuthRequest = new PreAuthRequest();

		preAuthRequest.setCard(getCard(giftCardNumber, pinNumber));
		preAuthRequest.setRequestedAmount(getAmount(requestedAmount, currencyIsocode));
		preAuthRequest.setMerchant(getMerchant());

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		preAuthRequest.setDate(sdf.format(new Date()));
		preAuthRequest.setInvoiceNumber(cartModel.getCode());
		preAuthRequest.setRoutingID(getRoutingID());

		if (!getCheckForDuplicate())
		{
			preAuthRequest.setStan(svsGiftCardStanGenerator.generate().toString());
		}
		else if (StringUtils.isNotEmpty(transactionId)) // In case of retry we need to use the same transaction id
		{
			preAuthRequest.setTransactionID(transactionId);
		}
		else
		{
			preAuthRequest.setTransactionID(inverse(getMerchant().getMerchantNumber(), 2) + svsGiftCardTransactionIDGenerator.generate().toString());
		}

		preAuthRequest.setCheckForDuplicate(getCheckForDuplicate());
		return preAuthRequest;
	}

	private String getAuthTransactionId(final PaymentTransactionModel paymentTransaction)
	{
		final PaymentTransactionEntryModel trans = getAuthTransactionEntry(paymentTransaction);
		if (trans == null)
		{
			return null;
		}
		return trans.getRequestId();
	}

	@Override
	public PaymentTransactionEntryModel getAuthTransactionEntry(final PaymentTransactionModel paymentTransaction)
	{
		PaymentTransactionEntryModel transactionEntryReturn = null;
		Date latest = null;
		if (paymentTransaction != null)
		{
			for (final PaymentTransactionEntryModel transactionEntry : paymentTransaction.getEntries())
			{
				if (PaymentTransactionType.AUTHORIZATION.getCode().equals(transactionEntry.getType().getCode()) && TransactionStatus.ACCEPTED.name().equals(transactionEntry.getTransactionStatus()))
				{
					if (latest == null)
					{
						latest = transactionEntry.getCreationtime();
						transactionEntryReturn = transactionEntry;
					}
					else if (transactionEntry.getCreationtime().after(latest))
					{
						latest = transactionEntry.getCreationtime();
						transactionEntryReturn = transactionEntry;
					}
				}
			}
		}
		return transactionEntryReturn;
	}

	private void addCaptureTransactionRecord(final PreAuthCompleteResponse response, final AbstractOrderModel order, final PaymentTransactionType transType)
	{
		final PaymentTransactionModel paymentTransaction = getGiftCardTransaction(order, response.getCard().getCardNumber());
		if (paymentTransaction != null)
		{
			final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);

			BigDecimal amount = null;
			if (PaymentTransactionType.CANCEL.equals(transType))
			{
				amount = paymentTransaction.getPlannedAmount();
			}
			else
			{
				amount = BigDecimal.valueOf(response.getApprovedAmount() != null ? response.getApprovedAmount().getAmount() : 0);
			}

			entry.setType(transType);
			entry.setTime(new Date());
			entry.setCode(paymentService.getNewPaymentTransactionEntryCode(paymentTransaction, transType));
			entry.setCurrency(order.getCurrency());
			entry.setAmount(amount.setScale(2, RoundingMode.HALF_EVEN));
			entry.setRequestId(response.getTransactionID() != null ? response.getTransactionID() : TransactionStatus.ERROR.name());
			entry.setRequestToken(response.getAuthorizationCode() != null ? response.getAuthorizationCode() : TransactionStatus.ERROR.name());
			if (response.getReturnCode() != null)
			{
				entry.setTransactionStatus(response.getReturnCode().getReturnCode().equals(APPROVED) ? TransactionStatus.ACCEPTED.name() : response.getReturnCode().getReturnCode());
			}
			else
			{
				entry.setTransactionStatus("Didn't receive any response return code from SVS");
			}
			entry.setTransactionStatusDetails((response.getReturnCode() != null ? TransactionStatusDetails.SUCCESFULL.name() : response.getReturnCode().getReturnDescription()));
			final List<PaymentTransactionEntryModel> entries = new ArrayList<>(paymentTransaction.getEntries());
			entries.add(entry);
			paymentTransaction.setOrder(order);
			paymentTransaction.setEntries(entries);
			modelService.save(entry);
			modelService.save(paymentTransaction);
			modelService.save(order);
		}
	}

	private String addAuthPaymentTranactionRecord(final PreAuthResponse response, final CartModel sessionCart, final double requestedAmount)
	{
		final List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();
		PaymentTransactionModel paymentTransaction = null;
		List<PaymentTransactionEntryModel> entries = null;

		if (CollectionUtils.isNotEmpty(sessionCart.getPaymentTransactions()))
		{
			for (final PaymentTransactionModel transaction : sessionCart.getPaymentTransactions())
			{
				if (GIFT_CARD.equalsIgnoreCase(transaction.getPaymentProvider()) && transaction.getCode().equalsIgnoreCase(response.getCard().getCardNumber()))
				{
					paymentTransaction = transaction;
					entries = new ArrayList<>(transaction.getEntries());
				}
			}
			paymentTransactions.addAll(sessionCart.getPaymentTransactions());
		}

		final BigDecimal authorisedAmount = BigDecimal.valueOf(response.getApprovedAmount() != null ? response.getApprovedAmount().getAmount() : 0).setScale(2, RoundingMode.HALF_EVEN);

		if (paymentTransaction == null)
		{
			paymentTransaction = modelService.create(PaymentTransactionModel.class);
			paymentTransaction.setCode(response.getCard() != null ? response.getCard().getCardNumber() : StringUtils.EMPTY);
			paymentTransaction.setCurrency(sessionCart.getCurrency());
			paymentTransaction.setMerchantReferenceCode(getMerchant().getMerchantName());
			paymentTransaction.setOrder(sessionCart);
			paymentTransaction.setPaymentProvider(GIFT_CARD);
			paymentTransaction.setPlannedAmount(authorisedAmount);
			paymentTransaction.setRequestId(response.getTransactionID() != null ? response.getTransactionID() : TransactionStatus.ERROR.name());
			paymentTransactions.add(paymentTransaction);
			entries = new ArrayList<>();
		}

		final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);

		entry.setType(PaymentTransactionType.AUTHORIZATION);
		entry.setTime(new Date());
		entry.setCode(paymentService.getNewPaymentTransactionEntryCode(paymentTransaction, PaymentTransactionType.AUTHORIZATION));
		entry.setCurrency(sessionCart.getCurrency());
		entry.setAmount(authorisedAmount);
		entry.setRequestId(response.getTransactionID() != null ? response.getTransactionID() : TransactionStatus.ERROR.name());
		entry.setRequestToken(response.getAuthorizationCode() != null ? response.getAuthorizationCode() : TransactionStatus.ERROR.name());
		entry.setTransactionStatus(response.getReturnCode().getReturnCode().equals(APPROVED) ? TransactionStatus.ACCEPTED.name() : TransactionStatus.ERROR.name());
		entry.setTransactionStatusDetails(response.getReturnCode().getReturnCode().equals(APPROVED) ? TransactionStatusDetails.SUCCESFULL.name() : TransactionStatus.ERROR.name());

		entries.add(entry);
		paymentTransaction.setEntries(entries);
		paymentTransaction.setOrder(sessionCart);
		sessionCart.setPaymentTransactions(paymentTransactions);

		modelService.save(entry);
		modelService.save(paymentTransaction);
		modelService.save(sessionCart);

		if ((new BigDecimal(authorisedAmount.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN)).compareTo(((new BigDecimal(requestedAmount)).setScale(2, BigDecimal.ROUND_HALF_EVEN))) < 0)
		{
			return INSUFFICIENT_FUNDS;
		}
		return response.getReturnCode().getReturnCode();
	}

	@Override
	public PaymentTransactionModel getGiftCardTransaction(final AbstractOrderModel order, final String giftCardNumber)
	{
		if (order.getPaymentTransactions() != null)
		{
			for (final PaymentTransactionModel transaction : order.getPaymentTransactions())
			{
				if (transaction.getPaymentProvider().equals(GIFT_CARD) && transaction.getCode().equals(giftCardNumber))
				{
					return transaction;
				}
			}
		}
		return null;
	}

	private String inverse(final String merchantNumber, final int digits)
	{
		if (StringUtils.isBlank(merchantNumber))
		{
			return StringUtils.EMPTY;
		}

		if (merchantNumber.length() < digits)
		{
			return merchantNumber;
		}

		return swapPairs(merchantNumber, digits);
	}

	public String swapPairs(final String s, final int digits)
	{
		if (s.length() < digits)
		{
			return s;
		}
		else
		{
			return String.valueOf(s.charAt(1)) + String.valueOf(s.charAt(0)) + swapPairs(s.substring(digits), digits);
		}
	}
}
