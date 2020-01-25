/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.impl.DefaultMediaService;
import de.hybris.platform.servicelayer.media.impl.MediaDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import uk.co.thebodyshop.core.services.TbsMediaService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DefaultTbsMediaService extends DefaultMediaService implements TbsMediaService {

    public static final String QUALIFIER_MUST_NOT_BE_NULL = "qualifier must not be null";
    //unfortunately neccessary, because the OOTB MediaService declared its MediaDao as PRIVATE with no getter
    private MediaDao tbsMediaDao;

    public DefaultTbsMediaService(MediaDao tbsMediaDao) {
        this.tbsMediaDao = tbsMediaDao;
    }

    @Override
    public MediaModel getOrCreateImage(String code, CatalogVersionModel catalogVersion) {
        MediaModel result = null;
        List<MediaModel> images = getTbsMediaDao().findMediaByCode(catalogVersion, code);
        if(CollectionUtils.isEmpty(images)) {
            result = createAndSaveImage(code, catalogVersion);
        }
        else if(images.size() > 1) {
            removeDuplicateMedias(code, catalogVersion);
            result = images.get(0);
        }
        else {
            result = images.get(0);
        }
        return result;
    }

    private MediaModel createAndSaveImage(String code, CatalogVersionModel catalogVersion) {
        MediaModel image = getModelService().create(MediaModel.class);
        image.setCode(code);
        image.setCatalogVersion(catalogVersion);
        getModelService().save(image);
        return image;
    }

    private void removeDuplicateMedias(String code, CatalogVersionModel catalogVersion) {
        ServicesUtil.validateParameterNotNull(code, QUALIFIER_MUST_NOT_BE_NULL);
        ServicesUtil.validateParameterNotNull(catalogVersion, "Catalog version must not be null");
        List<MediaModel> medias = getTbsMediaDao().findMediaByCode(catalogVersion, code);
        if(medias != null && medias.size() > 1) {
            List<MediaModel> mediasToRemove = medias.subList(1, medias.size());
            getModelService().removeAll(mediasToRemove);
        }
    }

    @Override
    public boolean mediaCodeContainsAny(MediaModel media, String[] valueArr) {
        boolean result = false;
        if(media != null && valueArr != null) {
            for(String value : valueArr) {
                if(StringUtils.contains(media.getCode(), value)) {
                    result = true;
                }
            }
        }
        return result;
    }

    @Override
    public MediaFolderModel getOrCreateMediaFolder(String folderCode)
    {
        MediaFolderModel resultFolder = null;
        List<MediaFolderModel> mediaFolders = getTbsMediaDao().findMediaFolderByQualifier(folderCode);
        if(CollectionUtils.isEmpty(mediaFolders)) {
            resultFolder = createAndSaveMediaFolder(folderCode);
        }
        else if(mediaFolders.size() > 1) {
            removeDuplicateMediaFolders(folderCode);
            resultFolder = mediaFolders.get(0);
        }
        else {
            resultFolder = mediaFolders.get(0);
        }
        return resultFolder;

    }

    private MediaFolderModel createAndSaveMediaFolder(String folderCode) {
        MediaFolderModel mediaFolder = getModelService().create(MediaFolderModel.class);
        mediaFolder.setQualifier(folderCode);
        mediaFolder.setPath(folderCode);
        getModelService().save(mediaFolder);
        return mediaFolder;
    }

    private void removeDuplicateMediaFolders(String qualifier) {
        ServicesUtil.validateParameterNotNull(qualifier, QUALIFIER_MUST_NOT_BE_NULL);
        List<MediaFolderModel> mediaFolders = getTbsMediaDao().findMediaFolderByQualifier(qualifier);
        if(mediaFolders != null && mediaFolders.size() > 1) {
            List<MediaFolderModel> mediaFoldersToDelete = mediaFolders.subList(1, mediaFolders.size());
            getModelService().removeAll(mediaFoldersToDelete);
        }
    }

    @Override
    public MediaModel getImage(MediaContainerModel mediaContainer, String codeContains) {
        MediaModel result = null;
        Collection<MediaModel> images = mediaContainer.getMedias();
        if(images != null) {
            Optional<MediaModel> imageOptional = images.stream().filter(image -> StringUtils.contains(image.getCode(), codeContains)).findFirst();
            if(imageOptional.isPresent()) {
                result = imageOptional.get();
            }
        }
        return result;
    }

    @Override
    public MediaFormatModel getOrCreateMediaFormat(String qualifier) {
        MediaFormatModel resultFormat = null;
        List<MediaFormatModel> mediaFormats = getTbsMediaDao().findMediaFormatByQualifier(qualifier);
        if(CollectionUtils.isEmpty(mediaFormats)) {
            resultFormat = createAndSaveMediaFormat(qualifier);
        }
        else if(mediaFormats.size() > 1) {
            removeDuplicateMediaFormats(qualifier);
            resultFormat = mediaFormats.get(0);
        }
        else {
            resultFormat = mediaFormats.get(0);
        }
        return resultFormat;
    }


    private MediaFormatModel createAndSaveMediaFormat(String qualifier) {
        ServicesUtil.validateParameterNotNull(qualifier, QUALIFIER_MUST_NOT_BE_NULL);
        MediaFormatModel mediaFormat = getModelService().create(MediaFormatModel.class);
        mediaFormat.setQualifier(qualifier);
        getModelService().save(mediaFormat);
        return mediaFormat;
    }


    private void removeDuplicateMediaFormats(String qualifier) {
        ServicesUtil.validateParameterNotNull(qualifier, QUALIFIER_MUST_NOT_BE_NULL);
        List<MediaFormatModel> mediaFormats = getTbsMediaDao().findMediaFormatByQualifier(qualifier);
        if(mediaFormats != null && mediaFormats.size() > 1) {
            List<MediaFormatModel> mediaFormatsToRemove = mediaFormats.subList(1, mediaFormats.size());
            getModelService().removeAll(mediaFormatsToRemove);
        }
    }

    protected MediaDao getTbsMediaDao() {
        return tbsMediaDao;
    }
}
