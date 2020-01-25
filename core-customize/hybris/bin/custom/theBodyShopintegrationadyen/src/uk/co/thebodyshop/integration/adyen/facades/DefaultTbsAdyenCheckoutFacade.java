/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.facades;

import static com.adyen.v6.constants.Adyenv6coreConstants.KLARNA;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_CC;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_IDEAL;
import static com.adyen.v6.constants.Adyenv6coreConstants.PAYMENT_METHOD_ONECLICK;
import static uk.co.thebodyshop.integration.adyen.constants.TheBodyShopintegrationadyenConstants.PAYMENT_METHOD_GIROPAY;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.adyen.model.Card;
import com.adyen.model.checkout.PaymentsResponse;
import com.adyen.model.checkout.PaymentsResponse.ResultCodeEnum;
import com.adyen.model.recurring.Recurring;
import com.adyen.model.recurring.RecurringDetail;
import com.adyen.service.exception.ApiException;
import com.adyen.v6.exceptions.AdyenNonAuthorizedPaymentException;
import com.adyen.v6.facades.DefaultAdyenCheckoutFacade;
import com.adyen.v6.model.RequestInfo;

import uk.co.thebodyshop.integration.adyen.service.TbsAdyenTransactionService;
import uk.co.thebodyshop.payment.data.PaymentRedirectResponseData;
import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;


/**
 *
 */
public class DefaultTbsAdyenCheckoutFacade extends DefaultAdyenCheckoutFacade implements TbsAdyenCheckoutFacade
{
	private TbsAdyenTransactionService tbsAdyenTransactionService;

	@Override
	public OrderData authorisePayment(final CartData cartData) throws Exception
	{
		CustomerModel customer = null;
		if (!getCheckoutCustomerStrategy().isAnonymousCheckout())
		{
			customer = getCheckoutCustomerStrategy().getCurrentUserForCheckout();
		}

		final PaymentsResponse paymentsResponse = getAdyenPaymentService().authorisePayment(cartData, RequestInfo.empty(),
				customer);

		//In case of Authorized: create order and authorize it
		if (PaymentsResponse.ResultCodeEnum.AUTHORISED == paymentsResponse.getResultCode())
		{
			return createAuthorizedOrder(paymentsResponse, false);
		}

		//In case of Received: create order
		if (PaymentsResponse.ResultCodeEnum.RECEIVED == paymentsResponse.getResultCode())
		{
			return createOrderFromPaymentsResponse(paymentsResponse);
		}

		if (PaymentsResponse.ResultCodeEnum.PRESENTTOSHOPPER == paymentsResponse.getResultCode())
		{
			return createOrderFromPaymentsResponse(paymentsResponse);
		}

		throw new AdyenNonAuthorizedPaymentException(paymentsResponse);
	}

	/**
	 * Create order
	 */
	private OrderData createOrderFromPaymentsResponse(final PaymentsResponse paymentsResponse) throws InvalidCartException
	{
		LOGGER.debug("Create order from paymentsResponse: " + paymentsResponse.getPspReference());

		final OrderData orderData = getCheckoutFacade().placeOrder();
		final OrderModel orderModel = getOrderRepository().getOrderModel(orderData.getCode());
		updateOrder(orderModel, paymentsResponse);

		orderData.setAdyenBoletoUrl(paymentsResponse.getBoletoUrl());
		orderData.setAdyenBoletoData(paymentsResponse.getBoletoData());
		orderData.setAdyenBoletoBarCodeReference(paymentsResponse.getBoletoBarCodeReference());
		orderData.setAdyenBoletoExpirationDate(paymentsResponse.getBoletoExpirationDate());
		orderData.setAdyenBoletoDueDate(paymentsResponse.getBoletoDueDate());

		orderData.setAdyenMultibancoEntity(paymentsResponse.getMultibancoEntity());
		orderData.setAdyenMultibancoAmount(paymentsResponse.getMultibancoAmount());
		orderData.setAdyenMultibancoDeadline(paymentsResponse.getMultibancoDeadline());
		orderData.setAdyenMultibancoReference(paymentsResponse.getMultibancoReference());

		return orderData;
	}

	private void updateOrder(final OrderModel orderModel, final PaymentsResponse paymentsResponse)
	{
		try
		{
			getAdyenOrderService().updateOrderFromPaymentsResponse(orderModel, paymentsResponse);
		}
		catch (final Exception e)
		{
			LOGGER.error(e);
		}
	}

	@Override
	public PaymentDetailsWsDTO addPaymentDetails(final PaymentDetailsWsDTO paymentDetails, final boolean isAdyenPaymentRequired)
	{
		final CartModel cartModel = getCartService().getSessionCart();

		final AddressModel billingAddress = createBillingAddress(paymentDetails);
		PaymentInfoModel paymentInfo = null;
		if (isAdyenPaymentRequired)
		{
			paymentInfo = createPaymentInfo(cartModel, paymentDetails);
		}
		else
		{
			paymentInfo = cartModel.getPaymentInfo();
		}
		paymentInfo.setBillingAddress(billingAddress);
		billingAddress.setOwner(paymentInfo);

		getModelService().save(paymentInfo);

		cartModel.setPaymentInfo(paymentInfo);
		getModelService().save(cartModel);

		return paymentDetails;
	}

	private AddressModel createBillingAddress(final PaymentDetailsWsDTO paymentDetails)
	{
		final String titleCode = paymentDetails.getBillingAddress().getTitleCode();
		final AddressModel billingAddress = getModelService().create(AddressModel.class);
		if (StringUtils.isNotBlank(titleCode))
		{
			final TitleModel title = new TitleModel();
			title.setCode(titleCode);
			billingAddress.setTitle(getFlexibleSearchService().getModelByExample(title));
		}
		billingAddress.setFirstname(paymentDetails.getBillingAddress().getFirstName());
		billingAddress.setLastname(paymentDetails.getBillingAddress().getLastName());
		billingAddress.setLine1(paymentDetails.getBillingAddress().getLine1());
		billingAddress.setLine2(paymentDetails.getBillingAddress().getLine2());
		billingAddress.setTown(paymentDetails.getBillingAddress().getTown());
		billingAddress.setPostalcode(paymentDetails.getBillingAddress().getPostalCode());
		billingAddress.setCountry(getCommonI18NService().getCountry(paymentDetails.getBillingAddress().getCountry().getIsocode()));

		final AddressData addressData = new AddressData();
		addressData.setTitleCode(paymentDetails.getBillingAddress().getTitleCode());
		addressData.setFirstName(billingAddress.getFirstname());
		addressData.setLastName(billingAddress.getLastname());
		addressData.setLine1(billingAddress.getLine1());
		addressData.setLine2(billingAddress.getLine2());
		addressData.setTown(billingAddress.getTown());
		addressData.setPostalCode(billingAddress.getPostalcode());
		addressData.setBillingAddress(true);

		if (paymentDetails.getBillingAddress().getCountry() != null)
		{
			final CountryData countryData = getI18NFacade()
					.getCountryForIsocode(paymentDetails.getBillingAddress().getCountry().getIsocode());
			addressData.setCountry(countryData);
		}
		if (paymentDetails.getBillingAddress().getRegion().getIsocode() != null)
		{
			final RegionData regionData = getI18NFacade().getRegion(paymentDetails.getBillingAddress().getCountry().getIsocode(),
					paymentDetails.getBillingAddress().getRegion().getIsocode());
			addressData.setRegion(regionData);
		}

		getAddressReverseConverter().convert(addressData, billingAddress);

		return billingAddress;
	}

	@Override
	public OrderData handleRedirectPayload(final String paymentData, final HashMap<String, String> details)
	{
		try
		{
			final PaymentsResponse paymentsResponse = getAdyenPaymentService().getPaymentDetailsFromPayload(details, paymentData);
			final CartData cartData = getCheckoutFacade().getCheckoutCart();
			if (!cartData.getCode().equals(paymentsResponse.getMerchantReference()))
			{
				throw new InvalidCartException("Merchant reference doesn't match cart's code");
			}

			if (PaymentsResponse.ResultCodeEnum.AUTHORISED == paymentsResponse.getResultCode())
			{
				return createAuthorizedOrder(paymentsResponse, false);
			}
			else if (PaymentsResponse.ResultCodeEnum.RECEIVED == paymentsResponse.getResultCode())
			{
				return createAuthorizedOrder(paymentsResponse, true);
			}
		}
		catch (final Exception e)
		{
			LOGGER.warn(e);
		}
		throw new IllegalArgumentException("Invalid payload");
	}

	@Override
	public OrderData handle3DResponse(final PaymentTransactionModel paymentRedirectTransaction, final String paRes,
			final String md) throws Exception
	{
		if (Objects.isNull(paymentRedirectTransaction))
		{
			throw new SignatureException("MD does not match!");
		}
		final String transactionMd = paymentRedirectTransaction.getAdyenPaymentMD();
		final String transactionPaymentData = paymentRedirectTransaction.getAdyenPaymentData();
		try
		{
			final PaymentsResponse paymentsResponse = getAdyenPaymentService().authorise3DPayment(transactionPaymentData, paRes, md);
			if (PaymentsResponse.ResultCodeEnum.AUTHORISED == paymentsResponse.getResultCode())
			{
				return createAuthorizedOrder(paymentsResponse, false);
			}
			throw new AdyenNonAuthorizedPaymentException(paymentsResponse);
		}
		catch (final Exception e)
		{
			throw e;
		}
	}

	private OrderData createAuthorizedOrder(final PaymentsResponse paymentsResponse, final boolean isAuthPending)
			throws InvalidCartException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final String merchantTransactionCode = cartModel.getCode();

		//First save the transactions to the CartModel < AbstractOrderModel
		if (isAuthPending)
		{
			getTbsAdyenTransactionService().markOrderAsAuthorizationPending(cartModel, merchantTransactionCode,
					paymentsResponse.getPspReference());
		}
		else
		{
			getTbsAdyenTransactionService().authorizeOrderModel(cartModel, merchantTransactionCode,
					paymentsResponse.getPspReference());
		}

		return createOrderFromPaymentsResponse(paymentsResponse);
	}

	@Override
	public PaymentDetailsListWsDTO getPaymentDetails() throws IOException, ApiException
	{
		final CustomerModel customer = getCheckoutCustomerStrategy().getCurrentUserForCheckout();

		final List<RecurringDetail> recurringDetails = getAdyenPaymentService().getStoredCards(customer.getCustomerID());
		final List<RecurringDetail> sortedRecurringDetails = recurringDetails.stream()
				.sorted(Comparator.comparing(RecurringDetail::getCreationDate).reversed()).collect(Collectors.toList());

		final PaymentDetailsListWsDTO paymentDetailsListWsDTO = new PaymentDetailsListWsDTO();
		paymentDetailsListWsDTO.setPayments(toPaymentDetails(sortedRecurringDetails));

		return paymentDetailsListWsDTO;
	}

	private List<PaymentDetailsWsDTO> toPaymentDetails(final List<RecurringDetail> recurringDetails)
	{
		return recurringDetails.stream().map(r -> toPaymentDetail(r)).collect(Collectors.toList());
	}

	private PaymentDetailsWsDTO toPaymentDetail(final RecurringDetail recurringDetail)
	{
		final PaymentDetailsWsDTO paymentDetailsWsDTO = new PaymentDetailsWsDTO();

		final Card card = recurringDetail.getCard();

		if (card == null)
		{
			throw new RuntimeException("Card information not found");
		}

		paymentDetailsWsDTO.setAccountHolderName(card.getHolderName());
		paymentDetailsWsDTO.setCardNumber("**** **** **** " + card.getNumber());
		paymentDetailsWsDTO.setExpiryMonth(card.getExpiryMonth());
		paymentDetailsWsDTO.setExpiryYear(card.getExpiryYear());
		paymentDetailsWsDTO.setSubscriptionId(recurringDetail.getRecurringDetailReference());
		paymentDetailsWsDTO.setAdyenSelectedReference(recurringDetail.getRecurringDetailReference());
		if (recurringDetail.getContractTypes().contains(Recurring.ContractEnum.ONECLICK.name()))
		{
			paymentDetailsWsDTO.setAdyenPaymentMethod(PAYMENT_METHOD_ONECLICK);
		}
		paymentDetailsWsDTO.setCardPaymentRequired(true);
		return paymentDetailsWsDTO;
	}

	@Override
	public OrderData getPaymentRedirectInfo(final PaymentsResponse paymentsResponse)
	{
		try
		{
			if (ResultCodeEnum.REDIRECTSHOPPER.equals(paymentsResponse.getResultCode()))
			{
				return getRedirectShopperInfo(paymentsResponse);
			}
		}
		catch (final Exception ex)
		{
			LOGGER.error("An exception occured while trying to set-up the response data to redirect the shopper", ex);
			return null;
		}
		return null;
	}

	private OrderData getRedirectShopperInfo(final PaymentsResponse paymentsResponse)
	{
		getTbsAdyenTransactionService().createPaymentRedirectTransaction(getCartService().getSessionCart(), paymentsResponse);
		final OrderData orderData = new OrderData();
		final PaymentRedirectResponseData redirectResponseData = new PaymentRedirectResponseData();
		if (null != paymentsResponse.getRedirect().getMethod())
		{
			redirectResponseData.setRedirectUrl(paymentsResponse.getRedirect().getUrl());
			redirectResponseData.setMethod(paymentsResponse.getRedirect().getMethod().name());
		}
		else
		{
			redirectResponseData.setRedirectUrl(paymentsResponse.getAction().getUrl());
			redirectResponseData.setMethod(paymentsResponse.getAction().getMethod());
		}
		redirectResponseData.setData(paymentsResponse.getRedirect().getData());
		orderData.setPaymentRedirect(redirectResponseData);
		return orderData;
	}

	@Override
	public OrderData handlePaymentRedirectResponse(final CartData cartData,
			final PaymentRedirectInfoModel paymentRedirectInfoModel) throws Exception
	{
		if (PAYMENT_METHOD_CC.equals(cartData.getAdyenPaymentMethod())
				|| PAYMENT_METHOD_ONECLICK.equals(cartData.getAdyenPaymentMethod()))
		{
			return handlePaymentRedirectMethodCC(paymentRedirectInfoModel);
		}
		else if (PAYMENT_METHOD_IDEAL.equals(cartData.getAdyenPaymentMethod())
				|| PAYMENT_METHOD_GIROPAY.equals(cartData.getAdyenPaymentMethod())
				|| (cartData.getAdyenPaymentMethod().startsWith(KLARNA)))
		{
			return handlePaymentRedirect(paymentRedirectInfoModel);
		}
		LOGGER.error("Couldn't handle payment redirect response for cart :: " + cartData.getCode() + " and user with uid :: "
				+ cartData.getUser().getUid());
		throw new Exception("Your payment could not be completed. Please try again.");
	}

	private OrderData handlePaymentRedirectMethodCC(final PaymentRedirectInfoModel paymentRedirectInfoModel) throws Exception
	{
		final PaymentTransactionModel paymentRedirectTransaction = getTbsAdyenTransactionService()
				.getPaymentTransactionForMd(getCartService().getSessionCart(), paymentRedirectInfoModel.getAdyenPaymentMD());
		try
		{
			final OrderData orderData = handle3DResponse(paymentRedirectTransaction, paymentRedirectInfoModel.getAdyenPaReq(),
					paymentRedirectInfoModel.getAdyenPaymentMD());
			return orderData;
		}
		catch (final Exception ex)
		{
			LOGGER.error("Unable to process redirect payment for cart ::  " + getCartService().getSessionCart().getCode()
					+ " - payment method :: " + PAYMENT_METHOD_CC, ex);
			getTbsAdyenTransactionService().handlePaymentRedirectFailure(paymentRedirectTransaction);
		}
		throw new Exception("Please amend your card details");
	}

	private OrderData handlePaymentRedirect(final PaymentRedirectInfoModel paymentRedirectInfoModel) throws Exception
	{
		final PaymentTransactionModel paymentRedirectTransaction = getTbsAdyenTransactionService()
				.getLatestRedirectPaymentTransaction(getCartService().getSessionCart());
		try
		{
			final HashMap<String, String> paymentDetails = (paymentRedirectInfoModel.getPaymentDetails() instanceof HashMap)
					? (HashMap) paymentRedirectInfoModel.getPaymentDetails()
					: new HashMap<String, String>(paymentRedirectInfoModel.getPaymentDetails());
			final OrderData orderData = handleRedirectPayload(paymentRedirectTransaction.getAdyenPaymentData(), paymentDetails);
			return orderData;
		}
		catch (final Exception ex)
		{
			LOGGER.error("Unable to process redirect payment for cart ::  " + getCartService().getSessionCart().getCode()
					+ " - payment method :: " + PAYMENT_METHOD_CC, ex);
			getTbsAdyenTransactionService().handlePaymentRedirectFailure(paymentRedirectTransaction);
		}
		throw new Exception("Your payment could not be completed. Please try again.");
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

}