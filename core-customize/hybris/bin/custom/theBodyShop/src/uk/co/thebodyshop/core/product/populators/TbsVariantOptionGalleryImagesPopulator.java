/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.helpers.ProductBadgesDataHelper;
import uk.co.thebodyshop.core.product.data.TbsGalleryImageData;
import uk.co.thebodyshop.core.product.data.TbsImageData;

/**
 * @author Jagadeesh
 */
public class TbsVariantOptionGalleryImagesPopulator implements Populator<ProductModel, VariantOptionData>
{
	private static final String MEDIA_FORMAT_ZOOM = "zoom";

	private static final String MEDIA_FORMAT_PRODUCT = "product";

	private static final String MEDIA_FORMAT_THUMBNAIL = "thumbnail";

	private ModelService modelService;

	private MediaService mediaService;

	private MediaContainerService mediaContainerService;

	private ImageFormatMapping imageFormatMapping;

	private List<String> imageFormats;

	private ProductBadgesDataHelper productBadgesDataHelper;

	@Override
	public void populate(ProductModel source, VariantOptionData target) throws ConversionException
	{
		// Collect the media containers on the product
		final List<MediaContainerModel> mediaContainers = new ArrayList<>();
		collectMediaContainers(source, mediaContainers);

		if (!mediaContainers.isEmpty())
		{
			final List<TbsGalleryImageData> imageList = new ArrayList<>();

			// fill our image list with the product's existing images
			if (target.getImages() != null)
			{
				imageList.addAll(target.getImages());
			}

			// Use all the images as gallery images
			int galleryIndex = 0;
			for (final MediaContainerModel mediaContainer : mediaContainers)
			{
				addImagesInFormats(mediaContainer, ImageDataType.GALLERY, galleryIndex++, imageList, source);
			}

			for (TbsGalleryImageData imageData : imageList)
			{
				final List<TbsImageData> tbsImageList = new ArrayList<>();
				tbsImageList.add(imageData.getProduct());
				tbsImageList.add(imageData.getThumbnail());
				tbsImageList.add(imageData.getZoom());
				for (TbsImageData tbsImageData: tbsImageList) {
					getProductBadgesDataHelper().appendProductBadgesParameters(source, tbsImageData);
				}
			}

			// Overwrite the existing list of images
			target.setImages(imageList);
		}

	}

	protected void collectMediaContainers(final ProductModel productModel, final List<MediaContainerModel> list)
	{
		final List<MediaContainerModel> galleryImages = (List<MediaContainerModel>) getProductAttribute(productModel, ProductModel.GALLERYIMAGES);
		if (galleryImages != null)
		{
			for (final MediaContainerModel galleryImage : galleryImages)
			{
				if (!list.contains(galleryImage))
				{
					list.add(galleryImage);
				}
			}

			if (galleryImages.isEmpty() && productModel instanceof VariantProductModel)
			{
				collectMediaContainers(((VariantProductModel) productModel).getBaseProduct(), list);
			}
		}
	}

	protected Object getProductAttribute(final ProductModel productModel, final String attribute)
	{
		final Object value = getModelService().getAttributeValue(productModel, attribute);
		if (value == null && productModel instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			if (baseProduct != null)
			{
				return getProductAttribute(baseProduct, attribute);
			}
		}
		return value;
	}

	protected void addImagesInFormats(final MediaContainerModel mediaContainer, final ImageDataType imageType, final int galleryIndex, final List<TbsGalleryImageData> list, ProductModel product)
	{
		final TbsGalleryImageData galleryImageData = new TbsGalleryImageData();
		for (final String imageFormat : getImageFormats())
		{
			try
			{
				final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
				if (mediaFormatQualifier != null)
				{
					final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatQualifier);
					if (mediaFormat != null)
					{
						final MediaModel media = getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
						if (media != null)
						{
							final TbsImageData imageData = new TbsImageData();
							imageData.setUrl(media.getURL());
							imageData.setAltText(product.getName());
							imageData.setFormat(imageFormat);
							if (ImageDataType.GALLERY.equals(imageType))
							{
								imageData.setGalleryIndex(Integer.valueOf(galleryIndex));
							}
							if (MEDIA_FORMAT_ZOOM.equals(imageFormat))
							{
								galleryImageData.setZoom(imageData);
							}
							if (MEDIA_FORMAT_THUMBNAIL.equals(imageFormat))
							{
								galleryImageData.setThumbnail(imageData);
							}
							if (MEDIA_FORMAT_PRODUCT.equals(imageFormat))
							{
								galleryImageData.setProduct(imageData);
							}
						}
					}
				}

			}
			catch (final ModelNotFoundException ignore)
			{
				// Ignore
			}
		}
		list.add(galleryImageData);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected List<String> getImageFormats()
	{
		return imageFormats;
	}

	public void setImageFormats(List<String> imageFormats)
	{
		this.imageFormats = imageFormats;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	public void setMediaService(MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected MediaContainerService getMediaContainerService()
	{
		return mediaContainerService;
	}

	public void setMediaContainerService(MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
	}

	public ImageFormatMapping getImageFormatMapping()
	{
		return imageFormatMapping;
	}

	public void setImageFormatMapping(ImageFormatMapping imageFormatMapping)
	{
		this.imageFormatMapping = imageFormatMapping;
	}

	protected ProductBadgesDataHelper getProductBadgesDataHelper() {
		return productBadgesDataHelper;
	}

	public void setProductBadgesDataHelper(ProductBadgesDataHelper productBadgesDataHelper) {
		this.productBadgesDataHelper = productBadgesDataHelper;
	}
}
