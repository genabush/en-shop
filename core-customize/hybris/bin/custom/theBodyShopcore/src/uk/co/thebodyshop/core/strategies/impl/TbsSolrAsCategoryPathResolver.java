/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.strategies.impl;

import static uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants.CATEGORY_FIELD_SEARCH;

import de.hybris.platform.adaptivesearchsolr.strategies.impl.DefaultSolrAsCategoryPathResolver;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * @author vasanthramprakasam
 * Extend the OOB class to look for the category code at the solr property 'category'
 * instead of 'allCategories'
 */
public class TbsSolrAsCategoryPathResolver extends DefaultSolrAsCategoryPathResolver
{

	@Override
	protected String resolveCategoryCode(SearchQuery searchQuery)
	{
		String categoryCode = this.resolveCategoryCodeFromFilterQueries(searchQuery);
		return StringUtils.isNotBlank(categoryCode) ? categoryCode : this.resolveCategoryCodeFromFacetValues(searchQuery);
	}

	@Override
	protected String resolveCategoryCodeFromFilterQueries(SearchQuery searchQuery)
	{
		List<QueryField> filterQueries = searchQuery.getFilterQueries();
		if (CollectionUtils.isNotEmpty(filterQueries)) {
			Optional<QueryField> fieldOptional = filterQueries.stream().filter((queryField) -> CATEGORY_FIELD_SEARCH.equals(queryField.getField())).findAny();
			if (fieldOptional.isPresent()) {
				Set<String> values = fieldOptional.get().getValues();
				return !CollectionUtils.isEmpty(values) && values.size() <= 1 ? values.iterator().next() : null;
			}
		}

		return null;
	}

	@Override
	protected String resolveCategoryCodeFromFacetValues(SearchQuery searchQuery)
	{
		List<FacetValueField> facetValues = searchQuery.getFacetValues();
		if (CollectionUtils.isNotEmpty(facetValues)) {
			Optional<FacetValueField> fieldOptional = facetValues.stream().filter((facetValueField) -> CATEGORY_FIELD_SEARCH.equals(facetValueField.getField())).findAny();
			if (fieldOptional.isPresent()) {
				Set<String> values = fieldOptional.get().getValues();
				return CollectionUtils.isNotEmpty(values) && values.size() <= 1 ? values.iterator().next() : null;
			}
		}

		return null;
	}
}
