/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants.CATEGORY_FIELD_SEARCH;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import uk.co.thebodyshop.core.strategies.impl.TbsSolrAsCategoryPathResolver;


/**
 * @author vasanthramprakasam
 */
@UnitTest
public class TbsSolrAsCategoryPathResolverTest
{
	public static final String CATEGORY_CODE = "c123";

	private TbsSolrAsCategoryPathResolver solrAsCategoryPathResolver;
	@Mock
	private CategoryService categoryService;
	@Mock
	private FacetSearchConfig facetSearchConfig;
	private IndexedType indexedType;
	private SearchQuery searchQuery;
	@Spy
	private CatalogVersionModel catalogVersionModel;
	@Spy
	private CategoryModel categoryModel;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		searchQuery = new SearchQuery(facetSearchConfig,indexedType);
		searchQuery.getFilterQueries().add(new QueryField(CATEGORY_FIELD_SEARCH, CATEGORY_CODE));
		when(categoryService.getCategoryForCode(CATEGORY_CODE)).thenReturn(categoryModel);
		when(categoryService.getCategoriesForCode(CATEGORY_CODE)).thenReturn(Collections.singletonList(categoryModel));
		when(catalogVersionModel.getActive()).thenReturn(true);
		solrAsCategoryPathResolver = new TbsSolrAsCategoryPathResolver();
		solrAsCategoryPathResolver.setCategoryService(categoryService);
	}

	@Test
	public void testResolveCategoryPath()
	{
		solrAsCategoryPathResolver.resolveCategoryPath(searchQuery, Collections.singletonList(catalogVersionModel));
		verify(categoryService,times(1)).getCategoriesForCode(CATEGORY_CODE);
	}

}
