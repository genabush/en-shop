/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.content.impl;

import org.springframework.util.Assert;

import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import uk.co.thebodyshop.core.content.ContentSearchFacade;
import uk.co.thebodyshop.core.search.content.data.ContentPageData;
import uk.co.thebodyshop.core.search.facetdata.ContentSearchPageData;
import uk.co.thebodyshop.core.services.TbsContentSearchService;


/**
 * @author vasanthramprakasam
 */
public class DefaultContentSearchFacade<ITEM extends ContentPageData> implements ContentSearchFacade<ITEM>
{
    private TbsContentSearchService<SolrSearchQueryData, SearchResultValueData, ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>> contentSearchService;
    private Converter<ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>, ContentSearchPageData<SearchStateData, ITEM>> contentSearchPageConverter;
    private Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder;
    private ThreadContextService threadContextService;

    public DefaultContentSearchFacade(TbsContentSearchService<SolrSearchQueryData, SearchResultValueData, ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>> contentSearchService, Converter<ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>, ContentSearchPageData<SearchStateData, ITEM>> contentSearchPageConverter, Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder, ThreadContextService threadContextService)
    {
        this.contentSearchService = contentSearchService;
        this.contentSearchPageConverter = contentSearchPageConverter;
        this.searchQueryDecoder = searchQueryDecoder;
        this.threadContextService = threadContextService;
    }

    @Override
    public ContentSearchPageData<SearchStateData, ITEM> textSearch(final String text)
    {
        return getThreadContextService().executeInContext(
                new ThreadContextService.Executor<ContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
                {
                    @Override
                    public ContentSearchPageData<SearchStateData, ITEM> execute()
                    {
                        return getContentSearchPageConverter().convert(getContentSearchService().textSearch(text, null,
                                null));
                    }
                });
    }

    @Override
    public ContentSearchPageData<SearchStateData, ITEM> textSearch(final String text, final SearchQueryContext searchQueryContext)
    {
        return getThreadContextService().executeInContext(
                new ThreadContextService.Executor<ContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
                {
                    @Override
                    public ContentSearchPageData<SearchStateData, ITEM> execute()
                    {
                        return getContentSearchPageConverter()
                                .convert(getContentSearchService().textSearch(text, searchQueryContext, null));
                    }
                });
    }

    @Override
    public ContentSearchPageData<SearchStateData, ITEM> textSearch(final SearchStateData searchState,
                                                                   final PageableData pageableData)
    {
        Assert.notNull(searchState, "SearchStateData must not be null.");

        return getThreadContextService().executeInContext(
                new ThreadContextService.Executor<ContentSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
                {
                    @Override
                    public ContentSearchPageData<SearchStateData, ITEM> execute()
                    {
                        return getContentSearchPageConverter()
                                .convert(getContentSearchService().searchAgain(decodeState(searchState), pageableData));
                    }
                });
    }

    protected SolrSearchQueryData decodeState(final SearchStateData searchState)
    {
        return getSearchQueryDecoder().convert(searchState.getQuery());
    }

    protected TbsContentSearchService<SolrSearchQueryData, SearchResultValueData, ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>> getContentSearchService()
    {
        return contentSearchService;
    }

    protected Converter<ContentSearchPageData<SolrSearchQueryData, SearchResultValueData>, ContentSearchPageData<SearchStateData, ITEM>> getContentSearchPageConverter()
    {
        return contentSearchPageConverter;
    }

    protected Converter<SearchQueryData, SolrSearchQueryData> getSearchQueryDecoder()
    {
        return searchQueryDecoder;
    }

    protected ThreadContextService getThreadContextService()
    {
        return threadContextService;
    }
}
