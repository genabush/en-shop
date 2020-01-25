/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.suppliers;

import java.util.Optional;
import java.util.function.Supplier;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author vasanthramprakasam
 */
public class ActiveProductCatalogVersionSupplier implements Supplier<Optional<CatalogVersionModel>>
{

	private final BaseSiteService baseSiteService;

	public ActiveProductCatalogVersionSupplier(BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Override
	public Optional<CatalogVersionModel> get()
	{
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		if (null != baseSite && CollectionUtils.isNotEmpty(baseSite.getStores()))
		{
			final BaseStoreModel baseStore = baseSite.getStores().iterator().next();
			if (CollectionUtils.isNotEmpty(baseStore.getCatalogs()))
			{
				return baseStore.getCatalogs().stream().findFirst().map(CatalogModel::getActiveCatalogVersion);
			}
		}
		return Optional.empty();
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}
}
