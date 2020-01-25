/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.thebodyshop.core.daos.TbsMediaContainerDao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@UnitTest
public class DefaultTbsMediaContainerServiceTest {

    private static final String TEST_MEDIA_CONTAINER = "testMediaContainer";
    private static final String TEST_CATALOG = "testCatalog";
    private static final String STAGED = "Staged";

    @Mock
    private TbsMediaContainerDao tbsMediaContainerDao;

    @Mock
    private ModelService modelService;

    @Mock
    private CatalogVersionModel catalogVersion;

    @Mock
    private CatalogModel catalog;

    @InjectMocks
    private DefaultTbsMediaContainerService defaultTbsMediaContainerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultTbsMediaContainerService.setModelService(modelService);
    }

    @Test
    public void shouldReturnOneMediaContainer() {
        MediaContainerModel expectedMediaContainer = mock(MediaContainerModel.class);
        when(tbsMediaContainerDao.findMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion)).thenReturn(Arrays.asList(expectedMediaContainer));
        MediaContainerModel foundMediaContainer = defaultTbsMediaContainerService.getOrCreateMediaContainer(TEST_MEDIA_CONTAINER, catalogVersion);
        assertEquals(expectedMediaContainer, foundMediaContainer);
        verify(modelService, times(0)).create(eq(MediaContainerModel.class));
        verify(modelService, times(0)).save(any(MediaContainerModel.class));
        verify(modelService, times(0)).removeAll(any(MediaContainerModel.class));
    }

    @Test
    public void shouldCreateMediaContainer() {
        MediaContainerModel expectedMediaContainer = mock(MediaContainerModel.class);
        when(tbsMediaContainerDao.findMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion)).thenReturn(Collections.EMPTY_LIST);
        when(modelService.create(MediaContainerModel.class)).thenReturn(expectedMediaContainer);
        MediaContainerModel foundMediaContainer = defaultTbsMediaContainerService.getOrCreateMediaContainer(TEST_MEDIA_CONTAINER, catalogVersion);
        assertEquals(expectedMediaContainer, foundMediaContainer);
        verify(modelService, times(1)).create(eq(MediaContainerModel.class));
        verify(modelService, times(1)).save(expectedMediaContainer);
        verify(modelService, times(0)).removeAll(any(MediaContainerModel.class));
    }

    @Test
    public void shouldReturnOneMediaContainerFromTwoSizedList() {
        MediaContainerModel firstMediaContainer = mock(MediaContainerModel.class);
        MediaContainerModel secondMediaContainer = mock(MediaContainerModel.class);
        when(tbsMediaContainerDao.findMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion)).thenReturn(Arrays.asList(firstMediaContainer, secondMediaContainer));
        MediaContainerModel foundMediaContainer = defaultTbsMediaContainerService.getOrCreateMediaContainer(TEST_MEDIA_CONTAINER, catalogVersion);
        assertEquals(firstMediaContainer, foundMediaContainer);
        verify(modelService, times(0)).create(eq(MediaContainerModel.class));
        verify(modelService, times(0)).save(any(MediaContainerModel.class));
        verify(modelService, times(1)).removeAll(eq(Arrays.asList(secondMediaContainer)));
    }

    @Test(expected = UnknownIdentifierException.class)
    public void shouldThrowUnknownIdentifierException() {
        when(tbsMediaContainerDao.findMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion)).thenReturn(Collections.EMPTY_LIST);
        when(catalogVersion.getCatalog()).thenReturn(catalog);
        when(catalogVersion.getVersion()).thenReturn(STAGED);
        when(catalog.getId()).thenReturn(TEST_CATALOG);
        defaultTbsMediaContainerService.getMediaContainer(TEST_MEDIA_CONTAINER, catalogVersion);
    }

    @Test(expected = AmbiguousIdentifierException.class)
    public void shouldThrowAmbiguousIdentifierException() {
        MediaContainerModel firstMediaContainer = mock(MediaContainerModel.class);
        MediaContainerModel secondMediaContainer = mock(MediaContainerModel.class);
        when(tbsMediaContainerDao.findMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion)).thenReturn(Arrays.asList(firstMediaContainer, secondMediaContainer));
        when(catalogVersion.getCatalog()).thenReturn(catalog);
        when(catalogVersion.getVersion()).thenReturn(STAGED);
        when(catalog.getId()).thenReturn(TEST_CATALOG);
        defaultTbsMediaContainerService.getMediaContainer(TEST_MEDIA_CONTAINER, catalogVersion);
    }

    @Test
    public void shouldDeleteMediaContainersWithContent() {
        MediaContainerModel mediaContainerToDelete = mock(MediaContainerModel.class);
        List<MediaContainerModel> mediaContainersToDelete = Arrays.asList(mediaContainerToDelete);
        List<MediaModel> mediasToDelete = Arrays.asList(mock(MediaModel.class), mock(MediaModel.class));
        when(mediaContainerToDelete.getMedias()).thenReturn(mediasToDelete);
        defaultTbsMediaContainerService.deleteMediaContainersWithContent(mediaContainersToDelete);
        verify(modelService, times(1)).removeAll(mediasToDelete);
        verify(modelService, times(1)).removeAll(mediaContainersToDelete);
    }

    @Test
    public void shouldRemoveDuplicateMediaContainers() {
        MediaContainerModel firstMediaContainer = mock(MediaContainerModel.class);
        MediaContainerModel duplicateMediaContainer = mock(MediaContainerModel.class);
        when(tbsMediaContainerDao.findMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion)).thenReturn(Arrays.asList(firstMediaContainer, duplicateMediaContainer));
        defaultTbsMediaContainerService.getOrCreateMediaContainer(TEST_MEDIA_CONTAINER, catalogVersion);
        verify(modelService, times(1)).removeAll(Arrays.asList(duplicateMediaContainer));
    }

    @Test
    public void shouldNotRemoveAnyMediaContainer() {
        MediaContainerModel mediaContainer = mock(MediaContainerModel.class);
        when(tbsMediaContainerDao.findMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion)).thenReturn(Arrays.asList(mediaContainer));
        //defaultTbsMediaContainerService.removeDuplicateMediaContainers(TEST_MEDIA_CONTAINER, catalogVersion);
        verify(modelService, times(0)).removeAll(anyCollection());
    }

}
