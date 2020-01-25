/*
 * Copyright (c)
 * 2019. THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.Objects;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import uk.co.thebodyshop.core.enums.TbsBaseType;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Jagadeesh
 */
public class ProductColourValueResolver extends AbstractValueResolver<ProductModel, Object, Object>
{

	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext, final IndexedProperty indexedProperty, final ProductModel productModel, final ValueResolverContext<Object, Object> valueResolverContext)
			throws FieldValueProviderException
	{
		if (productModel instanceof TbsVariantProductModel)
		{
			final TbsVariantProductModel tbsVariantProduct = (TbsVariantProductModel) productModel;
			if (TbsBaseType.COLOUR.equals(((TbsBaseProductModel)tbsVariantProduct.getBaseProduct()).getType()) && Objects.nonNull(tbsVariantProduct.getColour()))
			{
				inputDocument.addField(indexedProperty, tbsVariantProduct.getColour().getHexCode());
			}
		}
	}
}
