/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.commercefacades.product.converters.populator.ProductGalleryImagesPopulator;
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
public class TbsProductGalleryImagesPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends ProductGalleryImagesPopulator<SOURCE, TARGET>
{
	private ProductBadgesDataHelper productBadgesDataHelper;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		// Collect the media containers on the product
		final List<MediaContainerModel> mediaContainers = new ArrayList<>();
		collectMediaContainers(productModel, mediaContainers);

		if (!mediaContainers.isEmpty())
		{
			final List<ImageData> imageList = new ArrayList<>();

			// fill our image list with the product's existing images
			if (productData.getImages() != null)
			{
				imageList.addAll(productData.getImages());
			}

			// Use all the images as gallery images
			int galleryIndex = 0;
			for (final MediaContainerModel mediaContainer : mediaContainers)
			{
				addImagesInFormats(mediaContainer, ImageDataType.GALLERY, galleryIndex++, imageList);
			}

			for (final ImageData imageData : imageList)
			{
				if (imageData.getAltText() == null)
				{
					imageData.setAltText(productModel.getName());
				}
				getProductBadgesDataHelper().appendProductBadgesParameters(productModel, imageData);
			}
			// Overwrite the existing list of images
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
