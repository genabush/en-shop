/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import uk.co.thebodyshop.core.daos.impl.DefaultTbsProductFacetDao;
import uk.co.thebodyshop.core.model.FinishFacetModel;
import uk.co.thebodyshop.core.model.TbsProductFacetModel;

@UnitTest
public class DefaultTbsProductFacetDaoTest
{

	public static final String TEST_FINISH_CODE = "TEST_FINISH_CODE";
	public static final String INVALID_TYPE = "INVALID_TYPE";
	private DefaultTbsProductFacetDao defaultTbsProductFacetDao;
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setUp()
	{
		flexibleSearchService = mock(FlexibleSearchService.class);
		defaultTbsProductFacetDao = new DefaultTbsProductFacetDao(flexibleSearchService);
	}

	@Test
	public void shouldInvokeFlexibleSearchServiceCorrectly()
	{
		final FlexibleSearchQuery query = generateTestFlexibleSearchQuery(TEST_FINISH_CODE, FinishFacetModel._TYPECODE);
		testFlexibleSearchServiceInvoked(TEST_FINISH_CODE, FinishFacetModel._TYPECODE, query, 1);
	}

	@Test
	public void shouldNotInvokeFlexibleSearchWithIncorrectType()
	{
		final FlexibleSearchQuery query = generateTestFlexibleSearchQuery(TEST_FINISH_CODE, INVALID_TYPE);
		testFlexibleSearchServiceInvoked(TEST_FINISH_CODE, INVALID_TYPE, query, 0);
	}

	private void testFlexibleSearchServiceInvoked(final String facetCode, final String facetType, final FlexibleSearchQuery query, final int times)
	{
		when(flexibleSearchService.search(any(FlexibleSearchQuery.class))).thenReturn(new SearchResultImpl<>(Collections.EMPTY_LIST, 1, 1, 1));
		defaultTbsProductFacetDao.findFacetsByTypeAndCode(facetType, facetCode);
		verify(flexibleSearchService, times(times)).search(query);
	}

	private FlexibleSearchQuery generateTestFlexibleSearchQuery(final String facetCode, final String facetType)
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT {").append(TbsProductFacetModel.PK);
		query.append("} FROM {").append(facetType);
		query.append("} WHERE {").append(TbsProductFacetModel.CODE);
		query.append("} = ?code");

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
		searchQuery.addQueryParameter("code", facetCode);
		return searchQuery;
	}

}
