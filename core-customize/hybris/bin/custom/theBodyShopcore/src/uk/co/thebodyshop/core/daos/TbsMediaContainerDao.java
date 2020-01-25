/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.media.dao.MediaContainerDao;

import java.util.List;

public interface TbsMediaContainerDao extends MediaContainerDao {

    List<MediaContainerModel> findMediaContainers(String qualifier, CatalogVersionModel catalogVersion);

}
