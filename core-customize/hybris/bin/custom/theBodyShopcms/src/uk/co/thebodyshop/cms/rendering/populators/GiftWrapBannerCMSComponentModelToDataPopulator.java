/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.cms.model.components.GiftWrapBannerCMSComponentModel;

/**
 * @author Jagadeesh
 */
public class GiftWrapBannerCMSComponentModelToDataPopulator implements Populator<CMSItemModel, Map<String, Object>>
{

	private static final String GIFT_WRAP_PRODUCT_URL = "giftWrapServiceImage";
	private static final String GIFT_WRAP_IMAGE_URL = "giftImage";
	private static final String GIFT_WRAP_SERVICE_MESSAGE = "giftWrapServiceMessage";

	private final Predicate<CMSItemModel> cmsGiftWrapBannerCMSComponentTypePredicate;

	@Override
	public void populate(final CMSItemModel cmsItemModel, final Map<String, Object> properties) throws ConversionException
	{
		if (getCmsGiftWrapBannerCMSComponentTypePredicate().test(cmsItemModel) && cmsItemModel instanceof GiftWrapBannerCMSComponentModel)
		{

			final GiftWrapBannerCMSComponentModel giftWrapBannerCMSComponentModel = (GiftWrapBannerCMSComponentModel) cmsItemModel;
			if (StringUtils.isNotEmpty(giftWrapBannerCMSComponentModel.getGiftImage()))
			{
				properties.put(GIFT_WRAP_IMAGE_URL, giftWrapBannerCMSComponentModel.getGiftImage());
			}
			if (StringUtils.isNotEmpty(giftWrapBannerCMSComponentModel.getGiftWrapServiceImage()))
			{
				properties.put(GIFT_WRAP_PRODUCT_URL, giftWrapBannerCMSComponentModel.getGiftWrapServiceImage());
			}
			if (StringUtils.isNotEmpty(giftWrapBannerCMSComponentModel.getGiftWrapServiceMessage()))
			{
				properties.put(GIFT_WRAP_SERVICE_MESSAGE, giftWrapBannerCMSComponentModel.getGiftWrapServiceMessage());
			}
		}
	}

	public GiftWrapBannerCMSComponentModelToDataPopulator(final Predicate<CMSItemModel> cmsGiftWrapBannerCMSComponentTypePredicate)
	{
		this.cmsGiftWrapBannerCMSComponentTypePredicate = cmsGiftWrapBannerCMSComponentTypePredicate;
	}

	protected Predicate<CMSItemModel> getCmsGiftWrapBannerCMSComponentTypePredicate()
	{
		return cmsGiftWrapBannerCMSComponentTypePredicate;
	}
}
