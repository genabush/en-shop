/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;
import uk.co.thebodyshop.core.media.amplience.AmplienceMediaSet;
import uk.co.thebodyshop.core.media.amplience.AmplienceMediaSetItem;
import uk.co.thebodyshop.core.services.AmplienceImageSyncService;
import uk.co.thebodyshop.core.services.TbsMediaContainerService;
import uk.co.thebodyshop.core.services.TbsMediaService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultAmplienceImageSyncService implements AmplienceImageSyncService {

    private static final Logger LOG = Logger.getLogger(DefaultAmplienceImageSyncService.class);

    private static final String MEDIA_SYNC_FAILED_INVALID_URL = "Malformatted URL for Ampliance connection for product ::";
    private static final String MEDIA_SYNC_FAILED_NO_MEDIASET_FOUND = "Unable to find mediaset in Ampliance for product ::";
    private static final String MEDIA_SYNC_FAILED_CANT_RETRIEVE_IMAGE = "Not able to retrieve media from Ampliance for product ::";
    private static final String DOUBLE_UNDERSCORE = "__";


    private ConfigurationService configurationService;
    private ModelService modelService;
    private TbsMediaService tbsMediaService;
    private TbsMediaContainerService tbsMediaContainerService;
    private ObjectMapperCreator objectMapperCreator;

    public DefaultAmplienceImageSyncService(ConfigurationService configurationService, ModelService modelService, TbsMediaService tbsMediaService, TbsMediaContainerService tbsMediaContainerService) {
        this(configurationService, modelService, tbsMediaService, tbsMediaContainerService, new ObjectMapperCreator());
    }

    public DefaultAmplienceImageSyncService(ConfigurationService configurationService, ModelService modelService, TbsMediaService tbsMediaService, TbsMediaContainerService tbsMediaContainerService, ObjectMapperCreator objectMapperCreator) {
        this.configurationService = configurationService;
        this.modelService = modelService;
        this.tbsMediaService = tbsMediaService;
        this.tbsMediaContainerService = tbsMediaContainerService;
        this.objectMapperCreator = objectMapperCreator;
    }

    @Override
    public void syncImagesForProduct(ProductModel product, StringBuilder syncMessage) throws MalformedURLException,IOException {
        StringBuilder failedProduct = new StringBuilder();
        AmplienceMediaSet amplienceMediaSet = getMediaSetForProduct(product.getCode(), syncMessage, failedProduct);

        //if sync message is not empty, then issues occurred in retrieving media sets from Amplience.
        //Then skip the rest and leave product as it is
        if(!StringUtils.equals(product.getCode(), failedProduct.toString())) {
            clearPictureThumbnailNormals(product);
            deleteUnneededGalleryImageContainers(amplienceMediaSet, product);
            updateGalleryImagesForProduct(amplienceMediaSet, product);
            setNormals(product);
            setPicture(product);
            setThumbnail(product);
            getModelService().save(product);
        }

    }

    private void clearPictureThumbnailNormals(ProductModel product) {
        product.setPicture(null);
        product.setThumbnail(null);
        product.setNormal(Collections.EMPTY_LIST);
    }

    private AmplienceMediaSet getMediaSetForProduct(final String productCode, final StringBuilder syncMessage, StringBuilder failedProduct)throws MalformedURLException,IOException
    {
        String rootUrl = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.AMPLIENCE_ROOT_URL);
        String urlSuffix = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.AMPLIENCE_MEDIA_URL_SUFFIX);
        String version= TheBodyShopCoreConstants.AMPLIENCE_MEDIA_URL_VERSION + new Date().getTime();
        final String urlString = rootUrl + productCode.toLowerCase() + urlSuffix + version;
        URL url;
        try
        {
            url = new URL(urlString);
            return getObjectMapperCreator().createObjectMapper().readValue(url, AmplienceMediaSet.class);
        }
        catch (final MalformedURLException e)
        {
            LOG.error("Invalid url for retrieving from Amplience: " + urlString, e);
            failedProduct.append(productCode);
            syncMessage.append(MEDIA_SYNC_FAILED_INVALID_URL + productCode).append(System.lineSeparator());
            throw e;
        }
        catch (final FileNotFoundException e)
        {
            syncMessage.append(MEDIA_SYNC_FAILED_NO_MEDIASET_FOUND + productCode).append(System.lineSeparator());
            failedProduct.append(productCode);
            LOG.warn("No mediaset found for product with code: " + productCode + " using url: " + urlString, e);
        }
        catch (final IOException e)
        {
            syncMessage.append(MEDIA_SYNC_FAILED_CANT_RETRIEVE_IMAGE + productCode).append(System.lineSeparator());
            failedProduct.append(productCode);
            LOG.error("Can't retrieve images for product with code: " + productCode, e);
            throw e;
        }

        final AmplienceMediaSet emptySet = new AmplienceMediaSet();
        emptySet.setItems(Collections.emptyList());
        return emptySet;
    }

    private void deleteUnneededGalleryImageContainers(AmplienceMediaSet amplienceMediaSet, ProductModel product) {
        if(product.getGalleryImages() != null) {
            if(CollectionUtils.isEmpty(amplienceMediaSet.getItems())) {
                getTbsMediaContainerService().deleteMediaContainersWithContent(product.getGalleryImages());
            }
            else {
                Collection<MediaContainerModel> galleryImageContainersToDelete = product.getGalleryImages()
                        .stream().filter(mediaContainer -> getMediaContainerIndexByCode(mediaContainer) > amplienceMediaSet.getItems().size())
                        .collect(Collectors.toList());
                getTbsMediaContainerService().deleteMediaContainersWithContent(galleryImageContainersToDelete);
            }
        }
    }

    private int getMediaContainerIndexByCode(MediaContainerModel mediaContainer) {
        int resultIndex = Integer.MAX_VALUE;
        String[] mediaContainerQualifierArr = mediaContainer.getQualifier().split(DOUBLE_UNDERSCORE);

        //expected qualifier format: <productcode>__<index>, for example p003257__1
        if(mediaContainerQualifierArr.length == 2 && mediaContainerQualifierArr[1].matches("\\d+")) {
            resultIndex = Integer.parseInt(mediaContainerQualifierArr[1]);
        }
        return resultIndex;
    }

    private void updateGalleryImagesForProduct(AmplienceMediaSet amplienceMediaSet, ProductModel product) {
        List<AmplienceMediaSetItem> amplienceMediaSetItems = amplienceMediaSet.getItems();
        if(amplienceMediaSetItems != null) {
            List<MediaContainerModel> galleryImageContainers = new ArrayList<>();
            for(int i=1; i <= amplienceMediaSetItems.size(); i++) {
                AmplienceMediaSetItem amplienceMediaSetItem = amplienceMediaSetItems.get(i-1);
                MediaContainerModel mediaContainer = tbsMediaContainerService.getOrCreateMediaContainer(product.getCode() + DOUBLE_UNDERSCORE + i, product.getCatalogVersion());
                updateImagesForMediaContainer(amplienceMediaSetItem, mediaContainer);
                getModelService().save(mediaContainer);
                galleryImageContainers.add(mediaContainer);
            }
            product.setGalleryImages(galleryImageContainers);

        }
    }

    private void updateImagesForMediaContainer(AmplienceMediaSetItem amplienceMediaSetItem, MediaContainerModel mediaContainer) {
        String amplienceMediaFormatsConfigStr = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.AMPLIENCE_IMPORT_MEDIA_FORMATS);
        if(amplienceMediaFormatsConfigStr != null) {
            String[] amplienceMediaFormatsConfigArr = amplienceMediaFormatsConfigStr.split(",");
            if(mediaContainer.getMedias() != null) {
                List<MediaModel> imagesToRemove = mediaContainer.getMedias().stream().filter(media -> !getTbsMediaService().mediaCodeContainsAny(media, amplienceMediaFormatsConfigArr)).collect(Collectors.toList());
                getTbsMediaContainerService().removeMediaFromContainer(mediaContainer, imagesToRemove);
            }
            for(String mediaFormatStr : amplienceMediaFormatsConfigArr) {
                String imageCode = mediaContainer.getQualifier() + DOUBLE_UNDERSCORE + mediaFormatStr;
                MediaModel image = tbsMediaService.getOrCreateImage(imageCode, mediaContainer.getCatalogVersion());
                updateImage(image, mediaFormatStr, amplienceMediaSetItem);
                image.setMediaContainer(mediaContainer);
                getModelService().save(image);
            }
        }
    }

    private void updateImage(MediaModel image, String mediaFormatStr, AmplienceMediaSetItem amplienceMediaSetItem) {
        String defaultMediaFolderCode = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.DEFAULT_MEDIA_FOLDER);
        image.setFolder(tbsMediaService.getOrCreateMediaFolder(defaultMediaFolderCode));
        image.setMediaFormat(tbsMediaService.getOrCreateMediaFormat(mediaFormatStr));
        String imageUrl = getImageUrl(amplienceMediaSetItem, image.getMediaFormat());
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(imageUrl);
        uriComponentsBuilder.scheme("https");
        getTbsMediaService().setUrlForMedia(image, uriComponentsBuilder.toUriString());
        if(amplienceMediaSetItem.getFormat() != null) {
            image.setMime(amplienceMediaSetItem.getFormat().getMime());
        }
    }

    private String getImageUrl(AmplienceMediaSetItem amplienceMediaSetItem, MediaFormatModel mediaFormatModel) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(amplienceMediaSetItem.getSrc());
        if(mediaFormatModel.getAmplienceTemplate() != null) {
            urlBuilder.append(".").append(amplienceMediaSetItem.getFormat().getExtension());
            urlBuilder.append("?").append(mediaFormatModel.getAmplienceTemplate());
        }
        return urlBuilder.toString();
    }

    private void setNormals(ProductModel product) {
        List<MediaModel> normals = new ArrayList<>();
        List<MediaContainerModel> galleryImageContainers = product.getGalleryImages();
        if(galleryImageContainers != null) {
            String normalFormatString = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.AMPLIENCE_IMPORT_MEDIA_FORMAT_NORMAL);
            for(MediaContainerModel galleryImageContainer : galleryImageContainers) {
                MediaModel normalFormattedImage = tbsMediaService.getImage(galleryImageContainer, normalFormatString);
                if(normalFormattedImage != null) {
                    normals.add(normalFormattedImage);
                }
            }
        }
        product.setNormal(normals);
    }

    private void setPicture(ProductModel product) {
        List<MediaContainerModel> galleryImageContainers = product.getGalleryImages();
        if(galleryImageContainers != null && CollectionUtils.isNotEmpty(galleryImageContainers)) {
            String normalFormatString = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.AMPLIENCE_IMPORT_MEDIA_FORMAT_NORMAL);
            MediaContainerModel firstGalleryImageContainer = galleryImageContainers.get(0);
            MediaModel normalFormattedImage = tbsMediaService.getImage(firstGalleryImageContainer, normalFormatString);
            if(normalFormattedImage != null) {
                product.setPicture(normalFormattedImage);
            }
        }
    }

    private void setThumbnail(ProductModel product) {
        List<MediaContainerModel> galleryImageContainers = product.getGalleryImages();
        if(galleryImageContainers != null && CollectionUtils.isNotEmpty(galleryImageContainers)) {
            String thumbnailFormatString = getConfigurationService().getConfiguration().getString(TheBodyShopCoreConstants.AMPLIENCE_IMPORT_MEDIA_FORMAT_THUMBNAIL);
            MediaContainerModel firstGalleryImageContainer = galleryImageContainers.get(0);
            MediaModel thumbnailFormattedImage = tbsMediaService.getImage(firstGalleryImageContainer, thumbnailFormatString);
            if(thumbnailFormattedImage != null) {
                product.setThumbnail(thumbnailFormattedImage);
            }
        }
    }

    public static class ObjectMapperCreator {
        public ObjectMapper createObjectMapper() {
            return new ObjectMapper();
        }
    }

    protected ConfigurationService getConfigurationService() {
        return configurationService;
    }

    protected ModelService getModelService() {
        return modelService;
    }

    protected TbsMediaService getTbsMediaService() {
        return tbsMediaService;
    }

    protected TbsMediaContainerService getTbsMediaContainerService() {
        return tbsMediaContainerService;
    }

    protected ObjectMapperCreator getObjectMapperCreator() {
        return objectMapperCreator;
    }
}
