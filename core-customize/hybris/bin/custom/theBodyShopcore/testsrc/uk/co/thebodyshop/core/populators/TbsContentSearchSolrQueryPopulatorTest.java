/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.populators;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SearchQueryTemplateNameResolver;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.store.services.BaseStoreService;


/**
 * @author vasanthramprakasam
 */
public class TbsContentSearchSolrQueryPopulatorTest
{
	private TbsContentSearchSolrQueryPopulator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> populator;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private FacetSearchService facetSearchService;
	@Mock
	private FacetSearchConfigService facetSearchConfigService;
	@Mock
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;
	@Mock
	private SearchQueryTemplateNameResolver searchQueryTemplateNameResolver;
	@Mock
	private SolrSearchQueryData solrSearchQueryData;
	@Mock
	private SearchQueryPageableData<SolrSearchQueryData> source;

	private SolrSearchRequest target;
	@Mock
	private SolrFacetSearchConfigModel solrFacetSearchConfigModel;
	@Mock
	private FacetSearchConfig facetSearchConfig;
	@Mock
	private SearchConfig searchConfig;
	@Mock
	private IndexConfig indexConfig;
	@Mock
	private IndexedType productIndex;
	@Mock
	private IndexedType contentIndex;
	@Mock
	private ComposedTypeModel productType;
	@Mock
	private ComposedTypeModel contentType;
	@Mock
	private CatalogVersionModel productCV;
	@Mock
	private CatalogVersionModel contentCV;
	@Mock
	private CatalogModel productCatalog;
	@Mock
	private ContentCatalogModel contentCatalog;
	@Mock
	private SearchQuery searchQuery;
	@Mock
	private CurrencyModel currentCurrency;
	@Mock
	private LanguageModel currentLanguage;
	@Before
	public void setUp() throws NoValidSolrConfigException,FacetConfigServiceException
	{
		MockitoAnnotations.initMocks(this);
		populator = new TbsContentSearchSolrQueryPopulator();
		populator.setBaseSiteService(baseSiteService);
		populator.setBaseStoreService(baseStoreService);
		populator.setCatalogVersionService(catalogVersionService);
		populator.setCommonI18NService(commonI18NService);
		populator.setFacetSearchConfigService(facetSearchConfigService);
		populator.setSearchQueryTemplateNameResolver(searchQueryTemplateNameResolver);
		populator.setSolrFacetSearchConfigSelectionStrategy(solrFacetSearchConfigSelectionStrategy);
		populator.setFacetSearchService(facetSearchService);
		when(solrFacetSearchConfigSelectionStrategy.getCurrentSolrFacetSearchConfig()).thenReturn(solrFacetSearchConfigModel);
		when(solrFacetSearchConfigModel.getName()).thenReturn("facetConfigName");
		when(facetSearchConfigService.getConfiguration("facetConfigName")).thenReturn(facetSearchConfig);
		when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		when(facetSearchConfig.getSearchConfig()).thenReturn(searchConfig);
		when(searchConfig.isLegacyMode()).thenReturn(false);
		when(productIndex.getComposedType()).thenReturn(productType);
		when(productType.getCode()).thenReturn(ProductModel._TYPECODE);
		when(contentIndex.getComposedType()).thenReturn(contentType);
		when(contentType.getCode()).thenReturn(ContentPageModel._TYPECODE);
		when(productCV.getCatalog()).thenReturn(productCatalog);
		when(contentCV.getCatalog()).thenReturn(contentCatalog);
		when(catalogVersionService.getSessionCatalogVersions()).thenReturn(Arrays.asList(productCV,contentCV));
		when(source.getSearchQueryData()).thenReturn(solrSearchQueryData);
		when(searchQueryTemplateNameResolver.resolveTemplateName(any(),any(),any())).thenReturn("default");
		when(facetSearchService.createFreeTextSearchQueryFromTemplate(any(),any(),any(),any())).thenReturn(searchQuery);
		when(commonI18NService.getCurrentCurrency()).thenReturn(currentCurrency);
		when(commonI18NService.getCurrentLanguage()).thenReturn(currentLanguage);
	}

	@Test
	public void testPopulateUsesCorrectIndexType()
	{
		target = new SolrSearchRequest();
		Map<String,IndexedType> indexedTypeMap = new HashMap<>();
		indexedTypeMap.put("product",productIndex);
		indexedTypeMap.put("content",contentIndex);
		when(indexConfig.getIndexedTypes()).thenReturn(indexedTypeMap);
		populator.populate(source,target);
		assertEquals(target.getIndexedType(),contentIndex);
	}

	@Test(expected= ConversionException.class)
	public void testPopulateThrowsExceptionWhenNoContentIndexPresent()
	{
		target = new SolrSearchRequest();
		Map<String,IndexedType> indexedTypeMap = new HashMap<>();
		indexedTypeMap.put("product",productIndex);
		when(indexConfig.getIndexedTypes()).thenReturn(indexedTypeMap);
		populator.populate(source,target);
	}

	@Test
	public void testPopulateUsesCorrectCatalogVersions()
	{
		target = new SolrSearchRequest();
		Map<String,IndexedType> indexedTypeMap = new HashMap<>();
		indexedTypeMap.put("product",productIndex);
		indexedTypeMap.put("content",contentIndex);
		when(indexConfig.getIndexedTypes()).thenReturn(indexedTypeMap);
		populator.populate(source,target);
		assertEquals(target.getCatalogVersions(), Collections.singletonList(contentCV));
	}

}
