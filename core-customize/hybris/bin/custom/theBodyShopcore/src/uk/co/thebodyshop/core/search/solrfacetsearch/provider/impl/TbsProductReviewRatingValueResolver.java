/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

public class TbsProductReviewRatingValueResolver extends AbstractValueResolver<ProductModel, Object, Object>
{

	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext, final IndexedProperty indexedProperty, final ProductModel product, final ValueResolverContext<Object, Object> valueResolverContext)
			throws FieldValueProviderException
	{
		if (product instanceof TbsBaseProductModel)
		{
			final TbsBaseProductModel tbsBaseProduct = (TbsBaseProductModel) product;
			addReviewRating(inputDocument, indexedProperty, tbsBaseProduct);
		}
		else if (product instanceof TbsVariantProductModel)
		{
			final TbsVariantProductModel tbsVariantProduct = (TbsVariantProductModel) product;
			addReviewRating(inputDocument, indexedProperty, tbsVariantProduct.getBaseProduct());
		}
	}

	private void addReviewRating(final InputDocument inputDocument, final IndexedProperty indexedProperty, final ProductModel product) throws FieldValueProviderException
	{
		if (product.getReviewRating() != null)
		{
			// ratingRange
			if (indexedProperty.isFacet() && indexedProperty.getValueRangeSets() != null)
			{
				final Collection<ValueRangeSet> valueRangeSets = indexedProperty.getValueRangeSets().values();
				for (final ValueRangeSet valueRangeSet : valueRangeSets)
				{
					inputDocument.addField(indexedProperty, product.getReviewRating(), valueRangeSet.getQualifier());
				}
			}
			// reviewAvgRating
			else
			{
				inputDocument.addField(indexedProperty, product.getReviewRating());
			}
		}
	}

}
