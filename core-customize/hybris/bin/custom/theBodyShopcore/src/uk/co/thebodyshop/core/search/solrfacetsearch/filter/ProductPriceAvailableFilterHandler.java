/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.filter;

import java.util.Map;
import java.util.Optional;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * @author vasanthramprakasam
 */
public class ProductPriceAvailableFilterHandler implements SearchFilterHandler
{
	private static final String PRODUCT_PRICE_AVAILABLE_FIELD = "productPriceAvailable";

	private final FieldNameProvider fieldNameProvider;

	public ProductPriceAvailableFilterHandler(
			FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public void addFilterQuery(final SearchQuery searchQuery, final IndexedType indexedType)
	{
		final Map<String, IndexedProperty> indexedProperties = indexedType.getIndexedProperties();
		final Optional<IndexedProperty> productVisiblityindexedProperty = Optional.ofNullable(indexedProperties.get(
				PRODUCT_PRICE_AVAILABLE_FIELD));
		if (productVisiblityindexedProperty.isPresent())
		{
			searchQuery.addFilterQuery(getFieldNameProvider().getFieldName(productVisiblityindexedProperty.get(), null, FieldNameProvider.FieldType.INDEX), SearchQuery.Operator.AND, "true");
		}
	}

	@Override
	public boolean canAddFilter(SearchQuery searchQuery, IndexedType indexedType)
	{
		return !indexedType.getComposedType().getCode().equals(ContentPageModel._TYPECODE);
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}
}
