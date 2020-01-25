/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.v2.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commercewebservicescommons.dto.order.CollectionPointResponseWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CollectionPointWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.status.StatusWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;

import uk.co.thebodyshop.collectionpoints.response.data.CollectionPointResponseData;
import uk.co.thebodyshop.core.collection.CollectionPointData;
import uk.co.thebodyshop.core.collection.CollectionPointResponseDTO;
import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.exceptions.NoCheckoutCartException;
import uk.co.thebodyshop.core.exceptions.UnsupportedRequestException;
import uk.co.thebodyshop.core.v2.helper.CollectionPointDataJsonHelper;
import uk.co.thebodyshop.facades.collectionpoint.search.CollectionPointSearchFacade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller for store locator search and detail pages.
 *
 * @author prateek.goel
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/collection-point")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
@Api(tags = "CollectionPoints")
public class CollectionPointController extends BaseCommerceController
{

	private static final Logger LOG = LoggerFactory.getLogger(CollectionPointController.class);

	@Resource(name = "collectionPointSearchFacade")
	private CollectionPointSearchFacade collectionPointSearchFacade;

	@Resource(name = "collectionPointDataJsonHelper")
	private CollectionPointDataJsonHelper collectionPointDataJsonHelper;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "findCollectionPoints", value = "Find the Stores as per current location or as per town or post code entered", notes = "Returns the store results data having information")
	@ApiBaseSiteIdAndUserIdParam
	public CollectionPointResponseWsDTO findCollectionPoints(@RequestParam(value = "q", required = false)
	final String locationQuery, @RequestParam(value = "lat", required = false)
	final Double latitude, @RequestParam(value = "long", required = false)
	final Double longitude, @ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields)
			throws GeoLocatorException, MapServiceException {

		final GeoPoint geoPoint = ofGeoPoint(latitude, longitude);
		final String sanitizedSearchQuery = filter(locationQuery);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Performing search for click and collect points for query: [{}], gps coordinates [{},{}]", locationQuery, latitude, longitude);
		}
		final CollectionPointResponseDTO collectionPointResponse = searchCollectionPoints(sanitizedSearchQuery, geoPoint);
		return getDataMapper().map(collectionPointResponse, CollectionPointResponseWsDTO.class, fields);
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/carts/{cartId}/save", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Added the cart collection point address for the cart.", notes = "Creates an address and assigns it to the cart as the collectionPoint address.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CollectionPointWsDTO createCartCollectionPointDetails(@ApiParam(value = "Request body parameter that contains details such as the customer's first name (firstName), the customer's last name (lastName), "
			+ "the country (country.isocode), the first part of the address (line1), the second part of the address (line2), the town (town), the postal code (postalCode).\n\nThe DTO is in XML or .json format.", required = true)
	@RequestBody
	final CollectionPointWsDTO collectionPoint, @ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields) throws NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("createCartCollectionPointDetails");
		}
		final CollectionPointData collectionPointData = getDataMapper().map(collectionPoint, CollectionPointData.class);
		AddressData addressData = collectionPointData.getAddress();
		final AddressData collectionPointDeliveryAddress = getTbsCheckoutFacade().getCollectionDeliveryAddress(FulfillmentMethodEnum.COLLECTION);
		if (null != collectionPointDeliveryAddress)
		{
			updateCollectionAddressInternal(collectionPointDeliveryAddress, addressData);
		}
		else
		{
			addressData = createCollectionAddressInternal(addressData);
			getTbsCheckoutFacade().updateFulfillmentMethod(FulfillmentMethodEnum.COLLECTION);
			final String addressId = addressData.getId();
			super.setCartDeliveryAddressInternal(addressId);
		}
		getTbsCheckoutFacade().addCollectionPointInfo(collectionPointData);
		return collectionPoint;
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/carts/{cartId}/collectordetails", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "saveCollectorDetails", value = "Updated the collector details in collection point address", notes = "Saving it")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public StatusWsDTO saveCollectorDetails(@RequestBody
			final AddressWsDTO address, @ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL")
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields) throws UnsupportedRequestException
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("saveCollectorDetails");
		}
		validate(address, "address", getCollectionAddressValidator());
		final StatusWsDTO statusWsDTO = new StatusWsDTO();
		final AddressData collectionPointDeliveryAddress = getTbsCheckoutFacade().getCollectionDeliveryAddress(FulfillmentMethodEnum.COLLECTION);
		if (null != collectionPointDeliveryAddress)
		{
			updateCollectionAddressInternal(collectionPointDeliveryAddress, address.getFirstName(), address.getLastName(), address.getCellphone());
			statusWsDTO.setCode("success");
			return statusWsDTO;
		}
		statusWsDTO.setCode("error.collectionpoint.not.found");
		return statusWsDTO;
	}

	private CollectionPointResponseDTO searchCollectionPoints(final String locationQuery, final GeoPoint geoPoint)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("Finding stores for search string: %s", locationQuery));
		}
		final CollectionPointResponseData searchResult =
				geoPoint == null ? collectionPointSearchFacade.locationSearch(locationQuery) :
					collectionPointSearchFacade.locationSearch(geoPoint.getLatitude(), geoPoint.getLongitude());
				return collectionPointDataJsonHelper.generateCollectionPointResponseData(searchResult);
	}

}
