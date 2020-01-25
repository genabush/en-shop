package uk.co.thebodyshop.core.v2.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.commercefacades.storefinder.StoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.status.StatusWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;

import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.exceptions.NoCheckoutCartException;
import uk.co.thebodyshop.core.exceptions.UnsupportedRequestException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller for store locator search and detail pages.
 *
 * @author prateek.goel
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/collect-in-store")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
@Api(tags = "CollectInStore")
public class CollectInStoreController extends BaseCommerceController
{
	private static final Logger LOG = LoggerFactory.getLogger(CollectInStoreController.class);

	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@RequestMapping(value = "/carts/{cartId}/search", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "findStores", value = "Find the Stores as per current location or as per town or post code entered", notes = "Returns the store results data having information")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public StoreFinderSearchPageWsDTO findStores(@RequestParam(value = "q", required = false) final String locationQuery, @RequestParam(value = "lat", required = false) final Double latitude,
			@RequestParam(value = "long", required = false) final Double longitude, @RequestParam(value = "currentPage", required = false) final int currentPage,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws GeoLocatorException, MapServiceException
	{
		final GeoPoint geoPoint = ofGeoPoint(latitude, longitude);

		final String sanitizedSearchQuery = filter(locationQuery);

		final PageableData pageableData = createPageableData(currentPage);

		final StoreFinderSearchPageData<PointOfServiceData> storeFinderPageData = searchStores(sanitizedSearchQuery, geoPoint, pageableData);

		final List<PointOfServiceData> storeResults = storeFinderPageData.getResults();

		final List<PointOfServiceData> availablePos = getTbsCheckoutFacade().getConsolidatedPickupOptions(storeResults);

		updatePOSAvailability(availablePos, storeResults);

		return getDataMapper().map(storeFinderPageData, StoreFinderSearchPageWsDTO.class, fields);
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/carts/{cartId}/save", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Added the cart collection point address for the cart.", notes = "Creates an address and assigns it to the cart as the collectionPoint address.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public PointOfServiceWsDTO createCartStoreDetails(@ApiParam(value = "Request body parameter that contains details such as the customer's first name (firstName), the customer's last name (lastName), "
			+ "the country (country.isocode), the first part of the address (line1), the second part of the address (line2), the town (town), the postal code (postalCode).\n\nThe DTO is in XML or .json format.", required = true) @RequestBody final PointOfServiceWsDTO pointOfService,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws NoCheckoutCartException
	{
		final PointOfServiceData posData = getDataMapper().map(pointOfService, PointOfServiceData.class);

		AddressData addressData = posData.getAddress();

		final AddressData storeDeliveryAddress = getTbsCheckoutFacade().getCollectionDeliveryAddress(FulfillmentMethodEnum.COLLECTINSTORE);

		if (null != storeDeliveryAddress)
		{
			updateCollectionAddressInternal(storeDeliveryAddress, addressData);
		}
		else
		{
			addressData.setId(null);
			addressData = createCollectionAddressInternal(addressData);
			getTbsCheckoutFacade().updateFulfillmentMethod(FulfillmentMethodEnum.COLLECTINSTORE);
			setCartDeliveryAddressInternal(addressData.getId());
			getTbsCheckoutFacade().updateCisDeliveryMethod(getBaseStoreService().getCurrentBaseStore());
		}

		getTbsCheckoutFacade().setStoreDetails(posData);

		return pointOfService;
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/carts/{cartId}/collectordetails", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "saveCollectorDetails", value = "Updated the collector details of Store", notes = "Saving it")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public StatusWsDTO saveCollectorDetails(@RequestBody final AddressWsDTO address,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws UnsupportedRequestException
	{
		validate(address, "address", getCollectionAddressValidator());

		final StatusWsDTO statusWsDTO = new StatusWsDTO();

		final AddressData storeDeliveryAddress = getTbsCheckoutFacade().getCollectionDeliveryAddress(FulfillmentMethodEnum.COLLECTINSTORE);

		if (null != storeDeliveryAddress)
		{
			updateCollectionAddressInternal(storeDeliveryAddress, address.getFirstName(), address.getLastName(), address.getCellphone());
			statusWsDTO.setCode("success");
		}
		else
		{
			statusWsDTO.setCode("error.store.not.found");
		}

		return statusWsDTO;
	}

	private void updatePOSAvailability(final List<PointOfServiceData> availablePOS, final List<PointOfServiceData> searchedPOS)
	{
		if (CollectionUtils.isNotEmpty(availablePOS) && CollectionUtils.isNotEmpty(searchedPOS))
		{
			searchedPOS.forEach(pointOfService -> {
				pointOfService.setAvailable(availablePOS.stream().filter(pos -> StringUtils.equalsIgnoreCase(pointOfService.getName(), pos.getName()) && pointOfService.isAvailable()).findAny().isPresent());
			});
		}
		else if (CollectionUtils.isNotEmpty(searchedPOS) && CollectionUtils.isEmpty(availablePOS))
		{
			searchedPOS.forEach(pointOfService -> {
				pointOfService.setAvailable(false);
			});
		}
	}

	private PageableData createPageableData(final int currentPage)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(currentPage);
		pageableData.setPageSize(getNumberOfResultsForStores());
		return pageableData;
	}

	private StoreFinderSearchPageData<PointOfServiceData> searchStores(final String locationQuery, final GeoPoint geoPoint, final PageableData pageableData)
	{
		StoreFinderSearchPageData<PointOfServiceData> searchResult;

		if (geoPoint != null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Finding stores for given geo point [{}, {}]", geoPoint.getLatitude(), geoPoint.getLongitude());
			}
			final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
			searchResult = storeFinderFacade.positionSearch(geoPoint, pageableData, currentBaseStore.getMaxRadiusForPoSSearch());
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Finding stores for given location query [{}]", locationQuery);
			}

			searchResult = storeFinderFacade.locationSearch(locationQuery, pageableData);
		}

		searchResult.getResults().forEach((pos) -> {
			pos.setSourceLatitude(searchResult.getSourceLatitude());
			pos.setSourceLongitude(searchResult.getSourceLongitude());
		});

		return searchResult;
	}

	private Integer getNumberOfResultsForStores()
	{
		return siteConfigService.getInt("stores.display.result.count", 20);
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

}
