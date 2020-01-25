/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.v2.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;

import uk.co.thebodyshop.core.order.TbsCheckoutFacade;
import uk.co.thebodyshop.integration.paypal.facade.PaypalFacade;
import uk.co.thebodyshop.paypal.data.PaypalCreateOrderResponse;

import io.swagger.annotations.Api;

/**
 * @author vasanthramprakasam
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/paypal/order/{cartId}")
@Api(tags = "Paypal payment")
public class PaypalController extends BaseCommerceController
{
	@Resource
	private PaypalFacade paypalFacade;

	@Resource
	private CartLoaderStrategy cartLoaderStrategy;

	@Resource
	private CartFacade cartFacade;

	@Resource
	private TbsCheckoutFacade tbsCheckoutFacade;

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@ApiBaseSiteIdUserIdAndCartIdParam
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public PaypalCreateOrderResponse createOrder(@PathVariable final String cartId)
	{
		cartLoaderStrategy.loadCart(cartId);
		if (cartFacade.hasSessionCart())
		{
			return paypalFacade.createOrder(cartFacade.getSessionCart());
		}
		throw new CartException(CartException.NOT_FOUND);
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@ApiBaseSiteIdUserIdAndCartIdParam
	@RequestMapping(value = "/authorize", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public OrderWsDTO authorizeOrder(@RequestParam final String orderId,@PathVariable final String cartId) throws Exception
	{
		cartLoaderStrategy.loadCart(cartId);
		boolean isStockProcessed = false;
		if (cartFacade.hasSessionCart())
		{
			final Map<String, String> errors = tbsCheckoutFacade.authSecondaryPayments();
			if (MapUtils.isNotEmpty(errors))
			{
				throw new Exception("Issues found during secondary payment authorization");
			}
			try
			{
				isStockProcessed = getTbsCheckoutFacade().processStock();
				final OrderData orderData = paypalFacade.authorizePayment(orderId).orElseThrow(() -> new CartException(CartException.NOT_FOUND));
				return getDataMapper().map(orderData, OrderWsDTO.class, DEFAULT_FIELD_SET);
			}
			catch (final InsufficientStockLevelException isle)
			{
				throw new Exception(isle.getMessage());
			}
			catch (final Exception e)
			{
				if (isStockProcessed)
				{
					getTbsCheckoutFacade().releaseStock();
				}
				throw new Exception(e.getMessage());
			}
		}
		throw new CartException(CartException.NOT_FOUND);
	}

}
