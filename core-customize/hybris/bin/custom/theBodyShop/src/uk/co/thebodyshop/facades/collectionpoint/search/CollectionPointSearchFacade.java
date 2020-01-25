/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.facades.collectionpoint.search;

import uk.co.thebodyshop.collectionpoints.response.data.CollectionPointResponseData;

/**
 * @author Lumi
 */
public interface CollectionPointSearchFacade
{
	CollectionPointResponseData locationSearch(final String locationText);

	CollectionPointResponseData locationSearch(final String locationId, final double latitude, final double longitude);

	CollectionPointResponseData locationSearch(final double latitude, final double longitude);

	boolean isValidSearchQuery(String searchText);
}
