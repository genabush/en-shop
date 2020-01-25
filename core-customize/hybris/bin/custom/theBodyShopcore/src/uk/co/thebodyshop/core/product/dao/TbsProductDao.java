/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.dao;

import java.util.Date;
import java.util.List;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author prateek.goel
 */
public interface TbsProductDao
{
	List<TbsVariantProductModel> fetchRecentlyCreatedVariantProducts(CatalogVersionModel catalogVersion, Date lastSuccessfulTime);

	List<TbsBaseProductModel> fetchRecentlyCreatedBaseProducts(CatalogVersionModel catalogVersion, Date lastSuccessfulTime);
}
