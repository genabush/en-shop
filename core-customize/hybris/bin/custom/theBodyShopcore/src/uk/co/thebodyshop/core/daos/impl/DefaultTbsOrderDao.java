/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.daos.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import uk.co.thebodyshop.core.daos.TbsOrderDao;


/**
 * @author vasanthramprakasam
 */
public class DefaultTbsOrderDao implements TbsOrderDao
{

	private static final String FIND_ORDER_QUERY = "select {" + OrderModel.PK + "} from {" + OrderModel._TYPECODE + "} where {" + OrderModel.CODE + "} = ?code";

	private final FlexibleSearchService flexibleSearchService;

	public DefaultTbsOrderDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public OrderModel findOrderByCode(final String code)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ORDER_QUERY);
		query.addQueryParameter("code",code);
		return getFlexibleSearchService().searchUnique(query);
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

}
