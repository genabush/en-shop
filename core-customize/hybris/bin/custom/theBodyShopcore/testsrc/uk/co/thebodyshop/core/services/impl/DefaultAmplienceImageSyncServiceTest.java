/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;
import uk.co.thebodyshop.core.media.amplience.AmplienceMediaSet;
import uk.co.thebodyshop.core.media.amplience.AmplienceMediaSetItem;
import uk.co.thebodyshop.core.services.TbsMediaContainerService;
import uk.co.thebodyshop.core.services.TbsMediaService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
public class DefaultAmplienceImageSyncServiceTest {

    protected static final String TEST_PRODUCT_CODE = "testProductCode";
    protected static final String TEST_AMPLIENCE_URL = "https://www.test.com/";
    protected static final String AMPLIENCE_URL_SUFFIX = "_SET.json";

    protected static final String TEST_PRODUCT_CODE_1 = "testProductCode__1";
    protected static final String TEST_PRODUCT_CODE_2 = "testProductCode__2";
    protected static final String TEST_PRODUCT_CODE_3 = "testProductCode__3";
    protected static final String TEST_PRODUCT_CODE_4 = "testProductCode__4";

    protected static final String TEST_IMAGE_URL_1 = "http://www.test.com/img_1";
    protected static final String TEST_IMAGE_URL_2 = "http://www.test.com/img_2";
    protected static final String TEST_IMAGE_URL_3 = "http://www.test.com/img_3";

    protected static final String TEST_IMAGE_URL_1_HTTPS = "https://www.test.com/img_1";
    protected static final String TEST_IMAGE_URL_2_HTTPS = "https://www.test.com/img_2";
    protected static final String TEST_IMAGE_URL_3_HTTPS = "https://www.test.com/img_3";
    protected static final String TEST_IMAGE_URL_4_HTTPS = "https://www.test.com/img_4";

    protected static final String IMAGE_FORMATS = "515Wx515H,300Wx300H,150Wx150H,96Wx96H";

    protected static final String FOLDER_AMPLIENCE = "amplience";

    protected static final String IMAGE_FORMAT_1200Wx1200H = "1200Wx1200H";
    protected static final String IMAGE_FORMAT_515Wx515H = "515Wx515H";
    protected static final String IMAGE_FORMAT_300Wx300H = "300Wx300H";
    protected static final String IMAGE_FORMAT_150Wx150H = "150Wx150H";
    protected static final String IMAGE_FORMAT_96Wx96H = "96Wx96H";

    protected static final String IMAGE_JPEG = "image/jpeg";

    protected static final String DOUBLE_UNDERSCORE = "__";
    protected static final String QUESTIONMARK = "?";
    protected static final String JPEG_EXTENSION = ".jpg";

    protected static final int WIDTH_640 = 640;
    protected static final int HEIGHT_640 = 640;

    @Mock
    protected ConfigurationService configurationService;

    @Mock
    protected Configuration configuration;

    @Mock
    protected TbsMediaService tbsMediaService;

    @Mock
    protected TbsMediaContainerService tbsMediaContainerService;

    @Mock
    protected DefaultAmplienceImageSyncService.ObjectMapperCreator objectMapperCreator;

    @Mock
    protected ModelService modelService;

    @Mock
    protected ObjectMapper objectMapper;

    @Spy
    protected ProductModel product;

    @Spy
    protected CatalogVersionModel catalogVersion;

    @Spy
    protected MediaContainerModel mediaContainer_1;
    @Spy
    protected MediaContainerModel mediaContainer_2;
    @Spy
    protected MediaContainerModel mediaContainer_3;
    @Spy
    protected MediaContainerModel mediaContainer_4;

    @Spy
    protected MediaModel mediaContainer_1_1200Wx1200H;
    @Spy
    protected MediaModel mediaContainer_1_515Wx515H;
    @Spy
    protected MediaModel mediaContainer_1_300Wx300H;
    @Spy
    protected MediaModel mediaContainer_1_150Wx150H;
    @Spy
    protected MediaModel mediaContainer_1_96Wx96H;

    @Spy
    protected MediaModel mediaContainer_2_1200Wx1200H;
    @Spy
    protected MediaModel mediaContainer_2_515Wx515H;
    @Spy
    protected MediaModel mediaContainer_2_300Wx300H;
    @Spy
    protected MediaModel mediaContainer_2_150Wx150H;
    @Spy
    protected MediaModel mediaContainer_2_96Wx96H;

    @Spy
    protected MediaModel mediaContainer_3_1200Wx1200H;
    @Spy
    protected MediaModel mediaContainer_3_515Wx515H;
    @Spy
    protected MediaModel mediaContainer_3_300Wx300H;
    @Spy
    protected MediaModel mediaContainer_3_150Wx150H;
    @Spy
    protected MediaModel mediaContainer_3_96Wx96H;

    @Spy
    protected MediaModel mediaContainer_4_1200Wx1200H;
    @Spy
    protected MediaModel mediaContainer_4_515Wx515H;
    @Spy
    protected MediaModel mediaContainer_4_300Wx300H;
    @Spy
    protected MediaModel mediaContainer_4_150Wx150H;
    @Spy
    protected MediaModel mediaContainer_4_96Wx96H;

    @Spy
    protected MediaFolderModel amplienceFolder;

    @Spy
    protected MediaFormatModel mediaFormat_1200Wx1200H;
    @Spy
    protected MediaFormatModel mediaFormat_515Wx515H;
    @Spy
    protected MediaFormatModel mediaFormat_300Wx300H;
    @Spy
    protected MediaFormatModel mediaFormat_150Wx150H;
    @Spy
    protected MediaFormatModel mediaFormat_96Wx96H;

    @InjectMocks
    protected DefaultAmplienceImageSyncService defaultAmplienceImageSyncService;

    protected StringBuilder syncMessageBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        syncMessageBuilder = new StringBuilder();
        setUpProductBasics();
        setUpMediaContainers();
        setUpMedias();
        setUpMediaFolder();
        setUpMediaFormats();
        prepareConfigurationServiceInvokations();
        prepareObjectMapperInvokations();
        prepareMediaContainerServiceInvokations();
        prepareMediaServiceInvokations();
        prepareMediaContainerInvokations();
    }

    private void setUpProductBasics() {
        product.setCode(TEST_PRODUCT_CODE);
        product.setCatalogVersion(catalogVersion);
    }

    private void setUpMediaContainers() {
        setUpMediaContainer(mediaContainer_1, TEST_PRODUCT_CODE_1);
        setUpMediaContainer(mediaContainer_2, TEST_PRODUCT_CODE_2);
        setUpMediaContainer(mediaContainer_3, TEST_PRODUCT_CODE_3);
        setUpMediaContainer(mediaContainer_4, TEST_PRODUCT_CODE_4);
    }

    private void setUpMediaContainer(MediaContainerModel mediaContainer, String qualifier) {
        mediaContainer.setQualifier(qualifier);
        mediaContainer.setCatalogVersion(catalogVersion);
    }

    private void setUpMedias() {
        setUpMedia(mediaContainer_1_1200Wx1200H, TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H);
        setUpMedia(mediaContainer_1_515Wx515H, TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H);
        setUpMedia(mediaContainer_1_300Wx300H, TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H);
        setUpMedia(mediaContainer_1_150Wx150H, TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H);
        setUpMedia(mediaContainer_1_96Wx96H, TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H);

        setUpMedia(mediaContainer_2_1200Wx1200H, TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H);
        setUpMedia(mediaContainer_2_515Wx515H, TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H);
        setUpMedia(mediaContainer_2_300Wx300H, TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H);
        setUpMedia(mediaContainer_2_150Wx150H, TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H);
        setUpMedia(mediaContainer_2_96Wx96H, TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H);

        setUpMedia(mediaContainer_3_1200Wx1200H, TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H);
        setUpMedia(mediaContainer_3_515Wx515H, TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H);
        setUpMedia(mediaContainer_3_300Wx300H, TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H);
        setUpMedia(mediaContainer_3_150Wx150H, TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H);
        setUpMedia(mediaContainer_3_96Wx96H, TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H);

        setUpMedia(mediaContainer_4_1200Wx1200H, TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H);
        setUpMedia(mediaContainer_4_515Wx515H, TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H);
        setUpMedia(mediaContainer_4_300Wx300H, TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H);
        setUpMedia(mediaContainer_4_150Wx150H, TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H);
        setUpMedia(mediaContainer_4_96Wx96H, TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H);
    }

    private void setUpMedia(MediaModel media, String code) {
        media.setCode(code);
        media.setCatalogVersion(catalogVersion);
    }

    private void setUpMediaFolder() {
        amplienceFolder.setQualifier(FOLDER_AMPLIENCE);
        amplienceFolder.setPath(FOLDER_AMPLIENCE);
    }

    private void setUpMediaFormats() {
        setUpMediaFormat(mediaFormat_1200Wx1200H, IMAGE_FORMAT_1200Wx1200H);
        setUpMediaFormat(mediaFormat_515Wx515H, IMAGE_FORMAT_515Wx515H);
        setUpMediaFormat(mediaFormat_300Wx300H, IMAGE_FORMAT_300Wx300H);
        setUpMediaFormat(mediaFormat_150Wx150H, IMAGE_FORMAT_150Wx150H);
        setUpMediaFormat(mediaFormat_96Wx96H, IMAGE_FORMAT_96Wx96H);
    }

    private void setUpMediaFormat(MediaFormatModel mediaFormat, String qualifier) {
        mediaFormat.setQualifier(qualifier);
        mediaFormat.setAmplienceTemplate(qualifier);
    }

    private void prepareConfigurationServiceInvokations() {
        when(configurationService.getConfiguration()).thenReturn(configuration);
        when(configuration.getString(TheBodyShopCoreConstants.AMPLIENCE_ROOT_URL)).thenReturn(TEST_AMPLIENCE_URL);
        when(configuration.getString(TheBodyShopCoreConstants.AMPLIENCE_MEDIA_URL_SUFFIX)).thenReturn(AMPLIENCE_URL_SUFFIX);
        when(configuration.getString(TheBodyShopCoreConstants.AMPLIENCE_IMPORT_MEDIA_FORMATS)).thenReturn(IMAGE_FORMATS);
        when(configuration.getString(TheBodyShopCoreConstants.DEFAULT_MEDIA_FOLDER)).thenReturn(FOLDER_AMPLIENCE);
        when(configuration.getString(TheBodyShopCoreConstants.AMPLIENCE_IMPORT_MEDIA_FORMAT_NORMAL)).thenReturn(IMAGE_FORMAT_150Wx150H);
        when(configuration.getString(TheBodyShopCoreConstants.AMPLIENCE_IMPORT_MEDIA_FORMAT_THUMBNAIL)).thenReturn(IMAGE_FORMAT_96Wx96H);
    }

    private void prepareObjectMapperInvokations() {
        try {
            when(objectMapperCreator.createObjectMapper()).thenReturn(objectMapper);
            when(objectMapper.readValue(any(URL.class), eq(AmplienceMediaSet.class))).thenReturn(getCorrectMediaSetWithThreeImages());
        } catch (IOException e) {
            fail();
        }
    }

    private AmplienceMediaSet getCorrectMediaSetWithThreeImages() {
        AmplienceMediaSet amplienceMediaSet = new AmplienceMediaSet();
        amplienceMediaSet.setName(TEST_PRODUCT_CODE + AMPLIENCE_URL_SUFFIX);
        List<AmplienceMediaSetItem> amplienceMediaSetItems = new ArrayList<>();
        amplienceMediaSetItems.add(createMediaSetItem(TEST_IMAGE_URL_1));
        amplienceMediaSetItems.add(createMediaSetItem(TEST_IMAGE_URL_2));
        amplienceMediaSetItems.add(createMediaSetItem(TEST_IMAGE_URL_3));
        amplienceMediaSet.setItems(amplienceMediaSetItems);
        return amplienceMediaSet;
    }

    private AmplienceMediaSetItem createMediaSetItem(String url) {
        AmplienceMediaSetItem amplienceMediaSetItem = new AmplienceMediaSetItem();
        amplienceMediaSetItem.setType(AmplienceMediaSetItem.ItemType.img);
        amplienceMediaSetItem.setSrc(url);
        amplienceMediaSetItem.setWidth(WIDTH_640);
        amplienceMediaSetItem.setHeight(HEIGHT_640);
        amplienceMediaSetItem.setFormat(AmplienceMediaSetItem.ItemFormat.JPEG);
        return amplienceMediaSetItem;
    }

    private void prepareMediaContainerServiceInvokations() {
        when(tbsMediaContainerService.getOrCreateMediaContainer(TEST_PRODUCT_CODE_1, catalogVersion)).thenReturn(mediaContainer_1);
        when(tbsMediaContainerService.getOrCreateMediaContainer(TEST_PRODUCT_CODE_2, catalogVersion)).thenReturn(mediaContainer_2);
        when(tbsMediaContainerService.getOrCreateMediaContainer(TEST_PRODUCT_CODE_3, catalogVersion)).thenReturn(mediaContainer_3);
        when(tbsMediaContainerService.getOrCreateMediaContainer(TEST_PRODUCT_CODE_4, catalogVersion)).thenReturn(mediaContainer_4);
    }

    private void prepareMediaContainerInvokations() {
        when(mediaContainer_1.getMedias()).thenReturn(Arrays.asList(mediaContainer_1_515Wx515H, mediaContainer_1_300Wx300H, mediaContainer_1_150Wx150H, mediaContainer_1_96Wx96H));
        when(mediaContainer_2.getMedias()).thenReturn(Arrays.asList(mediaContainer_2_515Wx515H, mediaContainer_2_300Wx300H, mediaContainer_2_150Wx150H, mediaContainer_2_96Wx96H));
        when(mediaContainer_3.getMedias()).thenReturn(Arrays.asList(mediaContainer_3_515Wx515H, mediaContainer_3_300Wx300H, mediaContainer_3_150Wx150H, mediaContainer_3_96Wx96H));
        when(mediaContainer_4.getMedias()).thenReturn(Arrays.asList(mediaContainer_4_515Wx515H, mediaContainer_4_300Wx300H, mediaContainer_4_150Wx150H, mediaContainer_4_96Wx96H));
    }

    private void prepareMediaServiceInvokations() {
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion))
                .thenReturn(mediaContainer_1_1200Wx1200H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion))
                .thenReturn(mediaContainer_1_515Wx515H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion))
                .thenReturn(mediaContainer_1_300Wx300H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion))
                .thenReturn(mediaContainer_1_150Wx150H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion))
                .thenReturn(mediaContainer_1_96Wx96H);

        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion))
                .thenReturn(mediaContainer_2_1200Wx1200H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion))
                .thenReturn(mediaContainer_2_515Wx515H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion))
                .thenReturn(mediaContainer_2_300Wx300H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion))
                .thenReturn(mediaContainer_2_150Wx150H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion))
                .thenReturn(mediaContainer_2_96Wx96H);

        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion))
                .thenReturn(mediaContainer_3_1200Wx1200H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion))
                .thenReturn(mediaContainer_3_515Wx515H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion))
                .thenReturn(mediaContainer_3_300Wx300H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion))
                .thenReturn(mediaContainer_3_150Wx150H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion))
                .thenReturn(mediaContainer_3_96Wx96H);

        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion))
                .thenReturn(mediaContainer_4_1200Wx1200H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion))
                .thenReturn(mediaContainer_4_515Wx515H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion))
                .thenReturn(mediaContainer_4_300Wx300H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion))
                .thenReturn(mediaContainer_4_150Wx150H);
        when(tbsMediaService.getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion))
                .thenReturn(mediaContainer_4_96Wx96H);

        when(tbsMediaService.getOrCreateMediaFolder(FOLDER_AMPLIENCE)).thenReturn(amplienceFolder);

        when(tbsMediaService.getOrCreateMediaFormat(IMAGE_FORMAT_1200Wx1200H)).thenReturn(mediaFormat_1200Wx1200H);
        when(tbsMediaService.getOrCreateMediaFormat(IMAGE_FORMAT_515Wx515H)).thenReturn(mediaFormat_515Wx515H);
        when(tbsMediaService.getOrCreateMediaFormat(IMAGE_FORMAT_300Wx300H)).thenReturn(mediaFormat_300Wx300H);
        when(tbsMediaService.getOrCreateMediaFormat(IMAGE_FORMAT_150Wx150H)).thenReturn(mediaFormat_150Wx150H);
        when(tbsMediaService.getOrCreateMediaFormat(IMAGE_FORMAT_96Wx96H)).thenReturn(mediaFormat_96Wx96H);

        when(tbsMediaService.getImage(mediaContainer_1, IMAGE_FORMAT_1200Wx1200H)).thenReturn(mediaContainer_1_1200Wx1200H);
        when(tbsMediaService.getImage(mediaContainer_1, IMAGE_FORMAT_515Wx515H)).thenReturn(mediaContainer_1_515Wx515H);
        when(tbsMediaService.getImage(mediaContainer_1, IMAGE_FORMAT_300Wx300H)).thenReturn(mediaContainer_1_300Wx300H);
        when(tbsMediaService.getImage(mediaContainer_1, IMAGE_FORMAT_150Wx150H)).thenReturn(mediaContainer_1_150Wx150H);
        when(tbsMediaService.getImage(mediaContainer_1, IMAGE_FORMAT_96Wx96H)).thenReturn(mediaContainer_1_96Wx96H);

        when(tbsMediaService.getImage(mediaContainer_2, IMAGE_FORMAT_1200Wx1200H)).thenReturn(mediaContainer_2_1200Wx1200H);
        when(tbsMediaService.getImage(mediaContainer_2, IMAGE_FORMAT_515Wx515H)).thenReturn(mediaContainer_2_515Wx515H);
        when(tbsMediaService.getImage(mediaContainer_2, IMAGE_FORMAT_300Wx300H)).thenReturn(mediaContainer_2_300Wx300H);
        when(tbsMediaService.getImage(mediaContainer_2, IMAGE_FORMAT_150Wx150H)).thenReturn(mediaContainer_2_150Wx150H);
        when(tbsMediaService.getImage(mediaContainer_2, IMAGE_FORMAT_96Wx96H)).thenReturn(mediaContainer_2_96Wx96H);

        when(tbsMediaService.getImage(mediaContainer_3, IMAGE_FORMAT_1200Wx1200H)).thenReturn(mediaContainer_3_1200Wx1200H);
        when(tbsMediaService.getImage(mediaContainer_3, IMAGE_FORMAT_515Wx515H)).thenReturn(mediaContainer_3_515Wx515H);
        when(tbsMediaService.getImage(mediaContainer_3, IMAGE_FORMAT_300Wx300H)).thenReturn(mediaContainer_3_300Wx300H);
        when(tbsMediaService.getImage(mediaContainer_3, IMAGE_FORMAT_150Wx150H)).thenReturn(mediaContainer_3_150Wx150H);
        when(tbsMediaService.getImage(mediaContainer_3, IMAGE_FORMAT_96Wx96H)).thenReturn(mediaContainer_3_96Wx96H);

        when(tbsMediaService.getImage(mediaContainer_4, IMAGE_FORMAT_1200Wx1200H)).thenReturn(mediaContainer_4_1200Wx1200H);
        when(tbsMediaService.getImage(mediaContainer_4, IMAGE_FORMAT_515Wx515H)).thenReturn(mediaContainer_4_515Wx515H);
        when(tbsMediaService.getImage(mediaContainer_4, IMAGE_FORMAT_300Wx300H)).thenReturn(mediaContainer_4_300Wx300H);
        when(tbsMediaService.getImage(mediaContainer_4, IMAGE_FORMAT_150Wx150H)).thenReturn(mediaContainer_4_150Wx150H);
        when(tbsMediaService.getImage(mediaContainer_4, IMAGE_FORMAT_96Wx96H)).thenReturn(mediaContainer_4_96Wx96H);
        
    }

    @Test
    public void shouldUpdateImagesForProduct() throws MalformedURLException,IOException {
        defaultAmplienceImageSyncService.syncImagesForProduct(product, syncMessageBuilder);
        verifyGetMediaContainerInvoked();
        verifyGetOrCreateImageInvoked();
        verify(tbsMediaService, times(12)).getOrCreateMediaFolder(FOLDER_AMPLIENCE);
        verifyGetFormatInvoked();
        verifySetUrlInvoked();
        verifySetMediaContainerInvoked();
        assertProductSetCorrectly();
    }

    private void verifyGetMediaContainerInvoked() {
        verify(tbsMediaContainerService, times(1)).getOrCreateMediaContainer(TEST_PRODUCT_CODE_1, catalogVersion);
        verify(tbsMediaContainerService, times(1)).getOrCreateMediaContainer(TEST_PRODUCT_CODE_2, catalogVersion);
        verify(tbsMediaContainerService, times(1)).getOrCreateMediaContainer(TEST_PRODUCT_CODE_3, catalogVersion);
        verify(tbsMediaContainerService, times(0)).getOrCreateMediaContainer(TEST_PRODUCT_CODE_4, catalogVersion);
    }

    private void verifyGetOrCreateImageInvoked() {
        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_1 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion);

        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_2 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion);

        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion);
        verify(tbsMediaService, times(1)).getOrCreateImage(TEST_PRODUCT_CODE_3 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion);

        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_1200Wx1200H, catalogVersion);
        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_515Wx515H, catalogVersion);
        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_300Wx300H, catalogVersion);
        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_150Wx150H, catalogVersion);
        verify(tbsMediaService, times(0)).getOrCreateImage(TEST_PRODUCT_CODE_4 + DOUBLE_UNDERSCORE + IMAGE_FORMAT_96Wx96H, catalogVersion);

    }

    private void verifyGetFormatInvoked() {
        verify(tbsMediaService, times(0)).getOrCreateMediaFormat(IMAGE_FORMAT_1200Wx1200H);
        verify(tbsMediaService, times(3)).getOrCreateMediaFormat(IMAGE_FORMAT_515Wx515H);
        verify(tbsMediaService, times(3)).getOrCreateMediaFormat(IMAGE_FORMAT_300Wx300H);
        verify(tbsMediaService, times(3)).getOrCreateMediaFormat(IMAGE_FORMAT_150Wx150H);
        verify(tbsMediaService, times(3)).getOrCreateMediaFormat(IMAGE_FORMAT_96Wx96H);
    }

    private void verifySetUrlInvoked() {
        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_1_1200Wx1200H, TEST_IMAGE_URL_1_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_1200Wx1200H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_1_515Wx515H, TEST_IMAGE_URL_1_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_515Wx515H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_1_300Wx300H, TEST_IMAGE_URL_1_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_300Wx300H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_1_150Wx150H, TEST_IMAGE_URL_1_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_150Wx150H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_1_96Wx96H, TEST_IMAGE_URL_1_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_96Wx96H);

        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_2_1200Wx1200H, TEST_IMAGE_URL_2_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_1200Wx1200H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_2_515Wx515H, TEST_IMAGE_URL_2_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_515Wx515H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_2_300Wx300H, TEST_IMAGE_URL_2_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_300Wx300H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_2_150Wx150H, TEST_IMAGE_URL_2_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_150Wx150H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_2_96Wx96H, TEST_IMAGE_URL_2_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_96Wx96H);

        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_3_1200Wx1200H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_1200Wx1200H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_3_515Wx515H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_515Wx515H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_3_300Wx300H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_300Wx300H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_3_150Wx150H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_150Wx150H);
        verify(tbsMediaService, times(1)).setUrlForMedia(mediaContainer_3_96Wx96H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_96Wx96H);

        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_4_1200Wx1200H, TEST_IMAGE_URL_4_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_1200Wx1200H);
        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_4_515Wx515H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_515Wx515H);
        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_4_300Wx300H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_300Wx300H);
        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_4_150Wx150H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_150Wx150H);
        verify(tbsMediaService, times(0)).setUrlForMedia(mediaContainer_4_96Wx96H, TEST_IMAGE_URL_3_HTTPS + JPEG_EXTENSION + QUESTIONMARK + IMAGE_FORMAT_96Wx96H);

    }

    private void verifySetMediaContainerInvoked() {
        verify(mediaContainer_1_1200Wx1200H, times(0)).setMediaContainer(mediaContainer_1);
        verify(mediaContainer_1_515Wx515H, times(1)).setMediaContainer(mediaContainer_1);
        verify(mediaContainer_1_300Wx300H, times(1)).setMediaContainer(mediaContainer_1);
        verify(mediaContainer_1_150Wx150H, times(1)).setMediaContainer(mediaContainer_1);
        verify(mediaContainer_1_96Wx96H, times(1)).setMediaContainer(mediaContainer_1);

        verify(mediaContainer_2_1200Wx1200H, times(0)).setMediaContainer(mediaContainer_2);
        verify(mediaContainer_2_515Wx515H, times(1)).setMediaContainer(mediaContainer_2);
        verify(mediaContainer_2_300Wx300H, times(1)).setMediaContainer(mediaContainer_2);
        verify(mediaContainer_2_150Wx150H, times(1)).setMediaContainer(mediaContainer_2);
        verify(mediaContainer_2_96Wx96H, times(1)).setMediaContainer(mediaContainer_2);

        verify(mediaContainer_3_1200Wx1200H, times(0)).setMediaContainer(mediaContainer_3);
        verify(mediaContainer_3_515Wx515H, times(1)).setMediaContainer(mediaContainer_3);
        verify(mediaContainer_3_300Wx300H, times(1)).setMediaContainer(mediaContainer_3);
        verify(mediaContainer_3_150Wx150H, times(1)).setMediaContainer(mediaContainer_3);
        verify(mediaContainer_3_96Wx96H, times(1)).setMediaContainer(mediaContainer_3);

        verify(mediaContainer_4_1200Wx1200H, times(0)).setMediaContainer(mediaContainer_4);
        verify(mediaContainer_4_515Wx515H, times(0)).setMediaContainer(mediaContainer_4);
        verify(mediaContainer_4_300Wx300H, times(0)).setMediaContainer(mediaContainer_4);
        verify(mediaContainer_4_150Wx150H, times(0)).setMediaContainer(mediaContainer_4);
        verify(mediaContainer_4_96Wx96H, times(0)).setMediaContainer(mediaContainer_4);
    }

    private void assertProductSetCorrectly() {
        assertEquals(mediaContainer_1_150Wx150H, product.getPicture());
        assertEquals(mediaContainer_1_96Wx96H, product.getThumbnail());
        assertNormalsAtProducts();
        assertGalleryImagesAtProducts();
        assertImageFormatsAtImages();
        assertMimesAtImages();
    }

    private void assertNormalsAtProducts() {
        assertNotNull(product.getNormal());
        List<MediaModel> normals = new ArrayList<>(product.getNormal());
        assertTrue(normals.size() == 3);
        assertEquals(mediaContainer_1_150Wx150H, normals.get(0));
        assertEquals(mediaContainer_2_150Wx150H, normals.get(1));
        assertEquals(mediaContainer_3_150Wx150H, normals.get(2));
    }

    private void assertGalleryImagesAtProducts() {
        assertNotNull(product.getGalleryImages());
        List<MediaContainerModel> galleryImageContainers = product.getGalleryImages();
        assertTrue(galleryImageContainers.size() == 3);
        assertEquals(mediaContainer_1, galleryImageContainers.get(0));
        assertEquals(mediaContainer_2, galleryImageContainers.get(1));
        assertEquals(mediaContainer_3, galleryImageContainers.get(2));
        assertFalse(galleryImageContainers.contains(mediaContainer_4));

        assertNotNull(mediaContainer_1.getMedias());
        assertFalse(mediaContainer_1.getMedias().contains(mediaContainer_1_1200Wx1200H));
        assertTrue(mediaContainer_1.getMedias().contains(mediaContainer_1_515Wx515H));
        assertTrue(mediaContainer_1.getMedias().contains(mediaContainer_1_300Wx300H));
        assertTrue(mediaContainer_1.getMedias().contains(mediaContainer_1_150Wx150H));
        assertTrue(mediaContainer_1.getMedias().contains(mediaContainer_1_96Wx96H));

        assertNotNull(mediaContainer_2.getMedias());
        assertFalse(mediaContainer_2.getMedias().contains(mediaContainer_2_1200Wx1200H));
        assertTrue(mediaContainer_2.getMedias().contains(mediaContainer_2_515Wx515H));
        assertTrue(mediaContainer_2.getMedias().contains(mediaContainer_2_300Wx300H));
        assertTrue(mediaContainer_2.getMedias().contains(mediaContainer_2_150Wx150H));
        assertTrue(mediaContainer_2.getMedias().contains(mediaContainer_2_96Wx96H));

        assertNotNull(mediaContainer_3.getMedias());
        assertFalse(mediaContainer_3.getMedias().contains(mediaContainer_3_1200Wx1200H));
        assertTrue(mediaContainer_3.getMedias().contains(mediaContainer_3_515Wx515H));
        assertTrue(mediaContainer_3.getMedias().contains(mediaContainer_3_300Wx300H));
        assertTrue(mediaContainer_3.getMedias().contains(mediaContainer_3_150Wx150H));
        assertTrue(mediaContainer_3.getMedias().contains(mediaContainer_3_96Wx96H));

    }

    private void assertImageFormatsAtImages() {
        assertEquals(mediaFormat_515Wx515H, mediaContainer_1_515Wx515H.getMediaFormat());
        assertEquals(mediaFormat_300Wx300H, mediaContainer_1_300Wx300H.getMediaFormat());
        assertEquals(mediaFormat_150Wx150H, mediaContainer_1_150Wx150H.getMediaFormat());
        assertEquals(mediaFormat_96Wx96H, mediaContainer_1_96Wx96H.getMediaFormat());

        assertEquals(mediaFormat_515Wx515H, mediaContainer_2_515Wx515H.getMediaFormat());
        assertEquals(mediaFormat_300Wx300H, mediaContainer_2_300Wx300H.getMediaFormat());
        assertEquals(mediaFormat_150Wx150H, mediaContainer_2_150Wx150H.getMediaFormat());
        assertEquals(mediaFormat_96Wx96H, mediaContainer_2_96Wx96H.getMediaFormat());

        assertEquals(mediaFormat_515Wx515H, mediaContainer_3_515Wx515H.getMediaFormat());
        assertEquals(mediaFormat_300Wx300H, mediaContainer_3_300Wx300H.getMediaFormat());
        assertEquals(mediaFormat_150Wx150H, mediaContainer_3_150Wx150H.getMediaFormat());
        assertEquals(mediaFormat_96Wx96H, mediaContainer_3_96Wx96H.getMediaFormat());
    }

    private void assertMimesAtImages() {
        assertEquals(IMAGE_JPEG, mediaContainer_1_515Wx515H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_1_300Wx300H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_1_150Wx150H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_1_96Wx96H.getMime());

        assertEquals(IMAGE_JPEG, mediaContainer_2_515Wx515H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_2_300Wx300H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_2_150Wx150H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_2_96Wx96H.getMime());


        assertEquals(IMAGE_JPEG, mediaContainer_2_515Wx515H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_2_300Wx300H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_2_150Wx150H.getMime());
        assertEquals(IMAGE_JPEG, mediaContainer_2_96Wx96H.getMime());

    }

}
