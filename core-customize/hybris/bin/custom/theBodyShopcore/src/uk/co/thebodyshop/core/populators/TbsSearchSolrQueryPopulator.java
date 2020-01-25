/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.populators;

import java.util.Collection;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchSolrQueryPopulator;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import uk.co.thebodyshop.core.services.TbsCategoryService;

/**
 * @author Lakshmi
 **/
public class TbsSearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> extends SearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
{
	private final TbsCategoryService tbsCategoryService;
	private static final String INDEXEDTYPE_BASE_PRODUCT = "TbsBaseProduct";
	private static final String INDEXEDTYPE_VARIANT_PRODUCT = "Product";

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source, final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		super.populate(source, target);
		// We can only search one core so select the indexed type
		target.setIndexedType(getIndexedType(target.getFacetSearchConfig(), target.getSearchQueryData()));

		// Create the solr search query for the config and type (this sets-up the default page size and sort order)
		SearchQuery searchQuery;

		if (target.getFacetSearchConfig().getSearchConfig().isLegacyMode())
		{
			searchQuery = createSearchQueryForLegacyMode(target.getFacetSearchConfig(), target.getIndexedType(), source.getSearchQueryData().getSearchQueryContext(), source.getSearchQueryData().getFreeTextSearch());
		}
		else
		{
			searchQuery = createSearchQuery(target.getFacetSearchConfig(), target.getIndexedType(), source.getSearchQueryData().getSearchQueryContext(), source.getSearchQueryData().getFreeTextSearch());
		}

		searchQuery.setCatalogVersions(target.getCatalogVersions());
		searchQuery.setCurrency(getCommonI18NService().getCurrentCurrency().getIsocode());
		searchQuery.setLanguage(getCommonI18NService().getCurrentLanguage().getIsocode());

		// enable spell checker
		searchQuery.setEnableSpellcheck(true);

		target.setSearchQuery(searchQuery);
	}

	protected IndexedType getIndexedType(final FacetSearchConfig config, final SolrSearchQueryData searchQueryData)
	{
		final CategoryModel categoryModel = getTbsCategoryService().getCategoryForSolrQuery(searchQueryData);
		final IndexConfig indexConfig = config.getIndexConfig();

		// Strategy for working out which of the available indexed types to use
		final Collection<IndexedType> indexedTypes = indexConfig.getIndexedTypes().values();

		IndexedType baseProductIndex = null;
		if (indexedTypes != null && !indexedTypes.isEmpty())
		{
			for (final IndexedType indexedType : indexedTypes)
			{
				if (indexedType.getCode().contains(INDEXEDTYPE_BASE_PRODUCT))
				{
					baseProductIndex = indexedType;
				}
				if (Objects.isNull(categoryModel) && Objects.nonNull(baseProductIndex))
				{
					return baseProductIndex;
				}
				else if (Objects.nonNull(categoryModel) && !categoryModel.isVariantCategory() && Objects.nonNull(baseProductIndex))
				{
					return baseProductIndex;
				}
				else if (Objects.nonNull(categoryModel) && categoryModel.isVariantCategory() && indexedType.getCode().contains(INDEXEDTYPE_VARIANT_PRODUCT))
				{
					return indexedType;
				}
			}
		}
		// Base Product indexed type returned by default
		return baseProductIndex;
	}

	@Autowired
	public TbsSearchSolrQueryPopulator(final TbsCategoryService tbsCategoryService)
	{
		this.tbsCategoryService = tbsCategoryService;
	}

	protected TbsCategoryService getTbsCategoryService()
	{
		return tbsCategoryService;
	}
}
