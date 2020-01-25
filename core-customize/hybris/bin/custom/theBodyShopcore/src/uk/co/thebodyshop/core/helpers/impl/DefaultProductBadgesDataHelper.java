/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helpers.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.helpers.ProductBadgesDataHelper;
import uk.co.thebodyshop.core.model.BadgePositionModel;
import uk.co.thebodyshop.core.model.ProductBadgeModel;
import uk.co.thebodyshop.core.product.data.TbsImageData;

/**
 * @author Marcin
 */
public class DefaultProductBadgesDataHelper implements ProductBadgesDataHelper
{
	private static final String COMMA_REGEX = ",";
	private static final String EQ_CHAR = "=";
	private static final String HAS_PARAM_REGEX = ".*\\?.*";
	private static final String PARAM_CHAR = "?";
	private static final String AMP_CHAR = "&";
	private static final String DEFAULT_BADGE_MEDIA_FORMAT = "zoom";
	private static final String OPACITY_PARAM = "opacity";
	private static final String RIGHT_POSITION_PARAM = "right";
	private static final String LEFT_POSITION_PARAM = "left";
	private static final String BOTTOM_POSITION_PARAM = "bottom";
	private static final String TOP_POSITION_PARAM = "top";
	private static final String WIDTH_POSITION_PARAM = "width";
	private static final String HEIGHT_POSITION_PARAM = "height";
	private static final String ANCHOR_PARAM = "anchor";
	private static final String LAYER_PARAMS_END = "]";
	private static final String LAYER_PARAMS_START = "=[";
	private static final String SRC_PARAM = "src=";
	private static final String LAYER_PARAM = "layer";

	private final String mediaFormatForBadges;

	@Override
	public void appendProductBadgesParameters(final ProductModel productModel, final ImageData imageData)
	{
		if (getAllowedMediaFromatsAsList().contains(imageData.getFormat()))
		{
			final String productBadgesParams = getProductBagesUrl(productModel, imageData.getUrl());
			if (StringUtils.isNotEmpty(productBadgesParams))
			{
				final String imageUrl = imageData.getUrl();
				imageData.setUrl(imageUrl + productBadgesParams);
			}
		}
	}

	@Override
	public void appendProductBadgesParameters(final ProductModel productModel, final TbsImageData tbsImageData)
	{
		if (getAllowedMediaFromatsAsList().contains(tbsImageData.getFormat()))
		{
			final String productBadgesParams = getProductBagesUrl(productModel, tbsImageData.getUrl());
			if (StringUtils.isNotEmpty(productBadgesParams))
			{
				final String imageUrl = tbsImageData.getUrl();
				tbsImageData.setUrl(imageUrl + productBadgesParams);
			}
		}
	}

	@Override
	public String getProductBagesUrl(final ProductModel productModel, final String mediaUrl)
	{
		final StringBuilder productBadgesParams = new StringBuilder();
		final Collection<ProductBadgeModel> productBadges = getProductBadges(productModel);
		if (CollectionUtils.isNotEmpty(productBadges)) {
			int layerCounter = 1;
			for (final ProductBadgeModel productBadge : productBadges)
			{
				final String badgeParameters = getProductBadgeParameters(productBadge, layerCounter);
				if (StringUtils.isNotEmpty(badgeParameters))
				{
					setupInitialProductBadgesParameters(mediaUrl, productBadgesParams);
					productBadgesParams.append(badgeParameters);
				}
				layerCounter++;
			}
		}
		return productBadgesParams.toString();
	}

	/**
	 * @param mediaUrl
	 * @param productBadgesParams
	 */
	private void setupInitialProductBadgesParameters(final String mediaUrl, final StringBuilder productBadgesParams)
	{
		if (StringUtils.isEmpty(productBadgesParams.toString()))
		{
			if (hasParameters(mediaUrl))
			{
				productBadgesParams.append(AMP_CHAR);
			}
			else
			{
				productBadgesParams.append(PARAM_CHAR);
			}
		}
		else
		{
			productBadgesParams.append(AMP_CHAR);
		}
	}

	private List<String> getAllowedMediaFromatsAsList()
	{
		if (StringUtils.isNotEmpty(getMediaFormatForBadges()))
		{
			return Arrays.asList(getMediaFormatForBadges().split(COMMA_REGEX));
		}
		return Arrays.asList(DEFAULT_BADGE_MEDIA_FORMAT);
	}

	private Collection<ProductBadgeModel> getProductBadges(final ProductModel productModel)
	{
		Collection<ProductBadgeModel> productBadges = productModel.getProductBadges();
		if (CollectionUtils.isEmpty(productBadges))
		{
			if (productModel instanceof VariantProductModel)
			{
				if (Objects.nonNull(((VariantProductModel) productModel).getBaseProduct()))
				{
					productBadges = ((VariantProductModel) productModel).getBaseProduct().getProductBadges();
				}
			}
		}
		return productBadges;
	}

	private String getProductBadgeParameters(final ProductBadgeModel productBadge, final int layer)
	{
		final StringBuilder productBadgeParams = new StringBuilder();
		if (StringUtils.isNotEmpty(productBadge.getUrl()))
		{
			productBadgeParams.append(LAYER_PARAM + layer + LAYER_PARAMS_START);
			productBadgeParams.append(SRC_PARAM + productBadge.getUrl());
			populateBadgePostion(productBadgeParams, productBadge.getPosition());
			productBadgeParams.append(LAYER_PARAMS_END);
		}
		return productBadgeParams.toString();
	}

	private void populateBadgePostion(final StringBuilder productBadgeParams, final BadgePositionModel badgePosition)
	{
		if (Objects.nonNull(badgePosition))
		{
			if (StringUtils.isNotEmpty(badgePosition.getAnchor()))
			{
				productBadgeParams.append(getBadgePositionParam(ANCHOR_PARAM, badgePosition.getAnchor()));
			}
			if (StringUtils.isNotEmpty(badgePosition.getTop()))
			{
				productBadgeParams.append(getBadgePositionParam(TOP_POSITION_PARAM, badgePosition.getTop()));
			}
			if (StringUtils.isNotEmpty(badgePosition.getBottom()))
			{
				productBadgeParams.append(getBadgePositionParam(BOTTOM_POSITION_PARAM, badgePosition.getBottom()));
			}
			if (StringUtils.isNotEmpty(badgePosition.getLeft()))
			{
				productBadgeParams.append(getBadgePositionParam(LEFT_POSITION_PARAM, badgePosition.getLeft()));
			}
			if (StringUtils.isNotEmpty(badgePosition.getRight()))
			{
				productBadgeParams.append(getBadgePositionParam(RIGHT_POSITION_PARAM, badgePosition.getRight()));
			}
			if (StringUtils.isNotEmpty(badgePosition.getOpacity()))
			{
				productBadgeParams.append(getBadgePositionParam(OPACITY_PARAM, badgePosition.getOpacity()));
			}
			if(StringUtils.isNotEmpty(badgePosition.getWidth()))
			{
				productBadgeParams.append(getBadgePositionParam(WIDTH_POSITION_PARAM, badgePosition.getWidth()));
			}
			if(StringUtils.isNotEmpty(badgePosition.getHeight()))
			{
				productBadgeParams.append(getBadgePositionParam(HEIGHT_POSITION_PARAM, badgePosition.getHeight()));
			}
		}
	}

	private String getBadgePositionParam(final String position, final String positionValue)
	{
		return AMP_CHAR + position + EQ_CHAR + positionValue;
	}

	private boolean hasParameters(final String url)
	{
		return url.matches(HAS_PARAM_REGEX);
	}

	@Autowired
	public DefaultProductBadgesDataHelper(final String mediaFormatForBadges)
	{
		this.mediaFormatForBadges = mediaFormatForBadges;
	}

	/**
	 * @return the mediaFormatForBadges
	 */
	protected String getMediaFormatForBadges()
	{
		return mediaFormatForBadges;
	}

}
