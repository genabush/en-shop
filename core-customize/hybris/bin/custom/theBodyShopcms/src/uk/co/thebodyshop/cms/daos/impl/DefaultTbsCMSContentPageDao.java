/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.thebodyshop.cms.daos.TbsCMSContentPageDao;


public class DefaultTbsCMSContentPageDao implements TbsCMSContentPageDao
{
	private final FlexibleSearchService flexibleSearchService;

	private static final String FIND_ACTIVE_CMS_LINK_COMPONENT = "SELECT {" + CMSLinkComponentModel.PK + "} " + "FROM {"
			+ CMSLinkComponentModel._TYPECODE + "} " + "WHERE {" + CMSLinkComponentModel.URL + "} = ?url AND {"
			+ CMSLinkComponentModel.CATALOGVERSION + "} = ?cv";

	@Override
	public CMSLinkComponentModel findActiveCMSLinkComponent(final String url, final CatalogVersionModel catalogVersion)
	{
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(FIND_ACTIVE_CMS_LINK_COMPONENT);
		flexibleSearchQuery.addQueryParameter("url", url);
		flexibleSearchQuery.addQueryParameter("cv", catalogVersion);
		final SearchResult<CMSLinkComponentModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);
		if (CollectionUtils.isNotEmpty(searchResult.getResult()))
		{
			return searchResult.getResult().get(0);
		}
		return null;
	}

	@Autowired
	public DefaultTbsCMSContentPageDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

}
