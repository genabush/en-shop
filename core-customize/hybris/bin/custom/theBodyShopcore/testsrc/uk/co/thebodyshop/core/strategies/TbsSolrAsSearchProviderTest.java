/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants.CATEGORY_FIELD_SEARCH;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import uk.co.thebodyshop.core.strategies.impl.TbsSolrAsSearchProvider;


/**
 * @author vasanthramprakasam
 */
public class TbsSolrAsSearchProviderTest
{
	public static final String CATEGORY_CODE = "code";

	private TbsSolrAsSearchProvider solrAsSearchProvider;
	@Mock
	private AsSearchProfileContext context;
	@Mock
	private AsSearchQueryData asSearchQueryData;
	@Mock
	private FacetSearchConfig facetSearchConfig;
	@Mock
	private IndexedType indexedType;
	@Mock
	private FacetSearchService facetSearchService;
	@Mock
	private FacetSearchConfigService facetSearchConfigService;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private SearchQuery searchQuery;
	@Mock
	private IndexConfig indexConfig;
	@Mock
	private CategoryModel categoryModel;
	@Mock
	private SearchResult result;

	@Before
	public void setUp() throws FacetConfigServiceException, FacetSearchException
	{
		MockitoAnnotations.initMocks(this);
		solrAsSearchProvider = new TbsSolrAsSearchProvider();
		solrAsSearchProvider.setFacetSearchConfigService(facetSearchConfigService);
		solrAsSearchProvider.setCommonI18NService(commonI18NService);
		solrAsSearchProvider.setFacetSearchService(facetSearchService);
		when(context.getIndexConfiguration()).thenReturn("indexConfigCode");
		when(context.getIndexType()).thenReturn("indexedType");
		when(facetSearchConfigService.getConfiguration(context.getIndexConfiguration())).thenReturn(facetSearchConfig);
		when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		Map<String,IndexedType> indexedTypeMap = new HashMap<>();
		indexedTypeMap.put("indexedType",indexedType);
		when(indexedType.getIdentifier()).thenReturn("indexedType");
		when(indexConfig.getIndexedTypes()).thenReturn(indexedTypeMap);
		when(facetSearchService.createFreeTextSearchQueryFromTemplate(any(), any(), anyString(), anyString())).thenReturn(searchQuery);
		when(context.getCategoryPath()).thenReturn(Collections.singletonList(categoryModel));
		when(facetSearchService.search(searchQuery)).thenReturn(result);
		when(categoryModel.getCode()).thenReturn(CATEGORY_CODE);
	}

	@Test
	public void testSearchUsesCategoryFieldForFilter() throws AsException
	{
		solrAsSearchProvider.search(context,asSearchQueryData);
		verify(searchQuery,times(1)).addFilterQuery(CATEGORY_FIELD_SEARCH, CATEGORY_CODE);
	}
}
