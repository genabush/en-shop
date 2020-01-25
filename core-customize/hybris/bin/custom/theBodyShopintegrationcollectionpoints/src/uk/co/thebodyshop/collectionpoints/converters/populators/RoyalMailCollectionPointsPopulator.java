/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import uk.co.thebodyshop.collectionpoints.response.data.CollectionPoint;
import uk.co.thebodyshop.collectionpoints.response.data.CollectionPointResponseData;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.getlcdeliverylocationsresponse.GetLCDeliveryLocationsResponse;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.Location;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.Locations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexjollands on 22/09/2016.
 */
public class RoyalMailCollectionPointsPopulator implements Populator<GetLCDeliveryLocationsResponse, CollectionPointResponseData>
{

    @Resource(name = "royalMailCollectionPointConverter")
    private Converter<Location, CollectionPoint> collectionPointConverter;

    @Override
    public void populate(GetLCDeliveryLocationsResponse lcdlResponse, CollectionPointResponseData responseData) throws ConversionException
    {
        if (lcdlResponse != null && lcdlResponse.getLocations() != null && responseData != null)
        {
            Locations locations = lcdlResponse.getLocations();
            List<CollectionPoint> collectionPoints = new ArrayList<>();
            for (Location location : locations.getLocation())
            {
                collectionPoints.add(collectionPointConverter.convert(location));
            }
            responseData.setCollectionPoints(collectionPoints);
        }
    }

}
