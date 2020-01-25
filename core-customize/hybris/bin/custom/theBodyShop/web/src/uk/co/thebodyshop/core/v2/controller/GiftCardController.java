/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.v2.controller;

import java.math.BigDecimal;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import uk.co.thebodyshop.core.validator.GiftCardRequestValidator;
import uk.co.thebodyshop.integration.svs.facades.GiftCardFacade;
import uk.co.thebodyshop.integrations.svs.data.GiftCardResponseData;
import uk.co.thebodyshop.integrations.svs.dto.GiftCardRequestWsDTO;
import uk.co.thebodyshop.integrations.svs.dto.GiftCardResponseWsDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "GiftCard")
@RequestMapping(value = "/{baseSiteId}")
public class GiftCardController extends BaseController
{
	@Resource(name = "giftCardFacade")
	private GiftCardFacade giftCardFacade;
	@Resource(name = "giftCardRequestValidator")
	private GiftCardRequestValidator giftCardRequestValidator;
	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name="priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@RequestMapping(value = "/giftCard/checkbalance", method = RequestMethod.POST, consumes =
		{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(nickname = "getGiftCardBalance", value = "Get gift card balance.", notes = "Returns gift card balance for specific gift card number and pin.")
	@ApiBaseSiteIdParam
	public GiftCardResponseWsDTO getGiftCardBalance(@ApiParam(value = "Gift Card Request object.", required = true)
	@RequestBody
	final GiftCardRequestWsDTO giftCardRequest, @ApiFieldsParam
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields, final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
	{
		giftCardRequest.setPinRequired(true);
		validate(giftCardRequest, "giftCardRequest", giftCardRequestValidator);
		final CurrencyModel currency = commerceCommonI18NService.getCurrentCurrency();
		final Double giftCardBalance = giftCardFacade.getGiftCardBalance(giftCardRequest.getGiftCardNumber(), giftCardRequest.getGiftCardPin(), Objects.nonNull(currency) ? currency.getIsocode() : "GBP");
		final GiftCardResponseData giftCardData = new GiftCardResponseData();
		giftCardData.setGiftCardNumber(giftCardRequest.getGiftCardNumber());
		if (Objects.nonNull(giftCardBalance))
		{
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(giftCardBalance), currency.getIsocode());
			giftCardData.setGiftCardBalance(priceData.getFormattedValue());
			giftCardData.setSuccess(true);
		}
		else
		{
			giftCardData.setSuccess(false);
		}
		return getDataMapper().map(giftCardData, GiftCardResponseWsDTO.class, fields);
	}

	@RequestMapping(value = "/users/{userId}/carts/{cartId}/giftCard/addGiftCard", method = RequestMethod.POST, consumes =
		{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "addGiftCardToCart", value = "Add Gift Card to cart", notes = "Add gift card to cart. Requires the following " + "parameters: giftCardNumber, giftCardPin.")
	@ApiBaseSiteIdAndUserIdParam
	public GiftCardResponseWsDTO addGiftCard(@ApiParam(value = "cartId", required = true) @PathVariable final String cartId, @ApiParam(value = "Gift Card Request object.", required = true)
	@RequestBody
	final GiftCardRequestWsDTO giftCardRequest, @ApiFieldsParam
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields, final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
	{
		giftCardRequest.setPinRequired(true);
		validate(giftCardRequest, "giftCardRequest", giftCardRequestValidator);
		final GiftCardResponseData giftCardData = giftCardFacade.applyGiftCardToCart(giftCardRequest.getGiftCardNumber(), giftCardRequest.getGiftCardPin(), cartService.getSessionCart());
		return getDataMapper().map(giftCardData, GiftCardResponseWsDTO.class, fields);
	}

	@RequestMapping(value = "/users/{userId}/carts/{cartId}/giftCard/removeGiftCard", method = RequestMethod.DELETE, consumes =
		{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "removeGiftCardFromCart", value = "Remove Gift Card from cart", notes = "Remove gift card from cart. Requires the following " + "parameters: giftCardNumber")
	@ApiBaseSiteIdParam
	public GiftCardResponseWsDTO removeGiftCard(@ApiParam(value = "Gift Card Request object.", required = true)
	@RequestBody
	final GiftCardRequestWsDTO giftCardRequest, @ApiFieldsParam
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields, final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
	{
		giftCardRequest.setPinRequired(false);
		validate(giftCardRequest, "giftCardRequest", giftCardRequestValidator);
		giftCardFacade.removeGiftCard(giftCardRequest.getGiftCardNumber(), cartService.getSessionCart());
		final GiftCardResponseData giftCardData = new GiftCardResponseData();
		giftCardData.setGiftCardNumber(giftCardRequest.getGiftCardNumber());
		giftCardData.setSuccess(true);
		return getDataMapper().map(giftCardData, GiftCardResponseWsDTO.class, fields);
	}
}
