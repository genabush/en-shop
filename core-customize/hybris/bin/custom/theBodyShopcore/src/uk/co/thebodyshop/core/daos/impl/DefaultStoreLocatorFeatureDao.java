/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import de.hybris.platform.commerceservices.model.storelocator.StoreLocatorFeatureModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import uk.co.thebodyshop.core.daos.StoreLocatorFeatureDao;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hemchand
 */

public class DefaultStoreLocatorFeatureDao implements StoreLocatorFeatureDao
{
    private final FlexibleSearchService flexibleSearchService;
    private static final String FIND_STORE_LOCATOR_FEATURE_QUERY = "SELECT {slf:pk} from { " + StoreLocatorFeatureModel._TYPECODE + " as slf} WHERE {slf:code} = ?code ";

    @Override
    public StoreLocatorFeatureModel getStoreLocatorFeatureForCode(String code)
    {
        final Map<String, Object> attr = new HashMap<String, Object>(1);
        attr.put("code", code);
        final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_STORE_LOCATOR_FEATURE_QUERY);
        query.getQueryParameters().putAll(attr);
        final SearchResult<StoreLocatorFeatureModel> result = this.getFlexibleSearchService().search(query);
        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    public DefaultStoreLocatorFeatureDao(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }
}
