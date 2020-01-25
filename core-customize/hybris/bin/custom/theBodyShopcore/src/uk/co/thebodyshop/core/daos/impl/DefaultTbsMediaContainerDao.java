/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.media.dao.impl.DefaultMediaContainerDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import uk.co.thebodyshop.core.daos.TbsMediaContainerDao;

import java.util.List;

public class DefaultTbsMediaContainerDao extends DefaultMediaContainerDao implements TbsMediaContainerDao {

    private static final String MEDIA_CONTAINERS_BY_QUALIFIER_AND_CATALOG_VERSION = "SELECT {" + MediaContainerModel.PK + "} " +
            "FROM {" + MediaContainerModel._TYPECODE + "} " +
            "WHERE {" + MediaContainerModel.QUALIFIER + "} = ?qualifier " +
            "AND {" + MediaContainerModel.CATALOGVERSION  +"} = ?catalogVersion";

    @Override
    public List<MediaContainerModel> findMediaContainers(String qualifier, CatalogVersionModel catalogVersion) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(MEDIA_CONTAINERS_BY_QUALIFIER_AND_CATALOG_VERSION);
        query.addQueryParameter("qualifier", qualifier);
        query.addQueryParameter("catalogVersion", catalogVersion);
        SearchResult<MediaContainerModel> searchResult = getFlexibleSearchService().search(query);
        return searchResult.getResult();
    }

}
