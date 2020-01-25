/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Krishna
 */
public class VariantProductNameValueProvider extends AbstractValueResolver<TbsVariantProductModel, Object, Object>
{
	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final TbsVariantProductModel model, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{
		document.addField(indexedProperty, getProductName(model), resolverContext.getFieldQualifier());
	}

	private String getProductName(final TbsVariantProductModel variant)
	{
		if (null != variant.getBaseProduct())
		{
			final ProductModel baseProduct = variant.getBaseProduct();
			if (StringUtils.isNotEmpty(baseProduct.getName()))
			{
				return baseProduct.getName();
			}
		}
		return variant.getName();
	}
}
