/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import uk.co.thebodyshop.core.helpers.VariantDataHelper;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Marcin
 */
public class VariantSizeValueProvider extends AbstractValueResolver<TbsVariantProductModel, Object, Object>
{
	private VariantDataHelper variantDataHelper;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final TbsVariantProductModel model, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{
		document.addField(indexedProperty, getPropertyValue(model), resolverContext.getFieldQualifier());
	}

	protected Object getPropertyValue(final TbsVariantProductModel model)
	{
		return getVariantDataHelper().getFormattedSizeValue(model);
	}

	protected VariantDataHelper getVariantDataHelper()
	{
		return variantDataHelper;
	}

	public void setVariantDataHelper(final VariantDataHelper variantDataHelper)
	{
		this.variantDataHelper = variantDataHelper;
	}
}
