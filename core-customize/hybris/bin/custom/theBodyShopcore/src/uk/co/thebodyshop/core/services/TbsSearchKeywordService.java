/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.keywords.data.SearchKeyWordData;

/**
 * @author vasanthramprakasam
 */
public interface TbsSearchKeywordService
{

	SearchKeyWordData getSearchKeyWordForCode(String code) throws CMSItemNotFoundException;

	SearchResult<SearchKeyWordData> getSearchKeywordsMatchingTerm(String searchTerm, PageableData pageableData);

}
