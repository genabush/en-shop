/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.royalmail.client;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import uk.co.thebodyshop.collectionpoints.royalmail.data.CollectionPointRequestData;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.getlcdeliverylocationsresponse.GetLCDeliveryLocationsResponse;

/**
 * Created by alexjollands on 21/09/2016.
 */
public interface RoyalMailCollectionPointClient
{
    public GetLCDeliveryLocationsResponse processRequest(final CollectionPointRequestData requestData) throws ClientProtocolException, IOException;
}
