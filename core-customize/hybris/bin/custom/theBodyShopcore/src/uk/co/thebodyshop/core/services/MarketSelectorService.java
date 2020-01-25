/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import java.util.List;

import uk.co.thebodyshop.core.model.MarketSelectorSiteModel;

/**
 * @author Lakshmi
 */
public interface MarketSelectorService
{
	List<MarketSelectorSiteModel> getAllEnabledSites();
}
