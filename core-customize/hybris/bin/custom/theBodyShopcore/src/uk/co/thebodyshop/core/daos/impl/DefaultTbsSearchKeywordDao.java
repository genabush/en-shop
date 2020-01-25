/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.daos.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.cms.model.SearchKeywordModel;
import uk.co.thebodyshop.core.daos.TbsSearchKeywordDao;

/**
 * @author vasanthramprakasam
 */
public class DefaultTbsSearchKeywordDao implements TbsSearchKeywordDao
{

	private final FlexibleSearchService flexibleSearchService;

	private static final String LIKE_FORMAT = "{0}{1}{2}";

	public DefaultTbsSearchKeywordDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public SearchResult<SearchKeywordModel> findAllKeywordsMatchingCode(final String searchTerm, final PageableData pageableData)
	{
		final StringBuilder query = getBaseQuery().append(" WHERE LOWER({code}) LIKE ?code");
		final String code = MessageFormat.format(LIKE_FORMAT, "%", StringUtils.isNotBlank(searchTerm) ? searchTerm.toLowerCase() : "", "%");
		final Map<String, Object> params = new HashMap();
		params.put("code", code);
		final SearchResult<SearchKeywordModel> searchResult = flexibleSearchService.search(buildQuery(query, params, pageableData.getCurrentPage(), pageableData.getPageSize()));
		return searchResult;
	}

	@Override
	public SearchKeywordModel findKeywordForCode(final String code)
	{
		final StringBuilder query = getBaseQuery().append(" WHERE {code} = ?code");
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		searchQuery.addQueryParameter("code", code);
		return flexibleSearchService.searchUnique(searchQuery);
	}

	protected FlexibleSearchQuery buildQuery(final StringBuilder baseQuery, final Map<String, Object> params, final int currentPage, final int pageSize)
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(baseQuery);
		searchQuery.addQueryParameters(params);
		searchQuery.setStart(currentPage * pageSize);
		searchQuery.setCount(pageSize);
		searchQuery.setNeedTotal(true);
		return searchQuery;
	}

	protected StringBuilder getBaseQuery()
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT {").append(SearchKeywordModel.PK);
		query.append("} FROM {").append(SearchKeywordModel._TYPECODE);
		query.append("}");
		return query;
	}
}
