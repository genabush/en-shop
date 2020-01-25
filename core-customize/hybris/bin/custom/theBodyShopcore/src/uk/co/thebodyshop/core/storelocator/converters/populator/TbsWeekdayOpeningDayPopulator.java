/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.storelocator.converters.populator;

import de.hybris.platform.commercefacades.storelocator.converters.populator.OpeningDayPopulator;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.storelocator.model.WeekdayOpeningDayModel;

import uk.co.thebodyshop.core.model.TbsWeekdayOpeningDayModel;

/**
 * @author vasanthramprakasam
 */
public class TbsWeekdayOpeningDayPopulator extends OpeningDayPopulator<WeekdayOpeningDayModel, WeekdayOpeningDayData>
{
	 @Override
	 public void populate(WeekdayOpeningDayModel source, WeekdayOpeningDayData target)
	 {
			if (source instanceof TbsWeekdayOpeningDayModel)
			{
				 TbsWeekdayOpeningDayModel tbsWeekdayOpeningDay = (TbsWeekdayOpeningDayModel)source;
				 target.setClosingTime(getTimeDataConverter().convert(tbsWeekdayOpeningDay.getStoreClosingTime()));
				 target.setOpeningTime(getTimeDataConverter().convert(tbsWeekdayOpeningDay.getStoreOpeningTime()));
			}
	 }
}
