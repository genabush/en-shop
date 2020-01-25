/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import java.util.List;

import uk.co.thebodyshop.core.model.MarketSelectorSiteModel;

/**
 * @author Lakshmi
 */
public interface TbsMarketSelectorDao
{
	public List<MarketSelectorSiteModel> findAllEnabledSites();
}
