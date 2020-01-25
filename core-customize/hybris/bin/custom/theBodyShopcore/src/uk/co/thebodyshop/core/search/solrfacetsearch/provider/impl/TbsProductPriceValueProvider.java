/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.ProductPriceValueProvider;

import uk.co.thebodyshop.core.services.UserPriceGroupService;

public class TbsProductPriceValueProvider extends ProductPriceValueProvider
{
	private final CommercePriceService commercePriceService;

	private final FieldNameProvider fieldNameProvider;

	private final UserPriceGroupService userPriceGroupService;

	public TbsProductPriceValueProvider(final CommercePriceService commercePriceService, final FieldNameProvider fieldNameProvider, final UserPriceGroupService userPriceGroupService)
	{
		this.commercePriceService = commercePriceService;
		this.fieldNameProvider = fieldNameProvider;
		this.userPriceGroupService = userPriceGroupService;
		super.setFieldNameProvider(fieldNameProvider);
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final Object model) throws FieldValueProviderException
	{
		final Collection<FieldValue> fieldValues = new ArrayList<>();
		if (null != indexConfig.getBaseSite())
		{
			userPriceGroupService.setUserPriceGroupForSite(indexConfig.getBaseSite());
			fieldValues.addAll(super.getFieldValues(indexConfig, indexedProperty, model));
		}
		return fieldValues;
	}

	@Override
	protected void addFieldValues(final Collection<FieldValue> fieldValues, final ProductModel product, final IndexedProperty indexedProperty, final String currency) throws FieldValueProviderException
	{
		final PriceInformation price = commercePriceService.getFromPriceForProduct(product);
		if (price == null)
		{
			return;
		}
		final Double value = Double.valueOf(price.getPriceValue().getValue());
		final List<String> rangeNameList = getRangeNameList(indexedProperty, value, currency);
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, currency == null ? null : currency.toLowerCase(Locale.ROOT));

		for (final String fieldName : fieldNames)
		{
			if (rangeNameList.isEmpty())
			{
				fieldValues.add(new FieldValue(fieldName, value));
			}
			else
			{
				for (final String rangeName : rangeNameList)
				{
					fieldValues.add(new FieldValue(fieldName, rangeName == null ? value : rangeName));
				}
			}
		}
	}

	protected CommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	protected UserPriceGroupService getUserPriceGroupService()
	{
		return userPriceGroupService;
	}
}
