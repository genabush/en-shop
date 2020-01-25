/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import uk.co.thebodyshop.core.daos.TbsProductFacetDao;
import uk.co.thebodyshop.core.model.TbsProductFacetModel;
import uk.co.thebodyshop.core.services.ProductFacetService;

public class DefaultProductFacetService implements ProductFacetService
{
	TbsProductFacetDao tbsProductFacetDao;

	public DefaultProductFacetService(final TbsProductFacetDao tbsProductFacetDao)
	{
		this.tbsProductFacetDao = tbsProductFacetDao;
	}

	@Override
	public TbsProductFacetModel getFacetByTypeAndCode(final String derivedType, final String code)
	{
		TbsProductFacetModel result = null;
		final List<TbsProductFacetModel> tbsProductFacets = getTbsProductFacetDao().findFacetsByTypeAndCode(derivedType, code);

		if (CollectionUtils.isNotEmpty(tbsProductFacets))
		{
			result = tbsProductFacets.get(0);
		}

		return result;
	}

	protected TbsProductFacetDao getTbsProductFacetDao()
	{
		return tbsProductFacetDao;
	}
}
