/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services.impl;

import java.util.stream.Collectors;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import uk.co.thebodyshop.cms.model.SearchKeywordModel;
import uk.co.thebodyshop.core.daos.TbsSearchKeywordDao;
import uk.co.thebodyshop.core.keywords.data.SearchKeyWordData;
import uk.co.thebodyshop.core.services.TbsSearchKeywordService;

/**
 * @author vasanthramprakasam
 */
public class DefaultSearchKeywordService implements TbsSearchKeywordService
{

	private final TbsSearchKeywordDao tbsSearchKeywordDao;

	private final Converter<SearchKeywordModel, SearchKeyWordData> searchKeyWordDataConverter;

	public DefaultSearchKeywordService(final TbsSearchKeywordDao tbsSearchKeywordDao, final Converter<SearchKeywordModel, SearchKeyWordData> searchKeyWordDataConverter)
	{
		this.tbsSearchKeywordDao = tbsSearchKeywordDao;
		this.searchKeyWordDataConverter = searchKeyWordDataConverter;
	}

	@Override
	public SearchKeyWordData getSearchKeyWordForCode(final String code) throws CMSItemNotFoundException
	{
		try
		{
			return getSearchKeyWordDataConverter().convert(getTbsSearchKeywordDao().findKeywordForCode(code));
		}
		catch (final ModelNotFoundException mne)
		{
			throw new CMSItemNotFoundException("SearchKeyword with code [" + code + "] is not found", mne);
		}
	}

	@Override
	public SearchResult<SearchKeyWordData> getSearchKeywordsMatchingTerm(final String searchTerm, final PageableData pageableData)
	{
		final SearchResult<SearchKeywordModel> searchResult = getTbsSearchKeywordDao().findAllKeywordsMatchingCode(searchTerm, pageableData);
		return new SearchResultImpl<>(searchResult.getResult().stream() //
				.map(keyword -> getSearchKeyWordDataConverter().convert(keyword)).collect(Collectors.toList()), //
				searchResult.getTotalCount(), //
				searchResult.getRequestedCount(), //
				searchResult.getRequestedStart());
	}

	protected TbsSearchKeywordDao getTbsSearchKeywordDao()
	{
		return tbsSearchKeywordDao;
	}

	protected Converter<SearchKeywordModel, SearchKeyWordData> getSearchKeyWordDataConverter()
	{
		return searchKeyWordDataConverter;
	}
}
