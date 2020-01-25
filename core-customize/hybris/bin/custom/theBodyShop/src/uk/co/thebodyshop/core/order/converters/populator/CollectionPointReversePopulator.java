/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.converters.populator;

import de.hybris.platform.commercefacades.storelocator.data.OpeningScheduleData;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import uk.co.thebodyshop.core.collection.CollectionPointData;
import uk.co.thebodyshop.core.model.CollectionOpeningDayModel;
import uk.co.thebodyshop.core.model.CollectionPointModel;

/**
 * @author prateek.goel
 */
public class CollectionPointReversePopulator implements Populator<CollectionPointData, CollectionPointModel>
{

	private Converter<WeekdayOpeningDayData, CollectionOpeningDayModel> collectionOpeningDayReverseConverter;

	@Override
	public void populate(final CollectionPointData collectionPointData, final CollectionPointModel collectionPointModel) throws ConversionException
	{
		final Double distance = collectionPointData.getDistance();
		if (null != distance)
		{
			collectionPointModel.setDistance(distance.toString());
		}
		final GeoPoint geoPoint = collectionPointData.getGeoPoint();
		if (null != geoPoint)
		{
			collectionPointModel.setLatitude(geoPoint.getLatitude());
			collectionPointModel.setLongitude(geoPoint.getLongitude());
		}
		collectionPointModel.setSourceLatitude(collectionPointData.getSourceLatitude());
		collectionPointModel.setSourceLongitude(collectionPointData.getSourceLongitude());
		final OpeningScheduleData openingScheduleData = collectionPointData.getOpeningHours();
		if(null != openingScheduleData)
		{
			collectionPointModel.setCollectionOpeningDays(collectionOpeningDayReverseConverter.convertAll(openingScheduleData.getWeekDayOpeningList()));
		}
	}

	public Converter<WeekdayOpeningDayData, CollectionOpeningDayModel> getCollectionOpeningDayReverseConverter()
	{
		return collectionOpeningDayReverseConverter;
	}

	public void setCollectionOpeningDayReverseConverter(final Converter<WeekdayOpeningDayData, CollectionOpeningDayModel> collectionOpeningDayReverseConverter)
	{
		this.collectionOpeningDayReverseConverter = collectionOpeningDayReverseConverter;
	}

}
