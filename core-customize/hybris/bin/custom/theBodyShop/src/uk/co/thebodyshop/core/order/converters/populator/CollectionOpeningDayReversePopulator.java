/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.converters.populator;

import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.core.model.CollectionOpeningDayModel;

/**
 * @author prateek.goel
 */
public class CollectionOpeningDayReversePopulator implements Populator<WeekdayOpeningDayData, CollectionOpeningDayModel>
{

	@Override
	public void populate(final WeekdayOpeningDayData openingDayData, final CollectionOpeningDayModel collectionOpeningDayModel) throws ConversionException
	{
		collectionOpeningDayModel.setClosingTime(openingDayData.getClosingTime().getFormattedHour());
		collectionOpeningDayModel.setDay(openingDayData.getWeekDay());
		collectionOpeningDayModel.setOpeningTime(openingDayData.getOpeningTime().getFormattedHour());

	}

}
