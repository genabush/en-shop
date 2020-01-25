/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.daos.impl.DefaultTbsMarketSelectorDao;
import uk.co.thebodyshop.core.model.MarketSelectorSiteModel;

/**
 * @author Marcin
 */
@UnitTest
public class DefaultTbsMarketSelectorDaoTest
{
	private static final String EXPECTED_FIND_ALL_QUERY = "SELECT {" + MarketSelectorSiteModel.PK + "} FROM {" + MarketSelectorSiteModel._TYPECODE + "} WHERE {" + MarketSelectorSiteModel.ENABLED + "} = ?enabled";

	@InjectMocks
	private DefaultTbsMarketSelectorDao defaultTbsMarketSelectorDao;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private SearchResult<MarketSelectorSiteModel> searchResult;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMarketSelectorSearchWithNoSelecotrSites()
	{
		when(flexibleSearchService.search(eq(EXPECTED_FIND_ALL_QUERY), any(Map.class))).thenReturn(searchResult);

		final ArgumentCaptor<HashMap> captor = ArgumentCaptor.forClass(HashMap.class);

		defaultTbsMarketSelectorDao.findAllEnabledSites();

		verify(flexibleSearchService, times(1)).search(eq(EXPECTED_FIND_ALL_QUERY), captor.capture());

		assertTrue((Boolean) captor.getValue().get(MarketSelectorSiteModel.ENABLED));
	}
}
