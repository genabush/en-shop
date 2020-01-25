/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.v2.controller;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.thebodyshop.core.wishlist.GuestWishlistProductData;
import uk.co.thebodyshop.core.wishlist.WishlistShareRequestData;
import uk.co.thebodyshop.core.wishlist.StatusResponseData;
import uk.co.thebodyshop.core.wishlist.WishlistData;
import uk.co.thebodyshop.core.wishlist.WishlistFacade;
import uk.co.thebodyshop.core.wishlist.WishlistCollectionData;
import uk.co.thebodyshop.core.wishlist.ws.dto.StatusResponseWSDTO;
import uk.co.thebodyshop.core.wishlist.ws.dto.WishlistCollectionWSDTO;
import uk.co.thebodyshop.core.wishlist.ws.dto.WishlistRequestWSDTO;
import uk.co.thebodyshop.core.wishlist.ws.dto.WishlistWSDTO;
import uk.co.thebodyshop.core.wishlist.ws.dto.GuestWishlistProductWSDTO;
import uk.co.thebodyshop.core.wishlist.ws.dto.WishlistShareRequestWSDTO;
import uk.co.thebodyshop.core.wishlist.ws.dto.WishlistSaveRequestWSDTO;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Jagadeesh
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/wishlist")
@Api(tags = "Wishlist")
public class WishlistController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(WishlistController.class);

	@Resource
	private WishlistFacade wishlistFacade;

	@Resource(name = "wishlistValidator")
	private Validator wishlistValidator;

	@Resource(name = "activeProductCatalogVersionSupplier")
	private Supplier<Optional<CatalogVersionModel>> activeProductCatalogVersionSupplier;

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "createWishlist", value = "Creates a wishlist for a user.", notes = "Creates a new wishlists to user.")
	@ApiBaseSiteIdAndUserIdParam
	public WishlistWSDTO createWishlist(@ApiParam(value = "The name and id of the wishlist to create/update a wishlist model.", required = true) @RequestBody final WishlistRequestWSDTO wishlist,
										@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		WishlistData wishlistData = null;
		validate(wishlist, "wishlist", wishlistValidator);
		if (null == wishlist.getWishlistId())
		{
			wishlistData = wishlistFacade.createWishlist(wishlist.getWishlistName());
		}
		else
		{
			wishlistData = wishlistFacade.updateWishlistById(wishlist.getWishlistId(), wishlist.getWishlistName());
		}
		return getDataMapper().map(wishlistData, WishlistWSDTO.class, DEFAULT_FIELD_SET);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "getWishlist", value = "gets all wishlists for a user.", notes = "gets all wishlists to user.")
	@ApiBaseSiteIdAndUserIdParam
	public WishlistCollectionWSDTO getWishlists(@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		WishlistCollectionData allWishlists = wishlistFacade.getAllWishlists();
		allWishlists.getWishlists().sort(Comparator.comparing(WishlistData::getWishlistName));
		return getDataMapper().map(allWishlists, WishlistCollectionWSDTO.class, DEFAULT_FIELD_SET);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{wishlistId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "addWishlistEntry", value = "To add a product to wishlist model.", notes = "Creates a new wishlist entry to wishlist.")
	@ApiBaseSiteIdAndUserIdParam
	public WishlistWSDTO addWishlistEntry(@ApiParam(value = "wishlistId", required = true) @PathVariable final String wishlistId, @ApiParam(value = "productCode", required = true) @RequestParam final String productCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final WishlistData wishlistData = wishlistFacade.addWishListEntryWithProduct(wishlistId, productCode);
		return getDataMapper().map(wishlistData, WishlistWSDTO.class, DEFAULT_FIELD_SET);
	}

	@RequestMapping(value = "/{wishlistId}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "getWishlist", value = "get wishlist by id.", notes = "get wishlist by id.")
	@ApiBaseSiteIdAndUserIdParam
	public WishlistWSDTO getWishlistById(@ApiParam(value = "wishlistId", required = true) @PathVariable final String wishlistId, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final WishlistData wishlistData = wishlistFacade.getWishlistById(wishlistId);
		return getDataMapper().map(wishlistData, WishlistWSDTO.class, DEFAULT_FIELD_SET);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{wishlistId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "removeWishlist", value = "remove wishlist by id.", notes = "remove wishlist by id.")
	@ApiBaseSiteIdAndUserIdParam
	public StatusResponseWSDTO deleteWishlistById(@ApiParam(value = "wishlistId", required = true) @PathVariable final String wishlistId, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final StatusResponseData wishlistData = wishlistFacade.removeWishlistById(wishlistId);
		return getDataMapper().map(wishlistData, StatusResponseWSDTO.class, DEFAULT_FIELD_SET);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{wishlistId}/product/{productCode}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "removeWishlistEntry", value = "remove wishlist entry from wishlist model.", notes = "remove wishlist entry from wishlist model.")
	@ApiBaseSiteIdAndUserIdParam
	public WishlistWSDTO removeWishlistEntry(@ApiParam(value = "wishlistId", required = true) @PathVariable final String wishlistId, @ApiParam(value = "productCode", required = true) @PathVariable final String productCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final WishlistData wishlistData = wishlistFacade.removeWishlistEntryByProduct(wishlistId, productCode);
		return getDataMapper().map(wishlistData, WishlistWSDTO.class, DEFAULT_FIELD_SET);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/share", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "shareWishlist", value = "share wishlist to given email.", notes = "share wishlist to given email.")
	@ApiBaseSiteIdAndUserIdParam
	public StatusResponseWSDTO shareWishlist(@ApiParam(value = "The data to share the wishlist to give email id .", required = true) @RequestBody final WishlistShareRequestWSDTO wishlistShareRequest,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final StatusResponseData wishlistData = wishlistFacade.shareWishlist(populateShareWishlist(wishlistShareRequest));
		return getDataMapper().map(wishlistData, StatusResponseWSDTO.class, DEFAULT_FIELD_SET);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(nickname = "saveWishlist", value = "save wishlist to current user.", notes = "save wishlist to current user.")
	@ApiBaseSiteIdAndUserIdParam
	public StatusResponseWSDTO saveWishlist(@ApiParam(value = "wishlist id to save to the current user.", required = true) @RequestBody final WishlistSaveRequestWSDTO wishlistSaveRequest,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final StatusResponseData wishlistData = wishlistFacade.saveWishlistToCurrentUser(wishlistSaveRequest.getWishlistId());
		return getDataMapper().map(wishlistData, StatusResponseWSDTO.class, DEFAULT_FIELD_SET);
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "GuestWishlistProductsData", value = "get guest wishtlist product data by product codes.", notes = "get guest wishtlist product data by product codes.")
	@ApiBaseSiteIdAndUserIdParam
	public GuestWishlistProductWSDTO getWishlistProductsData(@ApiParam(value = "productCodes", required = true) @RequestParam @Nonnull final List<String> productCodes, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		GuestWishlistProductData guestWishlistproductData = wishlistFacade.getGuestWishlistProductData(productCodes, activeProductCatalogVersionSupplier.get().orElse(null));
		return getDataMapper().map(guestWishlistproductData, GuestWishlistProductWSDTO.class, DEFAULT_FIELD_SET);
	}
	private WishlistShareRequestData populateShareWishlist(final WishlistShareRequestWSDTO wishlistShareRequest)
	{
		final WishlistShareRequestData wishlistShareRequestData = new WishlistShareRequestData();
		wishlistShareRequestData.setMessage(wishlistShareRequest.getMessage());
		wishlistShareRequestData.setRecipientEmail(wishlistShareRequest.getRecipientEmail());
		wishlistShareRequestData.setSenderName(wishlistShareRequest.getSenderName());
		wishlistShareRequestData.setWishlistId(wishlistShareRequest.getWishlistId());
		wishlistShareRequestData.setRecipientName(wishlistShareRequest.getRecipientName());
		return wishlistShareRequestData;
	}

}
