/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.context;

import de.hybris.platform.catalog.model.CatalogVersionModel;

/**
 * @author vasanthramprakasam
 */
public interface ProductVisibilityContextExtractor
{
	 ProductVisibilityContext extractContext(String productCode, CatalogVersionModel catalogVersion);
}
