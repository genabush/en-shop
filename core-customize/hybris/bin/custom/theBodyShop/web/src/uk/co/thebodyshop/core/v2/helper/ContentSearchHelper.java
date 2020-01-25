/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.v2.helper;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import uk.co.thebodyshop.core.content.ContentSearchFacade;
import uk.co.thebodyshop.core.search.content.data.ContentPageData;
import uk.co.thebodyshop.core.search.content.ws.data.ContentSearchPageWsDTO;
import uk.co.thebodyshop.core.search.facetdata.ContentSearchPageData;
import uk.co.thebodyshop.core.util.ws.SearchQueryCodec;

/**
 * @author vasanthramprakasam
 */
@Component
public class ContentSearchHelper extends AbstractHelper
{
    @Resource(name = "contentSearchFacade")
    private ContentSearchFacade<ContentPageData> contentSearchFacade;
    @Resource(name = "cwsSearchQueryCodec")
    private SearchQueryCodec<SolrSearchQueryData> searchQueryCodec;
    @Resource(name = "solrSearchStateConverter")
    private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;

    public ContentSearchPageWsDTO searchContent(final String query, final int currentPage, final int pageSize, final String sort,
                                                final String fields)
    {
        final ContentSearchPageData<SearchStateData, ContentPageData> sourceResult = searchContent(query, currentPage, pageSize, sort);
        return getDataMapper().map(sourceResult, ContentSearchPageWsDTO.class, fields);
    }

    public ContentSearchPageData<SearchStateData, ContentPageData> searchContent(final String query, final int currentPage,
                                                                                 final int pageSize, final String sort)
    {
        final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
        final PageableData pageable = createPageableData(currentPage, pageSize, sort);

        return contentSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
    }

    public ContentSearchPageWsDTO searchContent(final String query, final int currentPage, final int pageSize, final String sort,
                                                final String fields, final String searchQueryContext)
    {
        final SearchQueryContext context = decodeContext(searchQueryContext);

        final ContentSearchPageData<SearchStateData, ContentPageData> sourceResult = searchContent(query, currentPage, pageSize, sort,
                context);
        return getDataMapper().map(sourceResult, ContentSearchPageWsDTO.class, fields);
    }

    protected ContentSearchPageData<SearchStateData, ContentPageData> searchContent(final String query, final int currentPage,
                                                                                    final int pageSize, final String sort, final SearchQueryContext searchQueryContext)
    {
        final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
        searchQueryData.setSearchQueryContext(searchQueryContext);

        final PageableData pageable = createPageableData(currentPage, pageSize, sort);

        return contentSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
    }

    protected SearchQueryContext decodeContext(final String searchQueryContext)
    {
        if (StringUtils.isBlank(searchQueryContext))
        {
            return null;
        }

        try
        {
            return SearchQueryContext.valueOf(searchQueryContext);
        }
        catch (final IllegalArgumentException e)
        {
            throw new RequestParameterException(searchQueryContext + " context does not exist", RequestParameterException.INVALID,
                    e);
        }
    }
}
