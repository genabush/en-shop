/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.daos.TbsMarketSelectorDao;
import uk.co.thebodyshop.core.model.MarketSelectorSiteModel;

/**
 * @author Lakshmi
 */
public class DefaultTbsMarketSelectorDao implements TbsMarketSelectorDao
{
	private final FlexibleSearchService flexibleSearchService;

	private static final String FIND_SITES_QUERY = "SELECT {" + MarketSelectorSiteModel.PK + "} " + "FROM {" + MarketSelectorSiteModel._TYPECODE + "} " + "WHERE {" + MarketSelectorSiteModel.ENABLED + "} = ?enabled";

	@Override
	public List<MarketSelectorSiteModel> findAllEnabledSites()
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(MarketSelectorSiteModel.ENABLED, Boolean.TRUE);
		final SearchResult<MarketSelectorSiteModel> searchResult = getFlexibleSearchService().search(FIND_SITES_QUERY, params);
		return searchResult.getResult();
	}

	public DefaultTbsMarketSelectorDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
