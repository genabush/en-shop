/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.stock.populator;

import de.hybris.platform.basecommerce.enums.DistanceUnit;
import de.hybris.platform.commercefacades.storefinder.converters.populator.PointOfServiceDistanceDataPopulator;
import de.hybris.platform.commercefacades.storelocator.data.OpeningScheduleData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.OpeningScheduleModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import uk.co.thebodyshop.core.helper.CollectionDistanceHelper;
import uk.co.thebodyshop.core.storelocator.pos.TbsPointOfServiceService;

/**
 * @author Jagadeesh
 */
public class TbsPointOfServiceDistanceDataPopulator extends PointOfServiceDistanceDataPopulator
{
	private final EnumerationService enumerationService;

	private final CollectionDistanceHelper collectionDistanceHelper;

	private final Converter<OpeningScheduleModel, OpeningScheduleData> openingScheduleConverter;

	private final TbsPointOfServiceService tbsPointOfServiceService;

	public TbsPointOfServiceDistanceDataPopulator(final EnumerationService enumerationService, final CollectionDistanceHelper collectionDistanceHelper, final Converter<OpeningScheduleModel, OpeningScheduleData> openingScheduleConverter,
			final TbsPointOfServiceService tbsPointOfServiceService)
	{
		this.enumerationService = enumerationService;
		this.collectionDistanceHelper = collectionDistanceHelper;
		this.openingScheduleConverter = openingScheduleConverter;
		this.tbsPointOfServiceService = tbsPointOfServiceService;
	}

	@Override
	public void populate(final PointOfServiceDistanceData source, final PointOfServiceData target) throws ConversionException
	{
		super.populate(source, target);
		final PointOfServiceModel pos = source.getPointOfService();
		if (null != pos)
		{
			final BaseStoreModel baseStore = pos.getBaseStore();
			final DistanceUnit distanceUnit = baseStore.getStorelocatorDistanceUnit();
			target.setDistanceUnit(enumerationService.getEnumerationName(distanceUnit));
			final String distanceString = getCollectionDistanceHelper().getDistanceStringForUnit("KM", String.valueOf(source.getDistanceKm()), distanceUnit);
			target.setDistance(Double.valueOf(distanceString));
			target.setAvailable(getTbsPointOfServiceService().isAvailableForCollectInStore(pos));
			if (null != pos.getOpeningSchedule())
			{
				target.setOpeningHours(getOpeningScheduleConverter().convert(pos.getOpeningSchedule()));
			}
		}
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public CollectionDistanceHelper getCollectionDistanceHelper()
	{
		return collectionDistanceHelper;
	}

	protected Converter<OpeningScheduleModel, OpeningScheduleData> getOpeningScheduleConverter()
	{
		return openingScheduleConverter;
	}

	protected TbsPointOfServiceService getTbsPointOfServiceService()
	{
		return tbsPointOfServiceService;
	}

}
