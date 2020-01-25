/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.dao.TbsProductDao;

/**
 * @author prateek.goel
 */
public class DefaultTbsProductDao extends DefaultProductDao implements TbsProductDao
{

	private static final String FIND_VARINAT_PRODUCTS_QUERY = "SELECT {" + TbsVariantProductModel.PK + "} " + "FROM {" + TbsVariantProductModel._TYPECODE + "} " + "WHERE {" + TbsVariantProductModel.CREATIONTIME + "} > ?creationTime and {"
			+ TbsVariantProductModel.APPROVALSTATUS + "} = ?approvalStatus and {" + TbsVariantProductModel.CATALOGVERSION + "} = ?cv";

	private static final String FIND_BASE_PRODUCTS_QUERY = "SELECT {" + TbsBaseProductModel.PK + "} " + "FROM {" + TbsBaseProductModel._TYPECODE + "} " + "WHERE {" + TbsBaseProductModel.CREATIONTIME + "} > ?creationTime and {"
			+ TbsBaseProductModel.APPROVALSTATUS + "} = ?approvalStatus and {" + TbsBaseProductModel.CATALOGVERSION + "} = ?cv";

	public DefaultTbsProductDao(final String typecode)
	{
		super(typecode);
	}

	@Override
	public List<TbsVariantProductModel> fetchRecentlyCreatedVariantProducts(final CatalogVersionModel catalogVersion, final Date lastSuccessfulTime)
	{
		final Map<String, Object> params = new HashMap<>();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(lastSuccessfulTime);
		cal.add(Calendar.HOUR, -1);
		final Date suggestedTime = cal.getTime();
		params.put("creationTime", suggestedTime);
		params.put("approvalStatus", ArticleApprovalStatus.READYTOBELOCALISED);
		params.put("cv", catalogVersion);
		final SearchResult<TbsVariantProductModel> searchResult = getFlexibleSearchService().search(FIND_VARINAT_PRODUCTS_QUERY, params);
		return searchResult.getResult();
	}

	@Override
	public List<TbsBaseProductModel> fetchRecentlyCreatedBaseProducts(CatalogVersionModel catalogVersion, Date lastSuccessfulTime) {
		final Map<String, Object> params = new HashMap<>();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(lastSuccessfulTime);
		cal.add(Calendar.HOUR, -1);
		final Date suggestedTime = cal.getTime();
		params.put("creationTime", suggestedTime);
		params.put("approvalStatus", ArticleApprovalStatus.READYTOBELOCALISED);
		params.put("cv", catalogVersion);
		final SearchResult<TbsBaseProductModel> searchResult = getFlexibleSearchService().search(FIND_BASE_PRODUCTS_QUERY, params);
		return searchResult.getResult();
	}

}
