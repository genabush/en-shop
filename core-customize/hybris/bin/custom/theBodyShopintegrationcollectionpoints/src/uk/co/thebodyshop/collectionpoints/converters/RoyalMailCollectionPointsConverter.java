/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.converters;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import uk.co.thebodyshop.collectionpoints.response.data.CollectionPointResponseData;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.getlcdeliverylocationsresponse.GetLCDeliveryLocationsResponse;

import javax.annotation.Resource;

/**
 * Created by alexjollands on 22/09/2016.
 */
public class RoyalMailCollectionPointsConverter implements Converter<GetLCDeliveryLocationsResponse, CollectionPointResponseData>
{

    @Resource(name = "royalMailCollectionPointsPopulator")
    private Populator<GetLCDeliveryLocationsResponse, CollectionPointResponseData> populator;

    @Override
    public CollectionPointResponseData convert(GetLCDeliveryLocationsResponse lcDeliveryLocationsResponse) throws ConversionException
    {
        final CollectionPointResponseData collectionPointResponseData = new CollectionPointResponseData();
        return convert(lcDeliveryLocationsResponse, collectionPointResponseData);
    }

    @Override
    public CollectionPointResponseData convert(GetLCDeliveryLocationsResponse lcDeliveryLocationsResponse, CollectionPointResponseData responseData) throws ConversionException
    {
        populator.populate(lcDeliveryLocationsResponse, responseData);
        return responseData;
    }
}
