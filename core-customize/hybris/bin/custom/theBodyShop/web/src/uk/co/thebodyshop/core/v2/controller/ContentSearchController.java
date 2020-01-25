/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.v2.controller;

/**
 * @author vasanthramprakasam
 */

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.co.thebodyshop.core.search.content.data.ContentPageData;
import uk.co.thebodyshop.core.search.content.ws.data.ContentSearchPageWsDTO;
import uk.co.thebodyshop.core.search.facetdata.ContentSearchPageData;
import uk.co.thebodyshop.core.v2.helper.ContentSearchHelper;

/**
 * Web Services Controller to expose the functionality of the
 * {@link uk.co.thebodyshop.core.content.ContentSearchFacade} and SearchFacade.
 */
@Controller
@Api(tags = "Content")
@RequestMapping(value = "/{baseSiteId}/content")
public class ContentSearchController extends BaseController
{

    @Resource
    private ContentSearchHelper contentSearchHelper;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(nickname = "getContent", value = "Get a list of content and additional data", notes =
            "Returns a list of content and additional data, such as available facets, "
                    + "available sorting, and pagination options. It can also include spelling suggestions. To make spelling suggestions work, you need to make sure "
                    + "that \"enableSpellCheck\" on the SearchQuery is set to \"true\" (by default, it should already be set to \"true\"). You also need to have indexed "
                    + "properties configured to be used for spellchecking.")
    @ApiBaseSiteIdParam
    public ContentSearchPageWsDTO getContent(
            @ApiParam(value = "Serialized query, free text search, facets. The format of a serialized query: freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String query,
            @ApiParam(value = "The current result page requested.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
            @ApiParam(value = "The number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
            @ApiParam(value = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
            @ApiParam(value = "The context to be used in the search query.") @RequestParam(required = false) final String searchQueryContext,
            @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletResponse response)
    {
        final ContentSearchPageWsDTO result = contentSearchHelper
                .searchContent(query, currentPage, pageSize, sort, addPaginationField(fields), searchQueryContext);
        // X-Total-Count header
        setTotalCountHeader(response, result.getPagination());
        return result;
    }


    @RequestMapping(value = "/search", method = RequestMethod.HEAD)
    @ApiOperation(nickname = "countContent", value = "Get a header with total number of content.", notes = "In the response header, the \"x-total-count\" indicates the total number of content satisfying a query.")
    @ApiBaseSiteIdParam
    public void countContent(
            @ApiParam(value = "Serialized query, free text search, facets. The format of a serialized query: freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2") @RequestParam(required = false) final String query,
            final HttpServletResponse response)
    {
        final ContentSearchPageData<SearchStateData, ContentPageData> result = contentSearchHelper.searchContent(query, 0, 1, null);
        setTotalCountHeader(response, result.getPagination());
    }
}
