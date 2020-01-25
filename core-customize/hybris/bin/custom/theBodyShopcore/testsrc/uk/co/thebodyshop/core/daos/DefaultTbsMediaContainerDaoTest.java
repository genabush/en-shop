/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.thebodyshop.core.daos.impl.DefaultTbsMediaContainerDao;

import java.util.List;

import static org.mockito.Mockito.*;

@UnitTest
public class DefaultTbsMediaContainerDaoTest {

    private static final String TEST_QUALIFIER = "testQualifier";

    @Mock
    private FlexibleSearchService flexibleSearchService;

    @Mock
    private CatalogVersionModel catalogVersion;

    @Mock
    private SearchResult searchResult;

    @Mock
    private List resultList;

    @InjectMocks
    private DefaultTbsMediaContainerDao defaultTbsMediaContainerDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultTbsMediaContainerDao.setFlexibleSearchService(flexibleSearchService);
    }

    @Test
    public void shouldInvokeFlexibleSearchServiceCorrectly() {
        String queryString = "SELECT {" + MediaContainerModel.PK + "} " +
                "FROM {" + MediaContainerModel._TYPECODE + "} " +
                "WHERE {" + MediaContainerModel.QUALIFIER + "} = ?qualifier " +
                "AND {" + MediaContainerModel.CATALOGVERSION  +"} = ?catalogVersion";
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("qualifier", TEST_QUALIFIER);
        query.addQueryParameter("catalogVersion", catalogVersion);
        when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
        when(searchResult.getResult()).thenReturn(resultList);
        defaultTbsMediaContainerDao.findMediaContainers(TEST_QUALIFIER, catalogVersion);
        verify(flexibleSearchService, times(1)).search(query);
    }

}
