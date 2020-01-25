/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.storefinder;

import de.hybris.platform.basecommerce.enums.DistanceUnit;
import de.hybris.platform.commerceservices.storefinder.helpers.DistanceHelper;

/**
 * @author Jagadeesh
 */
public interface TbsDistanceHelper extends DistanceHelper
{
	/**
	 * Gets the distance double for given distance value and the distance unit that is set in the base store model. In no distance unit is set in the store model default 'km' is assumed.
	 *
	 * @param distanceUnit
	 *          the distance unit that provided in base store model.
	 * @param distanceInKm
	 *          the distance in km
	 * @return the calculated distance string
	 */
	public Double getDistanceByUnit(final DistanceUnit distanceUnit, final double distanceInKm);
}
