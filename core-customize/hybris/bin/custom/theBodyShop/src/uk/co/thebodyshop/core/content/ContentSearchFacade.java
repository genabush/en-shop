/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.content;

import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import uk.co.thebodyshop.core.search.facetdata.ContentSearchPageData;


/**
 * @author vasanthramprakasam
 */
public interface ContentSearchFacade<ITEM>
{
    /**
     * Initiate a new search using simple free text query.
     *
     * @param text the search text
     * @return the search results
     */
    ContentSearchPageData<SearchStateData, ITEM> textSearch(String text);

    /**
     * Initiate a new search using simple free text query in a search query context.
     *
     * @param text
     *           the search text
     * @param searchQueryContext
     *           search query context
     * @return the search results
     */
    ContentSearchPageData<SearchStateData, ITEM> textSearch(String text, SearchQueryContext searchQueryContext);

    /**
     * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchStateData
     * must have been obtained from the results of a call to {@link #textSearch(String)}.
     *
     * @param searchState  the search query object
     * @param pageableData the page to return
     * @return the search results
     */
    ContentSearchPageData<SearchStateData, ITEM> textSearch(SearchStateData searchState, PageableData pageableData);

}
