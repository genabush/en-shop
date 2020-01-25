/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.services;

public interface CollectionPointService<Request, Response>
{

    Response findCollectionPoints(Request request);

    boolean isValidSearchQuery(String searchText);
}
