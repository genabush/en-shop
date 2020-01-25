/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies.impl;

import static uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants.CATEGORY_FIELD_SEARCH;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.adaptivesearchsolr.strategies.impl.SolrAsSearchProvider;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * @author vasanthramprakasam
 * Extend the OOB class to set filter for the category code at the solr property 'category'
 * instead of 'allCategories'
 */
public class TbsSolrAsSearchProvider extends SolrAsSearchProvider
{
	protected SearchQuery convertSearchQuery(AsSearchProfileContext context, AsSearchQueryData searchQuery) throws FacetConfigServiceException,
			AsException
	{
		FacetSearchConfig facetSearchConfig = getFacetSearchConfigService().getConfiguration(context.getIndexConfiguration());
		IndexedType indexedType = this.resolveIndexedType(facetSearchConfig, context.getIndexType());
		SearchQuery query = getFacetSearchService().createFreeTextSearchQueryFromTemplate(facetSearchConfig, indexedType, "DEFAULT", searchQuery.getQuery());
		query.setOffset(searchQuery.getActivePage());
		query.setPageSize(searchQuery.getPageSize());
		if (CollectionUtils.isNotEmpty(context.getCatalogVersions())) {
			query.setCatalogVersions(context.getCatalogVersions());
		}

		LanguageModel language = context.getLanguage();
		if (language != null) {
			query.setLanguage(language.getIsocode());
		}

		CurrencyModel currency = context.getCurrency();
		if (currency != null) {
			query.setCurrency(currency.getIsocode());
		}

		query.getHighlightingFields().clear();
		query.addHighlightingField("*");
		if (MapUtils.isNotEmpty(searchQuery.getFacetValues())) {
			Iterator var9 = searchQuery.getFacetValues().entrySet().iterator();

			while(var9.hasNext()) {
				Map.Entry<String, Set<String>> entry = (Map.Entry)var9.next();
				String indexProperty = entry.getKey();
				Set<String> facetValues = entry.getValue();
				if (StringUtils.isNotBlank(indexProperty) && CollectionUtils.isNotEmpty(facetValues)) {
					query.addFacetValue(indexProperty, facetValues);
				}
			}
		}

		CategoryModel selectedCategory = this.resolveSelectedCategory(context.getCategoryPath());
		if (selectedCategory != null) {
			query.addFilterQuery(CATEGORY_FIELD_SEARCH, new String[]{selectedCategory.getCode()});
		}

		query.setNamedSort(searchQuery.getSort());
		return query;
	}
}
