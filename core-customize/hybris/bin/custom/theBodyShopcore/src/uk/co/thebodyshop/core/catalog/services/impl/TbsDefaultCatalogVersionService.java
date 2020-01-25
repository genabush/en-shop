/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.catalog.services.impl;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.catalog.impl.DefaultCatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;

/**
 * @author Marcin
 */
public class TbsDefaultCatalogVersionService extends DefaultCatalogVersionService implements TbsCatalogVersionService
{
	private final String stagedCatalogId;
	private final String globalProductCatalogId;
	private final String globalContentCatalogId;
	private final String onlineCatalogId;

	@Override
	public boolean isStagedCatalog(final CatalogVersionModel catalogVersion)
	{
		return null != catalogVersion && getStagedCatalogId().equals(catalogVersion.getVersion());
	}

	@Override
	public boolean isContentCatalog(final CatalogVersionModel catalogVersion)
	{
		return null != catalogVersion && catalogVersion.getCatalog() instanceof ContentCatalogModel;
	}

	@Override
	public boolean isGlobalProductCatalog(final CatalogVersionModel catalogVersion)
	{
		return null != catalogVersion && null != catalogVersion.getCatalog() && getGlobalProductCatalogId().equals(catalogVersion.getCatalog().getId());
	}

	@Override
	public boolean isMarketProductCatalog(final CatalogVersionModel catalogVersion)
	{
		return null != catalogVersion && !isContentCatalog(catalogVersion) && null != catalogVersion.getCatalog() && !getGlobalProductCatalogId().equals(catalogVersion.getCatalog().getId());
	}

	@Override
	public boolean isGlobalContentCatalog(final CatalogVersionModel catalogVersion)
	{
		return null != catalogVersion && null != catalogVersion.getCatalog() && getGlobalContentCatalogId().equals(catalogVersion.getCatalog().getId());
	}

	@Override
	public boolean isMarketContentCatalog(final CatalogVersionModel catalogVersion)
	{
		return null != catalogVersion && isContentCatalog(catalogVersion) && null != catalogVersion.getCatalog() && !getGlobalContentCatalogId().equals(catalogVersion.getCatalog().getId());
	}

	@Override
	public boolean isStagedGlobalProductCatalog(final CatalogVersionModel catalogVersion)
	{
		return isStagedCatalog(catalogVersion) && isGlobalProductCatalog(catalogVersion);
	}

	@Override
	public boolean isStagedGlobalContentCatalog(final CatalogVersionModel catalogVersion)
	{
		return isStagedCatalog(catalogVersion) && isGlobalContentCatalog(catalogVersion);
	}

	@Override
	public CatalogVersionModel getRequestContentCatalog(final Collection<CatalogVersionModel> contentCatalogVersions)
	{
		CatalogVersionModel globalContentCatalogVersion = null;
		if (CollectionUtils.isNotEmpty(contentCatalogVersions))
		{
			for (final CatalogVersionModel catalogVersion : contentCatalogVersions)
			{
				if (isGlobalContentCatalog(catalogVersion))
				{
					globalContentCatalogVersion = catalogVersion;
				}
				else
				{
					return catalogVersion;
				}
			}
		}
		return globalContentCatalogVersion;
	}

	@Override
	public CatalogVersionModel getStagedGlobalProductCatalog()
	{
		return this.getCatalogVersion(getGlobalProductCatalogId(), getStagedCatalogId());
	}

	@Override
	public CatalogVersionModel getStagedMarketProductCatalog(final String marketCatalogId)
	{
		return this.getCatalogVersion(marketCatalogId, getStagedCatalogId());
	}

	@Override
	public CatalogVersionModel getOnlineMarketProductCatalog(final String marketCatalogId)
	{
		return this.getCatalogVersion(marketCatalogId, getOnlineCatalogId());
	}

	@Autowired
	public TbsDefaultCatalogVersionService(final String stagedCatalogId, final String globalProductCatalogId, final String globalContentCatalogId, final String onlineCatalogId)
	{
		this.stagedCatalogId = stagedCatalogId;
		this.globalProductCatalogId = globalProductCatalogId;
		this.globalContentCatalogId = globalContentCatalogId;
		this.onlineCatalogId = onlineCatalogId;
	}

	protected String getStagedCatalogId()
	{
		return stagedCatalogId;
	}

	protected String getGlobalProductCatalogId()
	{
		return globalProductCatalogId;
	}

	protected String getGlobalContentCatalogId()
	{
		return globalContentCatalogId;
	}

	protected String getOnlineCatalogId()
	{
		return onlineCatalogId;
	}

}
