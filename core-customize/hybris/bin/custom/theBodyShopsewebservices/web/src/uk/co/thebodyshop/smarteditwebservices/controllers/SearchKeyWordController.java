/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.smarteditwebservices.controllers;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmssmarteditwebservices.dto.PageableWsDTO;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.co.thebodyshop.core.keywords.data.SearchKeyWordData;
import uk.co.thebodyshop.core.keywords.ws.data.SearchKeyWordListWsDTO;
import uk.co.thebodyshop.core.keywords.ws.data.SearchKeyWordWsDTO;
import uk.co.thebodyshop.core.services.TbsSearchKeywordService;


/**
 * @author vasanthramprakasam
 */
@Controller
@RequestMapping(value = "/searchKeywords")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@Api(tags = "SearchKeywords")
public class SearchKeyWordController
{
	@Resource
	private TbsSearchKeywordService searchKeywordService;

	@Resource
	private WebPaginationUtils webPaginationUtils;

	@Resource
	private DataMapper dataMapper;

	@RequestMapping(value = "/{searchKeyWordCode}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get search keyword by code", notes = "Get a search keyword that matches the given code.")
	@ApiResponses(
	{ @ApiResponse(code = 400, message = "When the search keyword was not found (CMSItemNotFoundException) or when there was problem during conversion (ConversionException)."),
			@ApiResponse(code = 200, message = "SearchKeyWordWsDTO", response = SearchKeyWordWsDTO.class) })
	public SearchKeyWordWsDTO getSearchKeyWordById(@ApiParam(value = "The unique identifier of the search keyword", required = true)
	@PathVariable
	final String searchKeyWordCode) throws CMSItemNotFoundException
	{
		final SearchKeyWordData data = searchKeywordService.getSearchKeyWordForCode(searchKeyWordCode);
		return dataMapper.map(data, SearchKeyWordWsDTO.class);
	}

	@RequestMapping(method = RequestMethod.GET, params =
	{ "pageSize" })
	@ResponseBody
	@ApiOperation(value = "Find keywords by text or mask", notes = "Endpoint to retrieve keywords.")
	@ApiResponses(
	{ @ApiResponse(code = 200, message = "DTO which serves as a wrapper object that contains a list of Keyworddata, never null", response = SearchKeyWordListWsDTO.class) })
	@ApiImplicitParams(
	{ @ApiImplicitParam(name = "pageSize", value = "The maximum number of elements in the result list.", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "currentPage", value = "The requested page number", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "sort", value = "The string field the results will be sorted with", dataType = "string", paramType = "query") })
	public SearchKeyWordListWsDTO findSearchKeywords(@ApiParam(value = "The string value on which products will be filtered")
	@RequestParam(required = false)
	final String text, @ApiParam(value = "The string value on which search keywords will be filtered if no text value is provided")
	@RequestParam(required = false)
	final String mask, @ApiParam(value = "Pageable DTO", required = true)
	@ModelAttribute
	final PageableWsDTO pageableDto)
	{
		final String searchText = Strings.isNullOrEmpty(text) ? mask : text;
		final PageableData pageableData = Optional.of(pageableDto) //
				.map(pageableWsDTO -> dataMapper.map(pageableWsDTO, PageableData.class)) //
				.orElse(null);

		final SearchResult<SearchKeyWordData> results = searchKeywordService.getSearchKeywordsMatchingTerm(searchText, pageableData);

		final SearchKeyWordListWsDTO searchKeyWordListWsDTO = new SearchKeyWordListWsDTO();
		searchKeyWordListWsDTO.setKeywords(results.getResult() //
				.stream() //
				.map(keyWordData -> dataMapper.map(keyWordData, SearchKeyWordWsDTO.class)) //
				.collect(Collectors.toList()));
		searchKeyWordListWsDTO.setPagination(webPaginationUtils.buildPagination(results));
		return searchKeyWordListWsDTO;
	}

}
