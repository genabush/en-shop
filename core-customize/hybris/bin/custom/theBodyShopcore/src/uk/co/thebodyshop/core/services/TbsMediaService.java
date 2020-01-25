/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;

public interface TbsMediaService extends MediaService {

    MediaModel getOrCreateImage(String code, CatalogVersionModel catalogVersion);

    boolean mediaCodeContainsAny(MediaModel media, String[] valueArr);

    MediaFolderModel getOrCreateMediaFolder(String folderCode);

    MediaModel getImage(MediaContainerModel mediaContainer, String codeContains);

    MediaFormatModel getOrCreateMediaFormat(String qualifier);
}
