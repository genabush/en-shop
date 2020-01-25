/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;

/**
 * @author Balakirshna
 */
public interface MarkDownPriceRowService
{
	MarkDownPriceRowModel getActiveMarkDownPrice(ProductModel product);
}
