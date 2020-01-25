/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import uk.co.thebodyshop.core.daos.TbsMarketSelectorDao;
import uk.co.thebodyshop.core.model.MarketSelectorSiteModel;
import uk.co.thebodyshop.core.services.MarketSelectorService;

/**
 * @author Lakshmi
 */
public class DefaultMarketSelectorService implements MarketSelectorService
{

	private final TbsMarketSelectorDao tbsMarketSelectorDao;

	@Override
	public List<MarketSelectorSiteModel> getAllEnabledSites()
	{
		return getTbsMarketSelectorDao().findAllEnabledSites();
	}

	@Autowired
	public DefaultMarketSelectorService(final TbsMarketSelectorDao tbsMarketSelectorDao)
	{
		this.tbsMarketSelectorDao = tbsMarketSelectorDao;
	}

	protected TbsMarketSelectorDao getTbsMarketSelectorDao()
	{
		return tbsMarketSelectorDao;
	}
}
