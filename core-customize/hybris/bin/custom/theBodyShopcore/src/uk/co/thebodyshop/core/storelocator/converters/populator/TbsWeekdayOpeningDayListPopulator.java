/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.storelocator.converters.populator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.enums.WeekDay;
import de.hybris.platform.commercefacades.storelocator.converters.populator.OpeningDayPopulator;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.WeekdayOpeningDayModel;

import uk.co.thebodyshop.core.model.TbsWeekdayOpeningDayModel;

/**
 * @author vasanthramprakasam
 */
public class TbsWeekdayOpeningDayListPopulator extends OpeningDayPopulator<List<WeekdayOpeningDayModel>, List<WeekdayOpeningDayData>>
{

	private Converter<WeekdayOpeningDayModel, WeekdayOpeningDayData> weekDayConverter;

	@Override
	public void populate(final List<WeekdayOpeningDayModel> source, final List<WeekdayOpeningDayData> target)
	{
		final List<TbsWeekdayOpeningDayModel> next7Days = source.stream().filter(Objects::nonNull).filter(TbsWeekdayOpeningDayModel.class::isInstance).map(TbsWeekdayOpeningDayModel.class::cast)
				.filter(openingDay -> openingDay.getStoreOpeningTime() != null).filter(openingDay -> openingDay.getStoreClosingTime() != null).sorted(Comparator.comparing(TbsWeekdayOpeningDayModel::getStoreOpeningTime)).collect(Collectors.toList());

		final Map<WeekDay, List<WeekdayOpeningDayData>> orderedWeekDaysMap = new TreeMap<>(new WeekDayComparator(getWeekFirstDay()));

		for (final WeekdayOpeningDayModel weekDay : next7Days)
		{
			final WeekdayOpeningDayData data = getWeekDayConverter().convert(weekDay);
			List<WeekdayOpeningDayData> weekdaysOpeningDayData = orderedWeekDaysMap.get(weekDay.getDayOfWeek());
			if (weekdaysOpeningDayData == null)
			{
				weekdaysOpeningDayData = new ArrayList<>();
				orderedWeekDaysMap.put(weekDay.getDayOfWeek(), weekdaysOpeningDayData);
			}
			weekdaysOpeningDayData.add(data);
		}

		for (final WeekDay singleEnum : WeekDay.values())
		{
			if (!orderedWeekDaysMap.containsKey(singleEnum))
			{
				orderedWeekDaysMap.put(singleEnum, Arrays.asList(populateClosedWeekDay(singleEnum)));
			}
		}

		target.addAll(orderedWeekDaysMap.values().stream().flatMap(days -> days.stream()).collect(Collectors.toList()));

	}

	protected static class WeekDayComparator implements Comparator<WeekDay>
	{
		private final WeekDay firstDayOfWeek;

		WeekDayComparator(final WeekDay first)
		{
			firstDayOfWeek = first;
		}

		@Override
		public int compare(final WeekDay one, final WeekDay two)
		{
			if (one == null && two == null)
			{
				return 0;
			}
			else
			{
				if (one != null && two != null)
				{
					int oneRelativeOrder = one.ordinal() - firstDayOfWeek.ordinal();
					if (oneRelativeOrder < 0)
					{
						oneRelativeOrder += WeekDay.values().length;
					}
					int twoRelativeOrder = two.ordinal() - firstDayOfWeek.ordinal();
					if (twoRelativeOrder < 0)
					{
						twoRelativeOrder += WeekDay.values().length;
					}
					return oneRelativeOrder - twoRelativeOrder;
				}
				else
				{
					if (one == null)
					{
						return -1;
					}
					else
					{
						return 1;
					}
				}
			}
		}
	}

	protected WeekdayOpeningDayData populateClosedWeekDay(final WeekDay singleEnum)
	{
		final WeekdayOpeningDayData data = new WeekdayOpeningDayData();
		data.setClosed(true);
		data.setWeekDay(getWeekDaySymbols().get(singleEnum.ordinal()));
		return data;
	}

	protected WeekDay getWeekFirstDay()
	{
		final Locale defaultLocale = getCurrentLocale();
		final Calendar calendar = Calendar.getInstance(defaultLocale);
		return WeekDay.values()[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public Converter<WeekdayOpeningDayModel, WeekdayOpeningDayData> getWeekDayConverter()
	{
		return weekDayConverter;
	}

	@Required
	public void setWeekDayConverter(final Converter<WeekdayOpeningDayModel, WeekdayOpeningDayData> weekDayConverter)
	{
		this.weekDayConverter = weekDayConverter;
	}
}
