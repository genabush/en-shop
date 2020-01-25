/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.filter;

import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * @author vasanthramprakasam
 */
public interface SearchFilterHandler
{
	void addFilterQuery(final SearchQuery searchQuery, final IndexedType indexedType);

	boolean canAddFilter(final SearchQuery searchQuery, final IndexedType indexedType);
}
