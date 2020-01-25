/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.basecommerce.enums.DistanceUnit;
import de.hybris.platform.commercefacades.storelocator.helpers.impl.DefaultDistanceHelper;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

/**
 * @author prateek.goel
 */
public class CollectionDistanceHelper extends DefaultDistanceHelper
{
	private CommerceCommonI18NService commerceCommonI18NService;

	public static final double METRIC_DISTANCE_RATIO = 1.60934;

	public static String UNIT_KM = "KM";

	public static String UNIT_MI = "MI";

	public static String UNIT_MILES = "Miles";

	protected Double getDistanceInMiles(final double distanceInKm)
	{
		final Double distance = Double.valueOf(distanceInKm * IMPERIAL_DISTANCE_RATIO);

		return distance;
	}

	protected Double getDistanceInKm(final double distanceInMiles)
	{
		final Double distance = Double.valueOf(distanceInMiles * METRIC_DISTANCE_RATIO);

		return distance;
	}

	public double getDistanceForUnit(final DistanceUnit distanceUnit, final double distance, final DistanceUnit targetDistanceUnit)
	{
		double convertedDistance;
		if (distanceUnit.equals(targetDistanceUnit))
		{
			convertedDistance = distance;
		}
		else
		{
			if (DistanceUnit.KM.equals(distanceUnit))
			{
				convertedDistance = getDistanceInMiles(distance);
			}
			else
			{
				convertedDistance = getDistanceInKm(distance);
			}
		}
		return convertedDistance;
	}

	private DistanceUnit getDistanceUnit(final String distanceUnitString)
	{
		DistanceUnit distanceUnit = DistanceUnit.KM;
		if (StringUtils.equalsIgnoreCase(distanceUnitString, UNIT_KM))
		{
			distanceUnit = DistanceUnit.KM;
		}
		else if (StringUtils.equalsIgnoreCase(distanceUnitString, UNIT_MI) || StringUtils.equalsIgnoreCase(distanceUnitString, UNIT_MILES))
		{
			distanceUnit = DistanceUnit.MILES;
		}
		return distanceUnit;
	}

	public double getDistanceForUnit(final String distanceUnitString, final String distanceString, final DistanceUnit targetDistanceUnit)
	{
		final DistanceUnit distanceUnit = getDistanceUnit(distanceUnitString);
		final Double distance = Double.valueOf(distanceString);
		return getDistanceForUnit(distanceUnit, distance, targetDistanceUnit);
	}

	public String getDistanceStringForUnit(final String distanceUnitString, final String distanceString, final DistanceUnit targetDistanceUnit)
	{
		double distance = getDistanceForUnit(distanceUnitString, distanceString, targetDistanceUnit);
		final DecimalFormat roundedValue = new DecimalFormat("#.#");
		distance = Double.valueOf(roundedValue.format(distance));
		final NumberFormat numberFormat = NumberFormat.getInstance(getCommerceCommonI18NService().getCurrentLocale());
		numberFormat.setMaximumFractionDigits(1);
		return String.valueOf(distance);
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

}

