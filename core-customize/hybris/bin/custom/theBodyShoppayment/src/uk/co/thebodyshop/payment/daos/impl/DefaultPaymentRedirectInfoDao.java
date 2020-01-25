/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.daos.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.payment.daos.PaymentRedirectInfoDao;
import uk.co.thebodyshop.payment.model.PaymentRedirectInfoModel;

/**
 * @author Marcin
 */
public class DefaultPaymentRedirectInfoDao implements PaymentRedirectInfoDao
{

	private static final String CART_ID = "cartId";

	private static final String USER_ID = "userId";

	private final FlexibleSearchService flexibleSearchService;

	private static final String FIND_PAYMENT_REDIRECT_INFO_QUERY = "SELECT {" + PaymentRedirectInfoModel.PK + "} " + "FROM {" + PaymentRedirectInfoModel._TYPECODE + "} " + "WHERE {" + PaymentRedirectInfoModel.USERID + "} = ?userId " + "AND {"
			+ PaymentRedirectInfoModel.CARTID + "} = ?cartId " + "ORDER BY {" + PaymentRedirectInfoModel.CREATIONTIME + "} DESC";

	private static final String FIND_ALL_PAYMENT_REDIRECT_INFOS_QUERY = "SELECT {" + PaymentRedirectInfoModel.PK + "} " + "FROM {" + PaymentRedirectInfoModel._TYPECODE + "} " + "WHERE {" + PaymentRedirectInfoModel.USERID + "} = ?userId " + "AND {"
			+ PaymentRedirectInfoModel.CARTID + "} = ?cartId ";

	@Override
	public PaymentRedirectInfoModel findPaymentRedirectInfoForUserAndCart(final String userId, final String cartId)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(USER_ID, userId);
		params.put(CART_ID, cartId);
		final SearchResult<PaymentRedirectInfoModel> searchResult = getFlexibleSearchService().search(FIND_PAYMENT_REDIRECT_INFO_QUERY, params);
		if (Objects.nonNull(searchResult) && CollectionUtils.isNotEmpty(searchResult.getResult()))
		{
			return searchResult.getResult().get(0);
		}
		return null;
	}

	@Override
	public List<PaymentRedirectInfoModel> findAllPaymentRedirectInfosForUserAndCart(final String userId, final String cartId)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(USER_ID, userId);
		params.put(CART_ID, cartId);
		final SearchResult<PaymentRedirectInfoModel> searchResult = getFlexibleSearchService().search(FIND_ALL_PAYMENT_REDIRECT_INFOS_QUERY, params);
		if (Objects.nonNull(searchResult))
		{
			return searchResult.getResult();
		}
		return null;
	}

	public DefaultPaymentRedirectInfoDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
