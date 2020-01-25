/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ImageValueProvider;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import uk.co.thebodyshop.core.helpers.ProductBadgesDataHelper;

/**
 * @author Marcin
 */
public class TbsImageValueProvider extends ImageValueProvider
{
	private static final Logger LOG = Logger.getLogger(TbsImageValueProvider.class);

	private ProductBadgesDataHelper productBadgesDataHelper;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final MediaFormatModel mediaFormatModel = getMediaService().getFormat(getMediaFormat());
			if (mediaFormatModel != null)
			{
				final MediaModel media = findMedia((ProductModel) model, mediaFormatModel);
				if (media != null)
				{
					final String productBadgesParams = getProductBadgesDataHelper().getProductBagesUrl((ProductModel) model, media.getURL());
					return createFieldValues(indexedProperty, productBadgesParams, media);
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("No [" + mediaFormatModel.getQualifier() + "] image found for product [" + ((ProductModel) model).getCode() + "]");
				}
			}
		}
		return Collections.emptyList();
	}

	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final String productBadgesParams, final MediaModel media)
	{
		return createFieldValues(indexedProperty, getProductMediaUrlWithBadgePartameters(productBadgesParams, media.getURL()));
	}

	private String getProductMediaUrlWithBadgePartameters(final String productBadgesParams, final String url)
	{
		if (StringUtils.isNotEmpty(productBadgesParams))
		{
			return url + productBadgesParams;
		}
		return url;
	}

	/**
	 * @return the productBadgesDataHelper
	 */
	public ProductBadgesDataHelper getProductBadgesDataHelper()
	{
		return productBadgesDataHelper;
	}

	/**
	 * @param productBadgesDataHelper
	 *          the productBadgesDataHelper to set
	 */
	public void setProductBadgesDataHelper(final ProductBadgesDataHelper productBadgesDataHelper)
	{
		this.productBadgesDataHelper = productBadgesDataHelper;
	}
}
