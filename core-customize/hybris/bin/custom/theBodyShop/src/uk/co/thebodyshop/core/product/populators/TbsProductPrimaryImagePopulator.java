/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPrimaryImagePopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.core.helpers.ProductBadgesDataHelper;

/**
 * @author Marcin
 */
public class TbsProductPrimaryImagePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends ProductPrimaryImagePopulator<SOURCE, TARGET>
{
	private ProductBadgesDataHelper productBadgesDataHelper;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final MediaContainerModel primaryImageMediaContainer = getPrimaryImageMediaContainer(productModel);
		if (primaryImageMediaContainer != null)
		{
			final List<ImageData> imageList = new ArrayList<>();

			// Use the first container as the primary image
			addImagesInFormats(primaryImageMediaContainer, ImageDataType.PRIMARY, 0, imageList);

			for (final ImageData imageData : imageList)
			{
				if (imageData.getAltText() == null)
				{
					imageData.setAltText(productModel.getName());
				}
				getProductBadgesDataHelper().appendProductBadgesParameters(productModel, imageData);
			}
			productData.setImages(imageList);
		}
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
	public void setProductBadgesDataHelper(ProductBadgesDataHelper productBadgesDataHelper)
	{
		this.productBadgesDataHelper = productBadgesDataHelper;
	}
}
