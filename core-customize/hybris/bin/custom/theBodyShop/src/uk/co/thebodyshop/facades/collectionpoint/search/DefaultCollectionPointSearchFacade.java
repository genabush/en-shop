/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.facades.collectionpoint.search;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;

import uk.co.thebodyshop.collectionpoints.response.data.CollectionPointResponseData;
import uk.co.thebodyshop.collectionpoints.royalmail.data.CollectionPointRequestData;
import uk.co.thebodyshop.collectionpoints.services.CollectionPointService;
import uk.co.thebodyshop.collectionpoints.services.impl.DefaultRoyalMailCollectionPointService;
import uk.co.thebodyshop.core.enums.CollectionPointsEnum;

/**
 * @author Lumi
 */
public class DefaultCollectionPointSearchFacade implements CollectionPointSearchFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultCollectionPointSearchFacade.class);

	@Resource(name = "geoServiceWrapper")
	private GeoWebServiceWrapper geoWebServiceWrapper;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	private Map<String, String> collectionPoint2ServiceMappings;

	@Resource(name = "collectionServiceName2ServiceMappingsMap")
	private Map<String, CollectionPointService> collectionServiceName2ServiceMappingsMap;

	@Override
	public CollectionPointResponseData locationSearch(final String locationText)
	{

		if (StringUtils.isNotBlank(locationText))
		{
			try
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(String.format("Search for %s and showing only %s results.", locationText, getNumberOfResultsForCollectionPoints()));
				}

				return locationSearch(locationText, getNumberOfResultsForCollectionPoints());
			}
			catch (final GeoServiceWrapperException ex)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("DefaultCollectionPointSearchFacade.locationSearch throws GeoServiceWrapperException : ", ex);
				}
				ex.printStackTrace();
				LOG.info("Failed to resolve location for [" + locationText + "]");
			}
		}
		return null;
	}

	@Override
	public CollectionPointResponseData locationSearch(final String locationId, final double latitude, final double longitude)
	{
		return locationSearchWithCoordinates(null, locationId, latitude, longitude, 1);
	}

	@Override
	public boolean isValidSearchQuery(final String searchText)
	{
		final CollectionPointService<CollectionPointRequestData, CollectionPointResponseData> collectionPointService = getCollectionPointService();
		if (collectionPointService != null)
		{
			return collectionPointService.isValidSearchQuery(searchText);
		}
		return false;
	}

	@Override
	public CollectionPointResponseData locationSearch(final double latitude, final double longitude)
	{
		return locationSearchWithCoordinates(null, null, latitude, longitude, getNumberOfResultsForCollectionPoints());
	}

	private CollectionPointResponseData locationSearch(final String searchText, final Integer numberOfResults) throws GeoServiceWrapperException
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final CollectionPointRequestData request = new CollectionPointRequestData();
		request.setCountryCode(currentBaseStore.getDeliveryCountries().iterator().next().getIsocode());
		request.setLanguage(currentBaseStore.getDefaultLanguage().getIsocode());
		request.setZip(searchText);
		request.setNumberOfResults(numberOfResults == null ? 0 : numberOfResults.intValue());
		final CollectionPointService<CollectionPointRequestData, CollectionPointResponseData> collectionPointService = getCollectionPointService();
		CollectionPointResponseData results = null;
		if (collectionPointService != null)
		{
			final boolean isCoordinateSearch = useLatitudeAndLongitude(collectionPointService);
			GPS searchLocation = null;
			if (isCoordinateSearch)
			{
				searchLocation = resolveCoordinatesForRequest(currentBaseStore, searchText);
				request.setLatitude(searchLocation.getDecimalLatitude());
				request.setLongitude(searchLocation.getDecimalLongitude());
			}
			results = collectionPointService.findCollectionPoints(request);
			if (isCoordinateSearch && null != results)
			{
				results.setSourceLatitude(searchLocation.getDecimalLatitude());
				results.setSourceLongitude(searchLocation.getDecimalLongitude());
			}
		}
		return results;
	}

	private CollectionPointResponseData locationSearchWithCoordinates(final String searchText, final String locationId, final double latitude, final double longitude, final Integer numberOfResults)
	{
		LOG.debug("Searching with Coordinates searchText" + searchText + " LocationId:" + locationId + " latitude:" + latitude + " longitude" + longitude + " NoofResults" + numberOfResults);
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final CollectionPointRequestData request = new CollectionPointRequestData();
		request.setLatitude(latitude);
		request.setLongitude(longitude);
		request.setCountryCode(currentBaseStore.getDeliveryCountries().iterator().next().getIsocode());
		request.setLanguage(currentBaseStore.getDefaultLanguage().getIsocode());
		request.setLocationId(locationId);
		request.setZip(searchText);
		request.setNumberOfResults(numberOfResults == null ? 0 : numberOfResults.intValue());
		final CollectionPointService<CollectionPointRequestData, CollectionPointResponseData> collectionPointService = getCollectionPointService();
		CollectionPointResponseData results = null;
		if (collectionPointService != null)
		{
			results = collectionPointService.findCollectionPoints(request);
			results.setSourceLatitude(latitude);
			results.setSourceLongitude(longitude);
		}
		return results;
	}

	private GPS resolveCoordinatesForRequest(final BaseStoreModel currentBaseStore, final String searchText) throws GeoServiceWrapperException
	{
		return geoWebServiceWrapper.geocodeAddress(generateGeoAddressForSearchQuery(currentBaseStore, searchText));
	}

	private boolean useLatitudeAndLongitude(final CollectionPointService<CollectionPointRequestData, CollectionPointResponseData> collectionPointService)
	{
		if (collectionPointService instanceof DefaultRoyalMailCollectionPointService)
		{
			return true;
		}
		return false;
	}

	protected AddressData generateGeoAddressForSearchQuery(final BaseStoreModel baseStore, final String locationText)
	{
		final AddressData addressData = new AddressData();
		addressData.setZip(locationText);
		addressData.setCountryCode(baseStore.getDeliveryCountries().iterator().next().getIsocode());
		LOG.debug("Address Search Query" + addressData.getZip() + "Country" + addressData.getCountryCode());
		return addressData;
	}

	private Integer getNumberOfResultsForCollectionPoints()
	{
		return siteConfigService.getInt("collection.point.display.result.count", 20);
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	private CollectionPointService<CollectionPointRequestData, CollectionPointResponseData> getCollectionPointService()
	{
		CollectionPointService<CollectionPointRequestData, CollectionPointResponseData> service = null;
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		final CollectionPointsEnum collectionPoint = baseStore.getCollectionPoint();
		final String serviceName = getCollectionPoint2ServiceMappings().get(null != collectionPoint ? collectionPoint.getCode() : "RoyalMail");

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Service Found :" + serviceName + " For store " + baseStore);
		}

		if (StringUtils.isNotEmpty(serviceName))
		{
			service = collectionServiceName2ServiceMappingsMap.get(serviceName);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Service Class : " + service.getClass());
				if (service == null)
				{
					LOG.debug("Unable to fing service for Service Class : " + service.getClass());
				}
			}
		}
		return service;
	}

	protected Map<String, String> getCollectionPoint2ServiceMappings()
	{
		return collectionPoint2ServiceMappings;
	}

	public void setCollectionPoint2ServiceMappings(final Map<String, String> collectionPoint2ServiceMappings)
	{
		this.collectionPoint2ServiceMappings = collectionPoint2ServiceMappings;
	}

}
