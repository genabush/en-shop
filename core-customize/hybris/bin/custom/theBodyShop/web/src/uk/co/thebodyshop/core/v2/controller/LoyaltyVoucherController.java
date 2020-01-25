/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.v2.controller;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import uk.co.thebodyshop.loyalty.data.LoyaltyVoucherData;
import uk.co.thebodyshop.loyalty.data.LoyaltyVoucherDataList;
import uk.co.thebodyshop.loyalty.dto.LoyaltyVoucherListWsDTO;
import uk.co.thebodyshop.loyalty.voucher.LoyaltyVoucherFacade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Krishna
 */

@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts/{cartId}/loyalty")
@Api(tags = "Loyalty")
public class LoyaltyVoucherController extends BaseController
{
	@Resource(name = "loyaltyVoucherFacade")
	private LoyaltyVoucherFacade loyaltyVoucherFacade;

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/vouchers", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "getLoyaltyVouchers", value = "get loyalty vouchers.", notes = "get current user voucher.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public LoyaltyVoucherListWsDTO getLoyaltyVouchers(@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<LoyaltyVoucherData> userLoyaltyVouchers = loyaltyVoucherFacade.getLoyaltyVouchers();
		final LoyaltyVoucherDataList loyaltyVoucherData = new LoyaltyVoucherDataList();
		loyaltyVoucherData.setLoyaltyVouchers(userLoyaltyVouchers);
		return getDataMapper().map(loyaltyVoucherData, LoyaltyVoucherListWsDTO.class, DEFAULT_FIELD_SET);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/payment", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "applyLoyaltyVoucher", value = "apply loyalty voucher.", notes = "apply loyalty voucher to cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public LoyaltyVoucherListWsDTO applyLoyaltyVoucher(@ApiParam(value = "Voucher identifier (code)", required = true) @RequestParam @Nonnull final String voucherCode, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		LoyaltyVoucherDataList loyaltyVoucherData = new LoyaltyVoucherDataList();
		loyaltyVoucherData = loyaltyVoucherFacade.applyVoucherOnCart(voucherCode);
		return getDataMapper().map(loyaltyVoucherData, LoyaltyVoucherListWsDTO.class, DEFAULT_FIELD_SET);
	}
	
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/payment", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "removeLoyaltyVoucher", value = "remove loyalty voucher.", notes = "remove loyalty voucher from cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public LoyaltyVoucherListWsDTO removeLoyaltyVoucher(@ApiParam(value = "Voucher identifier (code)", required = true) @RequestParam @Nonnull final String voucherCode, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		LoyaltyVoucherDataList loyaltyVoucherData = loyaltyVoucherFacade.removeVoucherFromCart(voucherCode);
		return getDataMapper().map(loyaltyVoucherData, LoyaltyVoucherListWsDTO.class, DEFAULT_FIELD_SET);
	}
}
