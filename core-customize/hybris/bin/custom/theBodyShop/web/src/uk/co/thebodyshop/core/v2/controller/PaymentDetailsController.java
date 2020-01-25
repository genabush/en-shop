/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package uk.co.thebodyshop.core.v2.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.adyen.model.checkout.PaymentsResponse;
import com.adyen.v6.exceptions.AdyenNonAuthorizedPaymentException;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoDatas;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.core.PK.PKException;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import uk.co.thebodyshop.core.exceptions.NoCheckoutCartException;
import uk.co.thebodyshop.core.order.TbsCheckoutFacade;
import uk.co.thebodyshop.core.populator.options.PaymentInfoOption;
import uk.co.thebodyshop.core.swagger.ApiBaseSiteIdAndUserIdAndPaymentDetailsParams;
import uk.co.thebodyshop.integration.adyen.facades.TbsAdyenCheckoutFacade;
import uk.co.thebodyshop.integration.adyen.service.AdyenPaymentRefusalMessageService;
import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;
import uk.co.thebodyshop.payment.services.PaymentRedirectInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/paymentdetails")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "Payment Details")
public class PaymentDetailsController extends BaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(PaymentDetailsController.class);

	private static final String OBJECT_NAME_PAYMENT_DETAILS = "paymentDetails";

	@Resource(name = "adyenCheckoutFacade")
	private TbsAdyenCheckoutFacade adyenCheckoutFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;

	@Resource(name = "tbsCheckoutFacade")
	private TbsCheckoutFacade tbsCheckoutFacade;

	@Resource(name = "adyenPaymentRefusalMessageService")
	private AdyenPaymentRefusalMessageService adyenPaymentRefusalMessageService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "paymentRedirectInfoService")
	private PaymentRedirectInfoService paymentRedirectInfoService;

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getPaymentDetailsList", value = "Get customer's credit card payment details list.", notes = "Return customer's credit card payment details list.")
	@ApiBaseSiteIdAndUserIdParam
	public PaymentDetailsListWsDTO getPaymentDetailsList(@ApiParam(value = "Type of payment details.") @RequestParam(defaultValue = "false") final boolean saved, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getPaymentDetailsList");
		}

		final CCPaymentInfoDatas paymentInfoDataList = new CCPaymentInfoDatas();
		paymentInfoDataList.setPaymentInfos(getUserFacade().getCCPaymentInfos(saved));

		return getDataMapper().map(paymentInfoDataList, PaymentDetailsListWsDTO.class, fields);
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{paymentDetailsId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getPaymentDetails", value = "Get customer's credit card payment details.", notes = "Returns a customer's credit card payment details for the specified paymentDetailsId.")
	@ApiBaseSiteIdAndUserIdParam
	public PaymentDetailsWsDTO getPaymentDetails(@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		return getDataMapper().map(getPaymentInfo(paymentDetailsId), PaymentDetailsWsDTO.class, fields);
	}

	public CCPaymentInfoData getPaymentInfo(final String paymentDetailsId)
	{
		LOG.debug("getPaymentInfo : id = " + sanitize(paymentDetailsId));
		try
		{
			final CCPaymentInfoData paymentInfoData = getUserFacade().getCCPaymentInfoForCode(paymentDetailsId);
			if (paymentInfoData == null)
			{
				throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.", RequestParameterException.UNKNOWN_IDENTIFIER, "paymentDetailsId");
			}
			return paymentInfoData;
		}
		catch (final PKException e)
		{
			throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.", RequestParameterException.UNKNOWN_IDENTIFIER, "paymentDetailsId", e);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{paymentDetailsId}", method = RequestMethod.DELETE)
	@ApiOperation(nickname = "removePaymentDetails", value = "Deletes customer's credit card payment details.", notes = "Deletes a customer's credit card payment details based on a specified paymentDetailsId.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void removePaymentDetails(@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removePaymentDetails: id = {}", sanitize(paymentDetailsId));
		}
		getPaymentInfo(paymentDetailsId);
		getUserFacade().removeCCPaymentInfo(paymentDetailsId);
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{paymentDetailsId}", method = RequestMethod.PATCH)
	@ApiOperation(hidden = true, value = "Updates existing customer's credit card payment details. ", notes = "Updates an existing customer's credit card payment "
			+ "details based on the specified paymentDetailsId. Only those attributes provided in the request will be updated.")
	@ApiBaseSiteIdAndUserIdAndPaymentDetailsParams
	@ResponseStatus(HttpStatus.OK)
	public void updatePaymentDetails(@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId, final HttpServletRequest request)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updatePaymentDetails: id = {}", sanitize(paymentDetailsId));
		}

		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);

		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		final Collection<PaymentInfoOption> options = new ArrayList<>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		getHttpRequestPaymentInfoPopulator().populate(request, paymentInfoData, options);
		validate(paymentInfoData, OBJECT_NAME_PAYMENT_DETAILS, getCcPaymentInfoValidator());

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{paymentDetailsId}", method = RequestMethod.PATCH, consumes =
{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(nickname = "updatePaymentDetails", value = "Updates existing customer's credit card payment details.", notes = "Updates an existing customer's credit card payment details based "
			+ "on the specified paymentDetailsId. Only those attributes provided in the request will be updated.")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void updatePaymentDetails(@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@ApiParam(value = "Payment details object", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails)
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();

		getDataMapper().map(paymentDetails, paymentInfoData, "accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,defaultPaymentInfo,saved,"
				+ "billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress)", false);
		validate(paymentInfoData, OBJECT_NAME_PAYMENT_DETAILS, getCcPaymentInfoValidator());

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{paymentDetailsId}", method = RequestMethod.PUT)
	@ApiOperation(hidden = true, value = "Updates existing customer's credit card payment details. ", notes = "Updates existing customer's credit card payment "
			+ "info based on the payment info ID. Attributes not given in request will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdAndPaymentDetailsParams
	@ResponseStatus(HttpStatus.OK)
	public void replacePaymentDetails(@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId, final HttpServletRequest request)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("replacePaymentDetails: id = {}", sanitize(paymentDetailsId));
		}

		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);

		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		paymentInfoData.setAccountHolderName(null);
		paymentInfoData.setCardNumber(null);
		paymentInfoData.setCardType(null);
		paymentInfoData.setExpiryMonth(null);
		paymentInfoData.setExpiryYear(null);
		paymentInfoData.setDefaultPaymentInfo(false);
		paymentInfoData.setSaved(false);

		paymentInfoData.setIssueNumber(null);
		paymentInfoData.setStartMonth(null);
		paymentInfoData.setStartYear(null);
		paymentInfoData.setSubscriptionId(null);

		final AddressData address = paymentInfoData.getBillingAddress();
		address.setFirstName(null);
		address.setLastName(null);
		address.setCountry(null);
		address.setLine1(null);
		address.setLine2(null);
		address.setPostalCode(null);
		address.setRegion(null);
		address.setTitle(null);
		address.setTown(null);

		final Collection<PaymentInfoOption> options = new ArrayList<>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		getHttpRequestPaymentInfoPopulator().populate(request, paymentInfoData, options);
		validate(paymentInfoData, OBJECT_NAME_PAYMENT_DETAILS, getCcPaymentInfoValidator());

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{paymentDetailsId}", method = RequestMethod.PUT, consumes =
{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(nickname = "replacePaymentDetails", value = "Updates existing customer's credit card payment info.", notes = "Updates existing customer's credit card payment info based on the "
			+ "payment info ID. Attributes not given in request will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(HttpStatus.OK)
	public void replacePaymentDetails(@ApiParam(value = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,
			@ApiParam(value = "Payment details object.", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails)
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();

		validate(paymentDetails, OBJECT_NAME_PAYMENT_DETAILS, getPaymentDetailsDTOValidator());
		getDataMapper().map(paymentDetails, paymentInfoData, "accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,defaultPaymentInfo,saved,billingAddress"
				+ "(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress)", true);

		getUserFacade().updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			getUserFacade().setDefaultPaymentInfo(paymentInfoData);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/carts/{cartId}", method = RequestMethod.POST, consumes =
{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(nickname = "addPaymentDetails", value = "Passes on encrypted card details to Payment Provider.", notes = "Process encrypted customer card details to Payment provider and place order after succesful response. ")
	@ApiBaseSiteIdUserIdAndCartIdParam
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public OrderWsDTO placeOrder(@ApiParam(value = "Payment details object", required = true) @RequestBody final PaymentDetailsWsDTO paymentDetails, @PathVariable final String cartId) throws InvalidCartException, NoCheckoutCartException, Exception
	{
		validate(paymentDetails, OBJECT_NAME_PAYMENT_DETAILS, getPaymentDetailsDTOValidator());

		cartLoaderStrategy.loadCart(cartId);
		boolean isStockProcessed = false;
		if (cartFacade.hasSessionCart())
		{
			try
			{
				final Map<String, String> errors = tbsCheckoutFacade.authSecondaryPayments();
				if (MapUtils.isNotEmpty(errors))
				{
					throw new Exception("Issues found during secondary payment authorization");
				}
				final boolean hasOutstaningAmount = tbsCheckoutFacade.hasOutstaningAmount();
				adyenCheckoutFacade.addPaymentDetails(paymentDetails, hasOutstaningAmount);
				validateCartForPlaceOrder();
				isStockProcessed = getTbsCheckoutFacade().processStock();
				OrderData orderData;

				if (hasOutstaningAmount)
				{
					orderData = adyenCheckoutFacade.authorisePayment(cartFacade.getSessionCart());
					getTbsCheckoutFacade().verifyOrderAuthorizationStatus(orderData);
				}
				else
				{
					orderData = getTbsCheckoutFacade().placeOrder();
				}

				return getDataMapper().map(orderData, OrderWsDTO.class, DEFAULT_FIELD_SET);
			}
			catch (final InsufficientStockLevelException isle)
			{
				throw new Exception(isle.getMessage());
			}
			catch (final AdyenNonAuthorizedPaymentException anape)
			{
				if (isStockProcessed)
				{
					getTbsCheckoutFacade().releaseStock();
				}

				String exceptionMessage = anape.getMessage();

				final PaymentsResponse paymentsResponse = anape.getPaymentsResponse();
				if (paymentsResponse != null)
				{
					final OrderData orderData = adyenCheckoutFacade.getPaymentRedirectInfo(paymentsResponse);
					if (orderData != null)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Redirecting the shopper to external page for cart [{}]", cartId);
						}
						return getDataMapper().map(orderData, OrderWsDTO.class, DEFAULT_FIELD_SET);
					}

					LOG.warn("The Adyen authorisation failed. Result Code [{}], Refusal Reason [{}], Reason code [{}]", paymentsResponse.getResultCode(), paymentsResponse.getRefusalReason(), paymentsResponse.getRefusalReasonCode());

					exceptionMessage = adyenPaymentRefusalMessageService.getRefusalMessage(paymentsResponse.getRefusalReasonCode());
				}

				tbsCheckoutFacade.releaseSecondaryPayments();

				throw new Exception(exceptionMessage);
			}
			catch (final Exception e)
			{
				LOG.error("An exception occured while authorising the Adyen payment", e);

				tbsCheckoutFacade.releaseSecondaryPayments();

				throw new Exception("Your payment could not be completed. Please try again.");
			}
		}

		throw new Exception("No cart in session");
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/carts/{cartId}/placeRedirectOrder", method = RequestMethod.POST, consumes =
{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(nickname = "placeRedirectOrder", value = "Places an order for payment redirect scenarios", notes = "Process payment redirect data and places an order after succesful response.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public OrderWsDTO placeOrder(@ApiParam(value = "Payment details object", required = true) @PathVariable final String cartId, final HttpServletRequest httpRequest) throws InvalidCartException, NoCheckoutCartException, Exception
	{
		cartLoaderStrategy.loadCart(cartId);
		if (cartFacade.hasSessionCart())
		{
			final CartData cartData = cartFacade.getSessionCart();
			final PaymentRedirectInfoModel paymentRedirectInfoModel = paymentRedirectInfoService.getPaymentRedirectInfoForUserAndCart(cartData.getUser().getUid(), cartData.getCode());
			final OrderData orderData = adyenCheckoutFacade.handlePaymentRedirectResponse(cartData, paymentRedirectInfoModel);
			tbsCheckoutFacade.verifyOrderAuthorizationStatus(orderData);
			paymentRedirectInfoService.clearAllPaymentRedirectInfoForUserAndCart(cartData.getUser().getUid(), cartData.getCode());
			return getDataMapper().map(orderData, OrderWsDTO.class, DEFAULT_FIELD_SET);
		}
		throw new Exception("No cart in session");
	}
}
