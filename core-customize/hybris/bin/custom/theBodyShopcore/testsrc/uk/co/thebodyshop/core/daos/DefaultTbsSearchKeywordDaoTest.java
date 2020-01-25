/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.cms.model.SearchKeywordModel;
import uk.co.thebodyshop.core.daos.impl.DefaultTbsSearchKeywordDao;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultTbsSearchKeywordDaoTest
{

	private static final String EXPECTED_FIND_MATCHING_QUERY = "SELECT {" + SearchKeywordModel.PK + "} FROM {" + SearchKeywordModel._TYPECODE + "} WHERE LOWER({" + SearchKeywordModel.CODE + "}) LIKE ?code";

	private static final String EXPECTED_FIND_SINGLE = "SELECT {" + SearchKeywordModel.PK + "} FROM {" + SearchKeywordModel._TYPECODE + "} WHERE {" + SearchKeywordModel.CODE + "} = ?code";
	public static final int CURRENT_PAGE = 0;
	public static final int PAGE_SIZE = 10;

	private DefaultTbsSearchKeywordDao tbsSearchKeywordDao;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private SearchResult searchResult;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		tbsSearchKeywordDao = new DefaultTbsSearchKeywordDao(flexibleSearchService);
	}

	@Test
	public void testGetKeyWordsMatchingTerm()
	{
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		final ArgumentCaptor<FlexibleSearchQuery> captor = ArgumentCaptor.forClass(FlexibleSearchQuery.class);
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(CURRENT_PAGE);
		pageableData.setPageSize(PAGE_SIZE);
		tbsSearchKeywordDao.findAllKeywordsMatchingCode("term",pageableData);
		verify(flexibleSearchService, times(1)).search(captor.capture());

		final FlexibleSearchQuery expectedQuery = captor.getValue();
		assertTrue(expectedQuery.getQueryParameters().containsKey("code"));
		assertTrue(expectedQuery.getQueryParameters().containsValue("%term%"));
		assertEquals(expectedQuery.getStart(),0);
		assertEquals(expectedQuery.getCount(),10);
		assertEquals(expectedQuery.getQuery(), EXPECTED_FIND_MATCHING_QUERY);
	}

	@Test
	public void testGetKeyWordForCode()
	{
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(searchResult);
		final ArgumentCaptor<FlexibleSearchQuery> captor = ArgumentCaptor.forClass(FlexibleSearchQuery.class);
		tbsSearchKeywordDao.findKeywordForCode("code");
		verify(flexibleSearchService, times(1)).searchUnique(captor.capture());
		assertEquals(captor.getValue().getQueryParameters().get("code"),"code");
		assertEquals(captor.getValue().getQuery(), EXPECTED_FIND_SINGLE);
	}

}
