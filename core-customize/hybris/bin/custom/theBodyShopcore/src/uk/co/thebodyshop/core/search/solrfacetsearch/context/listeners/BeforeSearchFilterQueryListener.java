/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.context.listeners;

import java.util.List;

import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchListener;

import uk.co.thebodyshop.core.search.solrfacetsearch.filter.SearchFilterHandler;


/**
 * @author vasanthramprakasam
 */
public class BeforeSearchFilterQueryListener implements FacetSearchListener
{
	private final List<SearchFilterHandler> searchFilterHandlers;

	public BeforeSearchFilterQueryListener(
			List<SearchFilterHandler> searchFilterHandlers)
	{
		this.searchFilterHandlers = searchFilterHandlers;
	}

	@Override
	public void beforeSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
	{
		final SearchQuery searchQuery = facetSearchContext.getSearchQuery();
		final IndexedType indexedType = facetSearchContext.getIndexedType();
		getSearchFilterHandlers().stream().filter(handler -> handler.canAddFilter(searchQuery,indexedType)).forEach(handler -> handler.addFilterQuery(searchQuery,indexedType));
	}

	@Override
	public void afterSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
	{
		//Nothing here
	}

	@Override
	public void afterSearchError(FacetSearchContext facetSearchContext) throws FacetSearchException
	{
		//Nothing here
	}

	protected List<SearchFilterHandler> getSearchFilterHandlers()
	{
		return searchFilterHandlers;
	}
}
