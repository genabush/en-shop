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

/**
 * @author Marcin
 */
public class ProductPreviewDescriptionValueProvider extends AbstractValueResolver<ProductModel, Object, Object>
{
	private static final String HTML_TAG_REGEX = "<.*?>";

	private static final String PREVIEW_DESCRIPTION_ENDING = "...";

	private int descriptionMaxLength;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext, final IndexedProperty indexedProperty, final ProductModel model, final ValueResolverContext<Object, Object> resolverContext)
			throws FieldValueProviderException
	{
		document.addField(indexedProperty, getPropertyValue(model), resolverContext.getFieldQualifier());
	}

	protected Object getPropertyValue(final ProductModel model)
	{
		return trimDescription(model.getDescription());
	}

	private String trimDescription(String description)
	{
		if (StringUtils.isNotEmpty(description))
		{
			description = description.replaceAll(HTML_TAG_REGEX, "");
			if (description.length() <= getDescriptionMaxLength())
			{
				return description;
			}

			final int pos = description.lastIndexOf(" ", getDescriptionMaxLength() - 3);
			if (pos < 0)
			{
				return description.substring(0, getDescriptionMaxLength());
			}
			return description.substring(0, pos) + PREVIEW_DESCRIPTION_ENDING;
		}
		return StringUtils.EMPTY;
	}

	/**
	 * @return the descriptionMaxLength
	 */
	public int getDescriptionMaxLength()
	{
		return descriptionMaxLength;
	}

	/**
	 * @param descriptionMaxLength
	 *          the descriptionMaxLength to set
	 */
	public void setDescriptionMaxLength(final int descriptionMaxLength)
	{
		this.descriptionMaxLength = descriptionMaxLength;
	}

}
