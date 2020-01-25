/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.v2.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.basecommerce.enums.DistanceUnit;
import de.hybris.platform.commercefacades.storelocator.data.OpeningScheduleData;
import de.hybris.platform.commercefacades.storelocator.data.TimeData;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.collectionpoints.response.data.Address;
import uk.co.thebodyshop.collectionpoints.response.data.CollectionPoint;
import uk.co.thebodyshop.collectionpoints.response.data.CollectionPointResponseData;
import uk.co.thebodyshop.collectionpoints.response.data.OperatingHours;
import uk.co.thebodyshop.core.collection.CollectionPointData;
import uk.co.thebodyshop.core.collection.CollectionPointResponseDTO;
import uk.co.thebodyshop.core.helper.CollectionDistanceHelper;

/**
 * @author prateek.goel
 */
public class CollectionPointDataJsonHelper
{

	private CommonI18NService commonI18NService;

	private CollectionDistanceHelper collectionDistanceHelper;

	private EnumerationService enumerationService;

	private BaseStoreService baseStoreService;

	private static final Map<String, String> weekDaySymbolsMapping;
	static
	{
		final Map<String, String> weekDayMapping = new HashMap<>();
		weekDayMapping.put("2", "Monday");
		weekDayMapping.put("3", "Tuesday");
		weekDayMapping.put("4", "Wednesday");
		weekDayMapping.put("5", "Thursday");
		weekDayMapping.put("6", "Friday");
		weekDayMapping.put("7", "Saturday");
		weekDayMapping.put("1", "Sunday");
		weekDaySymbolsMapping = Collections.unmodifiableMap(weekDayMapping);
	}

	public CollectionPointResponseDTO generateCollectionPointResponseData(final CollectionPointResponseData searchResult)
	{
		final CollectionPointResponseDTO result = new CollectionPointResponseDTO();
		final List<CollectionPointData> collectionPoints = new ArrayList<>();
		if (searchResult != null && CollectionUtils.isNotEmpty(searchResult.getCollectionPoints()))
		{
			final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
			final String serviceType = null != baseStore.getCollectionPoint() ? baseStore.getCollectionPoint().getCode() : null;
			for (final CollectionPoint collectionPoint : searchResult.getCollectionPoints())
			{
				collectionPoints.add(createCollectionPointData(collectionPoint, searchResult, serviceType));
			}
			result.setCollectionPoint(collectionPoints);
			result.setSourceLatitude(searchResult.getSourceLatitude());
			result.setSourceLongitude(searchResult.getSourceLongitude());
			result.setServiceType(serviceType);
			result.setNoresults(Boolean.FALSE);
		}
		else
		{
			result.setNoresults(Boolean.TRUE);
		}
		return result;
	}

	private CollectionPointData createCollectionPointData(final CollectionPoint collectionPoint, final CollectionPointResponseData searchResult, final String serviceType)
	{
		final CollectionPointData collectionPointData = new CollectionPointData();
		final AddressData addressData = new AddressData();
		final Address address = collectionPoint.getAddress();
		if (address != null)
		{
			addressData.setCompanyName(address.getConsigneeName());
			addressData.setLine1(address.getAddressLine());
			addressData.setLine2(address.getAddressLine1());
			addressData.setTown(address.getCity());
			addressData.setPostalCode(address.getPostcode());
			addressData.setPhone(collectionPoint.getPhoneNumber());
			final CountryData country = new CountryData();
			country.setIsocode(address.getCountryCode());
			addressData.setCountry(country);
			collectionPointData.setAddress(addressData);
		}

		final List<OperatingHours> openingDays = collectionPoint.getOperatingHours();
		if (CollectionUtils.isNotEmpty(openingDays))
		{
			openingDays.sort(Comparator.comparing(OperatingHours::getDay));
			final List<WeekdayOpeningDayData> openingDayList = new ArrayList<>();
			for (final OperatingHours openingHours : openingDays)
			{
				final WeekdayOpeningDayData openingDay = new WeekdayOpeningDayData();
				if (openingHours.getCloseHours() != null)
				{
					openingDay.setClosingTime(getTimeData(openingHours.getCloseHours()));
				}

				if (openingHours.getOpenHours() != null)
				{
					openingDay.setOpeningTime(getTimeData(openingHours.getOpenHours()));
				}

				openingDay.setWeekDay(getDay(openingHours.getDay()));
				openingDayList.add(openingDay);
			}
			final OpeningScheduleData openingSchedule = new OpeningScheduleData();
			openingSchedule.setWeekDayOpeningList(openingDayList);
			collectionPointData.setOpeningHours(openingSchedule);
		}
		final DistanceUnit distanceUnit = getBaseStoreService().getCurrentBaseStore().getStorelocatorDistanceUnit();
		final String distanceString = getCollectionDistanceHelper().getDistanceStringForUnit(collectionPoint.getDistanceUnit(), collectionPoint.getDistance(), distanceUnit);
		collectionPointData.setDistance(Double.valueOf(distanceString));
		collectionPointData.setDistanceUnit(getEnumerationService().getEnumerationName(distanceUnit));
		final GeoPoint geoPoint = new GeoPoint();
		geoPoint.setLatitude(Double.valueOf(collectionPoint.getLatitude()).doubleValue());
		geoPoint.setLongitude(Double.valueOf(collectionPoint.getLongitude()).doubleValue());
		collectionPointData.setGeoPoint(geoPoint);
		collectionPointData.setSourceLatitude(searchResult.getSourceLatitude());
		collectionPointData.setSourceLongitude(searchResult.getSourceLongitude());
		collectionPointData.setServiceType(serviceType);
		return collectionPointData;
	}

	private TimeData getTimeData(final String hour)
	{
		final TimeData timeData = new TimeData();
		timeData.setFormattedHour(hour);
		return timeData;
	}

	private String getDay(final String day)
	{
		return weekDaySymbolsMapping.get(day);
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected CollectionDistanceHelper getCollectionDistanceHelper()
	{
		return collectionDistanceHelper;
	}

	public void setCollectionDistanceHelper(final CollectionDistanceHelper collectionDistanceHelper)
	{
		this.collectionDistanceHelper = collectionDistanceHelper;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

}

