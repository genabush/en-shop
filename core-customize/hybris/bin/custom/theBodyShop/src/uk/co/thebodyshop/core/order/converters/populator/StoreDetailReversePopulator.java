/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.converters.populator;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.core.model.StoreDetailModel;

/**
 * @author prateek.goel
 */
public class StoreDetailReversePopulator implements Populator<PointOfServiceData, StoreDetailModel>
{

	@Override
	public void populate(final PointOfServiceData source, final StoreDetailModel target) throws ConversionException
	{
		final Double distance = source.getDistance();
		if (null != distance)
		{
			target.setDistance(distance.toString());
		}
		target.setSourceLatitude(source.getSourceLatitude());
		target.setSourceLongitude(source.getSourceLongitude());
	}

}
