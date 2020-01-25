/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.impl.MediaDao;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@UnitTest
public class DefaultTbsMediaServiceTest {

    private static final String TEST_FOLDER = "testFolder";
    private static final String TEST_MEDIA = "testMedia";
    private static final String ANOTHER_VALUE = "anotherValue";
    private static final String TEST_FORMAT = "testFormat";

    @Mock
    private MediaDao mediaDao;

    @Mock
    private ModelService modelService;

    @Mock
    private CatalogVersionModel catalogVersion;

    @InjectMocks
    private DefaultTbsMediaService defaultTbsMediaService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultTbsMediaService.setModelService(modelService);
    }

    @Test
    public void shouldReturnMediaFolderFromOneSizedList() {
        MediaFolderModel preparedMediaFolder = mock(MediaFolderModel.class);
        when(mediaDao.findMediaFolderByQualifier(TEST_FOLDER)).thenReturn(Arrays.asList(preparedMediaFolder));
        MediaFolderModel foundMediaFolder = defaultTbsMediaService.getOrCreateMediaFolder(TEST_FOLDER);
        assertEquals(preparedMediaFolder, foundMediaFolder);
        verify(modelService, times(0)).create(eq(MediaFolderModel.class));
        verify(modelService, times(0)).save(eq(MediaFolderModel.class));
        verify(modelService, times(0)).removeAll(any(MediaFolderModel.class));
    }

    @Test
    public void shouldCreateMediaFolder() {
        MediaFolderModel preparedMediaFolder = mock(MediaFolderModel.class);
        when(mediaDao.findMediaFolderByQualifier(TEST_FOLDER)).thenReturn(Collections.EMPTY_LIST);
        when(modelService.create(MediaFolderModel.class)).thenReturn(preparedMediaFolder);
        MediaFolderModel foundMediaFolder = defaultTbsMediaService.getOrCreateMediaFolder(TEST_FOLDER);
        assertEquals(preparedMediaFolder, foundMediaFolder);
        verify(modelService, times(1)).create(eq(MediaFolderModel.class));
        verify(modelService, times(1)).save(preparedMediaFolder);
        verify(modelService, times(0)).removeAll(any(MediaFolderModel.class));
    }

    @Test
    public void shouldReturnMediaFolderFromTwoSizedList() {
        MediaFolderModel firstPreparedMediaFolder = mock(MediaFolderModel.class);
        MediaFolderModel secondPreparedMediaFolder = mock(MediaFolderModel.class);
        when(mediaDao.findMediaFolderByQualifier(TEST_FOLDER)).thenReturn(Arrays.asList(firstPreparedMediaFolder, secondPreparedMediaFolder));
        MediaFolderModel foundMediaFolder = defaultTbsMediaService.getOrCreateMediaFolder(TEST_FOLDER);
        assertEquals(firstPreparedMediaFolder, foundMediaFolder);
        verify(modelService, times(0)).create(eq(MediaFolderModel.class));
        verify(modelService, times(0)).save(eq(MediaFolderModel.class));
        verify(modelService, times(1)).removeAll(eq(Arrays.asList(secondPreparedMediaFolder)));
    }

    @Test
    public void shouldReturnMediaFromOneSizedList() {
        MediaModel preparedMedia = mock(MediaModel.class);
        when(mediaDao.findMediaByCode(catalogVersion, TEST_MEDIA)).thenReturn(Arrays.asList(preparedMedia));
        MediaModel foundMedia = defaultTbsMediaService.getOrCreateImage(TEST_MEDIA, catalogVersion);
        assertEquals(preparedMedia, foundMedia);
        verify(modelService, times(0)).create(eq(MediaModel.class));
        verify(modelService, times(0)).save(eq(MediaModel.class));
        verify(modelService, times(0)).removeAll(any(MediaModel.class));
    }

    @Test
    public void shouldCreateMedia() {
        MediaModel preparedMedia = mock(MediaModel.class);
        when(mediaDao.findMediaByCode(catalogVersion, TEST_MEDIA)).thenReturn(Collections.EMPTY_LIST);
        when(modelService.create(MediaModel.class)).thenReturn(preparedMedia);
        MediaModel foundMedia = defaultTbsMediaService.getOrCreateImage(TEST_MEDIA, catalogVersion);
        assertEquals(preparedMedia, foundMedia);
        verify(modelService, times(1)).create(eq(MediaModel.class));
        verify(modelService, times(1)).save(preparedMedia);
        verify(modelService, times(0)).removeAll(any(MediaModel.class));
    }

    @Test
    public void shouldReturnMediaFromTwoSizedList() {
        MediaModel firstMedia = mock(MediaModel.class);
        MediaModel secondMedia = mock(MediaModel.class);
        when(mediaDao.findMediaByCode(catalogVersion, TEST_MEDIA)).thenReturn(Arrays.asList(firstMedia, secondMedia));
        MediaModel foundMedia = defaultTbsMediaService.getOrCreateImage(TEST_MEDIA, catalogVersion);
        assertEquals(firstMedia, foundMedia);
        verify(modelService, times(0)).create(eq(MediaModel.class));
        verify(modelService, times(0)).save(eq(MediaModel.class));
        verify(modelService, times(1)).removeAll(eq(Arrays.asList(secondMedia)));
    }

    @Test
    public void shouldReturnTrueForMediaCodeContainsAny() {
        MediaModel media = mock(MediaModel.class);
        String[] codeValues = {TEST_MEDIA, ANOTHER_VALUE};
        when(media.getCode()).thenReturn(TEST_MEDIA);
        assertTrue(defaultTbsMediaService.mediaCodeContainsAny(media, codeValues));
    }

    @Test
    public void shouldReturnFalseForMediaCodeContainsAny() {
        MediaModel media = mock(MediaModel.class);
        String[] codeValues = {TEST_FOLDER, ANOTHER_VALUE};
        when(media.getCode()).thenReturn(TEST_MEDIA);
        assertFalse(defaultTbsMediaService.mediaCodeContainsAny(media, codeValues));
    }

    @Test
    public void shouldReturnMediaFormatFromOneSizedList() {
        MediaFormatModel preparedMediaFormat = mock(MediaFormatModel.class);
        when(mediaDao.findMediaFormatByQualifier(TEST_FORMAT)).thenReturn(Arrays.asList(preparedMediaFormat));
        MediaFormatModel foundMediaFormat = defaultTbsMediaService.getOrCreateMediaFormat(TEST_FORMAT);
        assertEquals(preparedMediaFormat, foundMediaFormat);
        verify(modelService, times(0)).create(eq(MediaFormatModel.class));
        verify(modelService, times(0)).save(eq(MediaFormatModel.class));
        verify(modelService, times(0)).removeAll(any(MediaFormatModel.class));
    }

    @Test
    public void shouldCreateMediaFormat() {
        MediaFormatModel preparedMediaFormat = mock(MediaFormatModel.class);
        when(mediaDao.findMediaFormatByQualifier(TEST_FORMAT)).thenReturn(Collections.EMPTY_LIST);
        when(modelService.create(MediaFormatModel.class)).thenReturn(preparedMediaFormat);
        MediaFormatModel foundMediaFormat = defaultTbsMediaService.getOrCreateMediaFormat(TEST_FORMAT);
        assertEquals(preparedMediaFormat, foundMediaFormat);
        verify(modelService, times(1)).create(eq(MediaFormatModel.class));
        verify(modelService, times(1)).save(preparedMediaFormat);
        verify(modelService, times(0)).removeAll(any(MediaFormatModel.class));
    }

    @Test
    public void shouldReturnMediaFormatFromTwoSizedList() {
        MediaFormatModel firstPreparedMediaFormat = mock(MediaFormatModel.class);
        MediaFormatModel secondPreparedMediaFormat = mock(MediaFormatModel.class);
        when(mediaDao.findMediaFormatByQualifier(TEST_FORMAT)).thenReturn(Arrays.asList(firstPreparedMediaFormat, secondPreparedMediaFormat));
        MediaFormatModel foundMediaFormat = defaultTbsMediaService.getOrCreateMediaFormat(TEST_FORMAT);
        assertEquals(firstPreparedMediaFormat, foundMediaFormat);
        verify(modelService, times(0)).create(eq(MediaFormatModel.class));
        verify(modelService, times(0)).save(eq(MediaFormatModel.class));
        verify(modelService, times(1)).removeAll(eq(Arrays.asList(secondPreparedMediaFormat)));
    }

}
