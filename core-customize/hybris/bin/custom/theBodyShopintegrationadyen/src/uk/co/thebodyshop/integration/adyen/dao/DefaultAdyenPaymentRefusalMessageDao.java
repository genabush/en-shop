/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import uk.co.thebodyshop.integration.adyen.model.AdyenPaymentRefusalMessageModel;

/**
 * @author Krishna
 */
public class DefaultAdyenPaymentRefusalMessageDao implements AdyenPaymentRefusalMessageDao
{
	private final FlexibleSearchService flexibleSearchService;
	private static final String QUERY = "SELECT {" + AdyenPaymentRefusalMessageModel.PK + "} " + "FROM {" + AdyenPaymentRefusalMessageModel._TYPECODE + "} ";

	@Override
	public List<AdyenPaymentRefusalMessageModel> getRefusalMessages()
	{
		final SearchResult<AdyenPaymentRefusalMessageModel> searchResult = getFlexibleSearchService().search(QUERY);
		return searchResult.getResult();
	}

	public DefaultAdyenPaymentRefusalMessageDao(final FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	protected FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}
}
