/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.v2.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;

import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Marcin
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/userId/{userId}/cart/{cartId}/language/{language}/currency/{currency}")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "Payment Redirect")
public class PaymentRedirectController extends BaseCommerceController
{
	private static final String LOCATION = "Location";
	private static final String ENV_DOMAIN = "theBodyShop.environment.domain.storefront";
	private static final String HTTPS_PROTOCOL = "https://";
	private static final String PAYMENT_REDIRECT_URL = "/checkout?redirectPayment=true";
	private static final String THREE_D_MD = "MD";
	private static final String THREE_D_PARES = "PaRes";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@RequestMapping(value = "/paymentRedirectDetails", method = RequestMethod.POST, consumes =
		{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	@ApiOperation(nickname = "paymentRedirectDetails", value = "Retrieves details for payment redirect scenarios", notes = "Process payment redirect response")
	@ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
	public void placeOrder(	@PathVariable final String baseSiteId, @PathVariable final String userId, @PathVariable final String cartId, @PathVariable final String language, @PathVariable final String currency, final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
	{
		setupPaymentRedirectInfo(userId, cartId, httpRequest);
		httpResponse.setHeader(LOCATION, getRedirectPaymentUrl(baseSiteId, userId, cartId, language, currency));
	}

	@RequestMapping(value = "/paymentRedirectDetails", method = RequestMethod.GET, consumes =
		{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.ALL_VALUE })
	@ApiOperation(nickname = "paymentRedirectDetails", value = "Retrieves details for payment redirect scenarios", notes = "Process payment redirect response")
	@ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
	public void placeOrderGET(@PathVariable final String baseSiteId, @PathVariable final String userId, @PathVariable final String cartId, @PathVariable final String language, @PathVariable final String currency, final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse)
	{
		setupPaymentRedirectInfo(userId, cartId, httpRequest);
		httpResponse.setHeader(LOCATION, getRedirectPaymentUrl(baseSiteId, userId, cartId, language, currency));
	}

	/**
	 * @param userId
	 * @param cartId
	 * @param httpRequest
	 */
	private void setupPaymentRedirectInfo(final String userId, final String cartId, final HttpServletRequest httpRequest)
	{
		final String paRes = httpRequest.getParameter(THREE_D_PARES);
		final String md = httpRequest.getParameter(THREE_D_MD);
		final Map<String, String> details = setupPaymentDetails(httpRequest);

		final PaymentRedirectInfoModel paymentRedirectInfoModel = modelService.create(PaymentRedirectInfoModel.class);
		paymentRedirectInfoModel.setUserId(userId);
		paymentRedirectInfoModel.setCartId(cartId);
		paymentRedirectInfoModel.setAdyenPaReq(paRes);
		paymentRedirectInfoModel.setAdyenPaymentMD(md);
		paymentRedirectInfoModel.setPaymentDetails(details);
		modelService.save(paymentRedirectInfoModel);
	}

	private Map<String, String> setupPaymentDetails(final HttpServletRequest httpRequest)
	{
		final Map<String, String> details = new HashMap<>();
		final Map<String, String[]> params = httpRequest.getParameterMap();
		for (final Map.Entry<String, String[]> entry : params.entrySet())
		{
			if (!THREE_D_PARES.equals(entry.getKey()) && !THREE_D_MD.equals(entry.getKey()))
			{
				if (entry.getValue().length > 0)
				{
					details.put(entry.getKey(), entry.getValue()[0]);
				}
			}
		}
		return details;
	}

	private String getRedirectPaymentUrl(final String baseSiteId, final String userId, final String cartId, final String language, final String currency) {
		final StringBuilder redirectPaymentUrl = new StringBuilder(HTTPS_PROTOCOL + configurationService.getConfiguration().getString(ENV_DOMAIN));
		redirectPaymentUrl.append("/" + baseSiteId);
		redirectPaymentUrl.append("/" + language);
		redirectPaymentUrl.append("/" + currency);
		redirectPaymentUrl.append(PAYMENT_REDIRECT_URL);
		return redirectPaymentUrl.toString();
	}
}
