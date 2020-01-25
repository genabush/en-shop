/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.storelocator.model.OpeningScheduleModel;
import uk.co.thebodyshop.core.daos.OpeningScheduleDao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hemchand
 */
public class DefaultOpeningScheduleDao implements OpeningScheduleDao
{
    private final FlexibleSearchService flexibleSearchService;
    private static final String FIND_OPENING_SCHEDULE_QUERY = "SELECT {os:pk} from { " + OpeningScheduleModel._TYPECODE + " as os} WHERE {os:code} = ?code ";

    @Override
    public OpeningScheduleModel getOpeningSchedule(String code)
    {
        final Map<String, Object> attr = new HashMap<String, Object>(1);
        attr.put("code", code);
        final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_OPENING_SCHEDULE_QUERY);
        query.getQueryParameters().putAll(attr);
        final SearchResult<OpeningScheduleModel> result = this.getFlexibleSearchService().search(query);
        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    public DefaultOpeningScheduleDao(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }

    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }
}
