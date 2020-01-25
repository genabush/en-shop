/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helpers;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.product.ProductModel;
import uk.co.thebodyshop.core.product.data.TbsImageData;

/**
 * @author Marcin
 */
public interface ProductBadgesDataHelper
{
	void appendProductBadgesParameters(final ProductModel productModel, final ImageData imageData);

	String getProductBagesUrl(final ProductModel productModel, final String mediaUrl);

	void appendProductBadgesParameters(final ProductModel productModel, final TbsImageData tbsImageData);
}
