/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.qualifier.provider.impl;

import java.util.Map;
import java.util.Objects;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.CurrencyQualifierProvider;

/**
 * @author Jagadeesh
 */
public class TbsCurrencyQualifierProvider extends CurrencyQualifierProvider
{

	private final Map<String, String> attributeCurrencyMap;

	public TbsCurrencyQualifierProvider(final Map<String, String> attributeCurrencyMap)
	{
		this.attributeCurrencyMap = attributeCurrencyMap;
	}

	@Override
	public boolean canApply(final IndexedProperty indexedProperty)
	{
		Objects.requireNonNull(indexedProperty, "indexedProperty is null");
		return getAttributeCurrencyMap().containsKey(indexedProperty.getName()) || indexedProperty.isCurrency();
	}

	protected Map<String, String> getAttributeCurrencyMap()
	{
		return attributeCurrencyMap;
	}

}
