/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Collections;
import java.util.Objects;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.*;
import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import uk.co.thebodyshop.core.search.facetdata.ContentSearchPageData;
import uk.co.thebodyshop.core.services.TbsContentSearchService;

/**
 * @author vasanthramprakasam
 */
public class DefaultContentSearchService<ITEM> implements TbsContentSearchService<SolrSearchQueryData, ITEM, ContentSearchPageData<SolrSearchQueryData, ITEM>>
{
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DefaultContentSearchService.class);

    private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter;
    private Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter;
    private Converter<SolrSearchResponse, ContentSearchPageData<SolrSearchQueryData, ITEM>> searchResponseConverter;

    public DefaultContentSearchService(Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter, Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter, Converter<SolrSearchResponse, ContentSearchPageData<SolrSearchQueryData, ITEM>> searchResponseConverter)
    {
        this.searchQueryPageableConverter = searchQueryPageableConverter;
        this.searchRequestConverter = searchRequestConverter;
        this.searchResponseConverter = searchResponseConverter;
    }

    @Override
    public ContentSearchPageData<SolrSearchQueryData, ITEM> textSearch(String text, PageableData pageableData)
    {
        final SolrSearchQueryData searchQueryData = createSearchQueryData();
        searchQueryData.setFreeTextSearch(text);
        searchQueryData.setFilterTerms(Collections.emptyList());

        return doSearch(searchQueryData, pageableData);
    }

    @Override
    public ContentSearchPageData<SolrSearchQueryData, ITEM> textSearch(String text, SearchQueryContext searchQueryContext, PageableData pageableData)
    {
        final SolrSearchQueryData searchQueryData = createSearchQueryData();
        searchQueryData.setFreeTextSearch(text);
        searchQueryData.setFilterTerms(Collections.emptyList());
        searchQueryData.setSearchQueryContext(searchQueryContext);

        return doSearch(searchQueryData, pageableData);
    }

    @Override
    public ContentSearchPageData<SolrSearchQueryData, ITEM> searchAgain(SolrSearchQueryData searchQueryData, PageableData pageableData)
    {
        return doSearch(searchQueryData, pageableData);
    }

    protected ContentSearchPageData<SolrSearchQueryData, ITEM> doSearch(
            final SolrSearchQueryData searchQueryData, final PageableData pageableData)
    {
        validateParameterNotNull(searchQueryData, "SearchQueryData cannot be null");

        // Create the SearchQueryPageableData that contains our parameters
        final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(searchQueryData,
                pageableData);

        // Build up the search request
        final SolrSearchRequest solrSearchRequest = getSearchQueryPageableConverter().convert(searchQueryPageableData);

        ContentSearchPageData<SolrSearchQueryData, ITEM> solrSearchResonseDate = null;
        if (Objects.nonNull(solrSearchRequest)) {
            // Execute the search
            final SolrSearchResponse solrSearchResponse = getSearchRequestConverter().convert(solrSearchRequest);
            if (Objects.nonNull(solrSearchResponse)) {
                // Convert the response
                solrSearchResonseDate = getSearchResponseConverter().convert(solrSearchResponse);
                return solrSearchResonseDate;
            }
        }
        return solrSearchResonseDate;
    }

    protected SearchQueryPageableData<SolrSearchQueryData> buildSearchQueryPageableData(final SolrSearchQueryData searchQueryData,
                                                                                        final PageableData pageableData)
    {
        final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = createSearchQueryPageableData();
        searchQueryPageableData.setSearchQueryData(searchQueryData);
        searchQueryPageableData.setPageableData(pageableData);
        return searchQueryPageableData;
    }

    protected SearchQueryPageableData<SolrSearchQueryData> createSearchQueryPageableData()
    {
        return new SearchQueryPageableData<>();
    }

    protected SolrSearchQueryData createSearchQueryData()
    {
        return new SolrSearchQueryData();
    }

    protected Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getSearchQueryPageableConverter()
    {
        return searchQueryPageableConverter;
    }

    protected Converter<SolrSearchRequest, SolrSearchResponse> getSearchRequestConverter()
    {
        return searchRequestConverter;
    }

    protected Converter<SolrSearchResponse, ContentSearchPageData<SolrSearchQueryData, ITEM>> getSearchResponseConverter()
    {
        return searchResponseConverter;
    }
}
