/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Marcin
 */
public class ProductVariantFlagValueProvider extends AbstractValueResolver<ItemModel, Object, Object>
{

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final ItemModel model, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{
		if (model instanceof TbsBaseProductModel)
		{
			document.addField(indexedProperty, Boolean.FALSE, resolverContext.getFieldQualifier());
		}
		else if (model instanceof TbsVariantProductModel)
		{
			document.addField(indexedProperty, Boolean.TRUE, resolverContext.getFieldQualifier());
		}
	}

}
