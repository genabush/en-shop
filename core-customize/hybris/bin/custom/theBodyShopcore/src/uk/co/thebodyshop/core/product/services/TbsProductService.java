/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.services;

import java.util.Date;
import java.util.List;

import de.hybris.platform.catalog.model.CatalogVersionModel;

/**
 * @author prateek.goel
 */
public interface TbsProductService
{
	List<String> fetchRecentlyCreatedVariantProductCodes(CatalogVersionModel catalogVersion, Date lastSuccessfulExecutionTime);

	List<String> fetchRecentlyCreatedBaseProductCodes(CatalogVersionModel catalogVersion, Date lastSuccessfulExecutionTime);
}
