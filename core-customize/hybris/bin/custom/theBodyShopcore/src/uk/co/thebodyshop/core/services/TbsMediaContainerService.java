/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.media.MediaContainerService;

import java.util.Collection;

public interface TbsMediaContainerService extends MediaContainerService {

    void deleteMediaContainersWithContent(Collection<MediaContainerModel> mediaContainers);

    MediaContainerModel getOrCreateMediaContainer(String qualifier, CatalogVersionModel catalogVersion);

    MediaContainerModel getMediaContainer(String qualifier, CatalogVersionModel catalogVersion);

}
