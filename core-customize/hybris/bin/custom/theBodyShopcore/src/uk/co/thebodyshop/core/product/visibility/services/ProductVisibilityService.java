/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.visibility.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;

/**
 * @author Marcin - Service checks @ProductModel visibility for specific customer
 */
public interface ProductVisibilityService
{
	/**
	 * Returns product visibility information for specific product code and catalogVersion
	 *
	 * @param productCode
	 * @param catalogVersion
	 * @return @ProductVisibilityData
	 */
	 ProductVisibilityData getVisibiltyInfo(final String productCode, final CatalogVersionModel catalogVersion);
}
