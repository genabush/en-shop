/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.daos.EmailWhenInStockDao;
import uk.co.thebodyshop.core.model.EmailWhenInStockModel;

/**
 * @author Krishna
 */
public class DefaultEmailWhenInStockDao implements EmailWhenInStockDao
{
	private static final String GET_EMAIL_WHEN_INSTOCK_QUERY = "SELECT {" + EmailWhenInStockModel.PK + "} " + "FROM {" + EmailWhenInStockModel._TYPECODE + "} " + "WHERE {" + EmailWhenInStockModel.EMAIL + "} = ?emailId " + " AND {"
			+ EmailWhenInStockModel.PRODUCTCODE + "} = ?productCode " + "AND {" + EmailWhenInStockModel.BASESITE + "} = ?baseSite";
	private static final String FIND_EMAIL_WHEN_IN_STOCK_BY_PCODE_AND_CMS_SITE = "SELECT {" + EmailWhenInStockModel.PK + "} " + "FROM {" + EmailWhenInStockModel._TYPECODE + "} " + "WHERE {" + EmailWhenInStockModel.PRODUCTCODE + "} = ?productCode "
			+ "AND {" + EmailWhenInStockModel.BASESITE + "} = ?baseSite";

	private final FlexibleSearchService flexibleSearchService;

	@Override
	public EmailWhenInStockModel getEmailWhenInStock(final String email, final String productCode, final CMSSiteModel baseSite)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_EMAIL_WHEN_INSTOCK_QUERY);
		query.addQueryParameter("emailId", email);
		query.addQueryParameter("productCode", productCode);
		query.addQueryParameter("baseSite", baseSite);
		final SearchResult<EmailWhenInStockModel> searchResult = getFlexibleSearchService().search(query);
		return CollectionUtils.isNotEmpty(searchResult.getResult()) ? searchResult.getResult().get(0) : null;
	}

	@Override
	public Collection<EmailWhenInStockModel> getEmailWhenInStockRecordsByProductAndSite(final String productCode, final CMSSiteModel baseSite)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_EMAIL_WHEN_IN_STOCK_BY_PCODE_AND_CMS_SITE);
		query.addQueryParameter("productCode", productCode);
		query.addQueryParameter("baseSite", baseSite);
		final SearchResult<EmailWhenInStockModel> searchResult = getFlexibleSearchService().search(query);
		return searchResult.getResult();
	}

	public DefaultEmailWhenInStockDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
