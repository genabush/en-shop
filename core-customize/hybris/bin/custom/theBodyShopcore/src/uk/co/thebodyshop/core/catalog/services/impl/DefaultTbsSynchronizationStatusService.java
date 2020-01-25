/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.catalog.services.impl;

import java.util.List;
import java.util.Objects;

import de.hybris.platform.catalog.enums.SyncItemStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.DefaultSynchronizationStatusService;
import de.hybris.platform.catalog.synchronization.SyncItemInfo;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

/**
 * @author Marcin
 */
public class DefaultTbsSynchronizationStatusService extends DefaultSynchronizationStatusService
{

	@Override
	public boolean matchesSyncStatus(final List<ItemModel> givenItems, final List<SyncItemJobModel> syncItemJobs, final SyncItemStatus syncStatus)
	{

		ServicesUtil.validateParameterNotNullStandardMessage("givenItems", givenItems);
		ServicesUtil.validateParameterNotNullStandardMessage("syncItemJobs", syncItemJobs);
		ServicesUtil.validateParameterNotNullStandardMessage("syncStatus", syncStatus);

		for (final ItemModel theItem : givenItems)
		{
			for (final SyncItemJobModel theJob : syncItemJobs)
			{
				if (isSameCatalogSyncStatus(theItem, theJob))
				{
					final SyncItemInfo syncItemInfo = getSyncInfo(theItem, theJob);
					if (syncItemInfo != null && !syncStatus.equals(syncItemInfo.getSyncStatus()))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean isSameCatalogSyncStatus(final ItemModel theItem, final SyncItemJobModel theJob) {
		final CatalogVersionModel catalogVersion = getCatalogVersionForItem(theItem);
		if (Objects.nonNull(catalogVersion) && (Objects.nonNull(theJob.getSourceVersion())))
		{
			return catalogVersion.getCatalog().equals(theJob.getSourceVersion().getCatalog()) && catalogVersion.getCatalog().equals(theJob.getTargetVersion().getCatalog());
		}
		return false;
	}
}
