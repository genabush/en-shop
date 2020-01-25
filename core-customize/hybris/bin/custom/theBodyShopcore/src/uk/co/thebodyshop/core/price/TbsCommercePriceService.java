/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.price;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;

/**
 * @author Jagadeesh
 */
public interface TbsCommercePriceService extends CommercePriceService
{
	public PriceInformation getBasePriceForProduct(final ProductModel product);
}
