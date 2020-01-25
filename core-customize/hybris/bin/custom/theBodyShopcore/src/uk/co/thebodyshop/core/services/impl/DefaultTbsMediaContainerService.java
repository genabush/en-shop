/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.impl.DefaultMediaContainerService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.collections.CollectionUtils;
import uk.co.thebodyshop.core.daos.TbsMediaContainerDao;
import uk.co.thebodyshop.core.services.TbsMediaContainerService;

import java.util.Collection;
import java.util.List;

public class DefaultTbsMediaContainerService extends DefaultMediaContainerService implements TbsMediaContainerService {

    private static final String QUALIFIER_CANNOT_BE_NULL = "Qualifier cannot be null";
    private static final String CATALOG_VERSION_CANNOT_BE_NULL = "Catalog version cannot be null";
    private TbsMediaContainerDao tbsMediaContainerDao;

    public DefaultTbsMediaContainerService(TbsMediaContainerDao tbsMediaContainerDao) {
        this.tbsMediaContainerDao = tbsMediaContainerDao;
    }

    @Override
    public MediaContainerModel getOrCreateMediaContainer(String qualifier, CatalogVersionModel catalogVersion) {
        MediaContainerModel resultMediaContainer = null;
        List<MediaContainerModel> mediaContainers = getTbsMediaContainerDao().findMediaContainers(qualifier, catalogVersion);
        if(CollectionUtils.isEmpty(mediaContainers)) {
            resultMediaContainer = createAndSaveMediaContainer(qualifier, catalogVersion);
        }
        else if(mediaContainers.size() > 1) {
            removeDuplicateMediaContainers(qualifier, catalogVersion);
            resultMediaContainer = mediaContainers.get(0);
        }
        else {
            resultMediaContainer = mediaContainers.get(0);
        }
        return resultMediaContainer;
    }

    @Override
    public MediaContainerModel getMediaContainer(String qualifier, CatalogVersionModel catalogVersion) {
        ServicesUtil.validateParameterNotNull(qualifier, QUALIFIER_CANNOT_BE_NULL);
        ServicesUtil.validateParameterNotNull(catalogVersion, CATALOG_VERSION_CANNOT_BE_NULL);
        List<MediaContainerModel> mediaContainers = getTbsMediaContainerDao().findMediaContainers(qualifier, catalogVersion);
        ServicesUtil.validateIfSingleResult(mediaContainers,
                "No result found for qualifier " + qualifier + " and catalog version " + catalogVersion.getCatalog().getId() + ":" + catalogVersion.getVersion(),
                "More than one result found for qualifier " + qualifier + " and catalog version " + catalogVersion.getCatalog().getId() + ":" + catalogVersion.getVersion());
        return mediaContainers.get(0);
    }

    @Override
    public void deleteMediaContainersWithContent(Collection<MediaContainerModel> mediaContainers) {
        if(mediaContainers != null) {
            for(MediaContainerModel mediaContainer : mediaContainers) {
                Collection<MediaModel> medias = mediaContainer.getMedias();
                if(medias != null) {
                    getModelService().removeAll(medias);
                }
            }
            getModelService().removeAll(mediaContainers);
        }
    }

    private MediaContainerModel createAndSaveMediaContainer(String qualifier, CatalogVersionModel catalogVersion) {
        ServicesUtil.validateParameterNotNull(qualifier, QUALIFIER_CANNOT_BE_NULL);
        ServicesUtil.validateParameterNotNull(catalogVersion, CATALOG_VERSION_CANNOT_BE_NULL);
        MediaContainerModel mediaContainer = getModelService().create(MediaContainerModel.class);
        mediaContainer.setQualifier(qualifier);
        mediaContainer.setCatalogVersion(catalogVersion);
        getModelService().save(mediaContainer);
        return mediaContainer;
    }

    private void removeDuplicateMediaContainers(String qualifier, CatalogVersionModel catalogVersion) {
        ServicesUtil.validateParameterNotNull(qualifier, QUALIFIER_CANNOT_BE_NULL);
        ServicesUtil.validateParameterNotNull(catalogVersion, CATALOG_VERSION_CANNOT_BE_NULL);
        List<MediaContainerModel> mediaContainers = getTbsMediaContainerDao().findMediaContainers(qualifier, catalogVersion);
        if(mediaContainers != null && mediaContainers.size() > 1) {
            List<MediaContainerModel> mediaContainersToDelete = mediaContainers.subList(1, mediaContainers.size());
            getModelService().removeAll(mediaContainersToDelete);
        }
    }

    protected TbsMediaContainerDao getTbsMediaContainerDao() {
        return tbsMediaContainerDao;
    }
}
