/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import java.util.Collections;
import java.util.List;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.daos.TbsProductFacetDao;
import uk.co.thebodyshop.core.model.CategoryTypeFacetModel;
import uk.co.thebodyshop.core.model.ConcernFacetModel;
import uk.co.thebodyshop.core.model.CoverageFacetModel;
import uk.co.thebodyshop.core.model.FinishFacetModel;
import uk.co.thebodyshop.core.model.FreeFromFacetModel;
import uk.co.thebodyshop.core.model.ProductTypeFacetModel;
import uk.co.thebodyshop.core.model.RangeFacetModel;
import uk.co.thebodyshop.core.model.ScentFacetModel;
import uk.co.thebodyshop.core.model.TargetAreaFacetModel;
import uk.co.thebodyshop.core.model.TbsProductFacetModel;
import uk.co.thebodyshop.core.model.VeganFacetModel;

public class DefaultTbsProductFacetDao implements TbsProductFacetDao
{
	private final FlexibleSearchService flexibleSearchService;

	@Override
	public List<TbsProductFacetModel> findFacetsByTypeAndCode(final String derivedType, final String code)
	{
		List<TbsProductFacetModel> resultList;
		final String validType = lookUpValidType(derivedType);

		if (validType != null)
		{
			final StringBuilder query = new StringBuilder();
			query.append("SELECT {").append(TbsProductFacetModel.PK);
			query.append("} FROM {").append(validType);
			query.append("} WHERE {").append(TbsProductFacetModel.CODE);
			query.append("} = ?code");

			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
			searchQuery.addQueryParameter("code", code);

			final SearchResult<TbsProductFacetModel> searchResult = getFlexibleSearchService().search(searchQuery);
			resultList = searchResult.getResult();
		}
		else
		{
			resultList = Collections.EMPTY_LIST;
		}
		return resultList;
	}

	private String lookUpValidType(final String derivedType)
	{
		if (FinishFacetModel._TYPECODE.equals(derivedType))
		{
			return FinishFacetModel._TYPECODE;
		}
		if (TargetAreaFacetModel._TYPECODE.equals(derivedType))
		{
			return TargetAreaFacetModel._TYPECODE;
		}
		if (ConcernFacetModel._TYPECODE.equals(derivedType))
		{
			return ConcernFacetModel._TYPECODE;
		}
		if (ScentFacetModel._TYPECODE.equals(derivedType))
		{
			return ScentFacetModel._TYPECODE;
		}
		if (CoverageFacetModel._TYPECODE.equals(derivedType))
		{
			return CoverageFacetModel._TYPECODE;
		}
		if (VeganFacetModel._TYPECODE.equals(derivedType))
		{
			return VeganFacetModel._TYPECODE;
		}
		if (FreeFromFacetModel._TYPECODE.equals(derivedType))
		{
			return FreeFromFacetModel._TYPECODE;
		}
		if (RangeFacetModel._TYPECODE.equals(derivedType))
		{
			return RangeFacetModel._TYPECODE;
		}
		if (ProductTypeFacetModel._TYPECODE.equals(derivedType))
		{
			return ProductTypeFacetModel._TYPECODE;
		}
		if (CategoryTypeFacetModel._TYPECODE.equals(derivedType))
		{
			return CategoryTypeFacetModel._TYPECODE;
		}

		return null;
	}

	public DefaultTbsProductFacetDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
