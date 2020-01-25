/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.converters.populator;

import de.hybris.platform.commercefacades.storelocator.data.TimeData;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.core.model.CollectionOpeningDayModel;

/**
 * @author prateek.goel
 */
public class CollectionOpeningDayPopulator implements Populator<CollectionOpeningDayModel, WeekdayOpeningDayData>
{

	@Override
	public void populate(final CollectionOpeningDayModel source, final WeekdayOpeningDayData target) throws ConversionException
	{
		target.setOpeningTime(getTimeData(source.getOpeningTime()));
		target.setClosingTime(getTimeData(source.getClosingTime()));
		target.setWeekDay(source.getDay());
	}

	private TimeData getTimeData(final String time)
	{
		final TimeData timeData = new TimeData();
		timeData.setFormattedHour(time);
		return timeData;
	}

}
