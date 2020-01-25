/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.cms.model.SearchKeywordModel;

/**
 * @author vasanthramprakasam
 */
public interface TbsSearchKeywordDao
{
	SearchResult<SearchKeywordModel> findAllKeywordsMatchingCode(String searchTerm, PageableData pageableData);

	SearchKeywordModel findKeywordForCode(String code);
}
