/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.catalog.services;

import java.util.Collection;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;

/**
 * @author Marcin
 */
public interface TbsCatalogVersionService extends CatalogVersionService
{
	boolean isStagedCatalog(CatalogVersionModel catalogVersion);

	boolean isContentCatalog(CatalogVersionModel catalogVersion);

	boolean isGlobalProductCatalog(CatalogVersionModel catalogVersion);

	boolean isMarketProductCatalog(CatalogVersionModel catalogVersion);

	boolean isGlobalContentCatalog(final CatalogVersionModel catalogVersion);

	boolean isMarketContentCatalog(final CatalogVersionModel catalogVersion);

	boolean isStagedGlobalProductCatalog(CatalogVersionModel catalogVersion);

	boolean isStagedGlobalContentCatalog(final CatalogVersionModel catalogVersion);

	CatalogVersionModel getRequestContentCatalog(final Collection<CatalogVersionModel> contentCatalogVersions);

	CatalogVersionModel getStagedGlobalProductCatalog();

	CatalogVersionModel getStagedMarketProductCatalog(final String marketCatalogId);

	CatalogVersionModel getOnlineMarketProductCatalog(final String marketCatalogId);
}
