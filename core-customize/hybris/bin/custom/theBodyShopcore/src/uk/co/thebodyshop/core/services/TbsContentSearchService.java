/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import uk.co.thebodyshop.core.search.facetdata.ContentSearchPageData;

/**
 * @author vasanthramprakasam
 */
public interface TbsContentSearchService<STATE, ITEM, RESULT extends ContentSearchPageData<STATE, ITEM>>
{
    /**
     * Initiate a new search using simple free text query.
     *
     * @param text
     *           the search text
     * @param pageableData
     *           the page to return, can be null to use defaults
     * @return the search results
     */
    RESULT textSearch(String text, PageableData pageableData);

    /**
     * Initiate a new search using simple free text query in a search query context.
     *
     * @param text
     *           the search text
     * @param searchQueryContext
     *           search query context
     * @param pageableData
     *           the page to return, can be null to use defaults
     * @return the search results
     */
    RESULT textSearch(String text, SearchQueryContext searchQueryContext, PageableData pageableData);

    /**
     * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchQueryData
     * must have been obtained from the results of a call to either {@link #textSearch(String,PageableData)}
     *
     * @param searchQueryData
     *           the search query object
     * @param pageableData
     *           the page to return
     * @return the search results
     */
    RESULT searchAgain(STATE searchQueryData, PageableData pageableData);
}
