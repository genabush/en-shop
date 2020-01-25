/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;
import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@UnitTest
public class VariantProductMeasurementPrepareInterceptorTest {

    private static final String TEST_UNIT_OF_MEASURE = "testUnitOfMeasure";

    private UnitService unitService;
    private TbsCatalogVersionService tbsCatalogVersionService;
    private InterceptorContext interceptorContext;
    private ModelService modelService;

    private VariantProductMeasurementPrepareInterceptor variantProductMeasurementPrepareInterceptor;

    @Before
    public void setUp() {
        unitService = mock(UnitService.class);
        tbsCatalogVersionService = mock(TbsCatalogVersionService.class);
        interceptorContext = mock(InterceptorContext.class);
        modelService = mock(ModelService.class);
        variantProductMeasurementPrepareInterceptor = new VariantProductMeasurementPrepareInterceptor(unitService, tbsCatalogVersionService);
        when(interceptorContext.getModelService()).thenReturn(modelService);
    }

    @Test
    public void shouldSetSizeFromMeasurementAtVariant() {
        TbsVariantProductModel tbsVariantProduct = new TbsVariantProductModel();
        setCatalogVersion(tbsVariantProduct);
        when(modelService.isNew(any(TbsVariantProductModel.class))).thenReturn(Boolean.TRUE);
        when(tbsCatalogVersionService.isMarketProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
        tbsVariantProduct.setErpMeasurement(1.5);
        invokePrepareInterceptor(tbsVariantProduct, interceptorContext);
        assertEquals("1.5", tbsVariantProduct.getSize());
        verify(unitService, times(0)).getUnitForCode(anyString());
    }

    @Test
    public void shouldSetUnitFromUnitMeasureAtVariant() {
        TbsVariantProductModel tbsVariantProduct = new TbsVariantProductModel();
        setCatalogVersion(tbsVariantProduct);
        when(modelService.isNew(any(TbsVariantProductModel.class))).thenReturn(Boolean.TRUE);
        when(tbsCatalogVersionService.isMarketProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
        tbsVariantProduct.setErpUnitOfMeasure(TEST_UNIT_OF_MEASURE);
        invokePrepareInterceptor(tbsVariantProduct, interceptorContext);
        assertNull(tbsVariantProduct.getSize());
        verify(unitService, times(1)).getUnitForCode(TEST_UNIT_OF_MEASURE);
    }

    @Test
    public void shouldNotSetAnythingIfNotNew() {
        TbsVariantProductModel tbsVariantProduct = new TbsVariantProductModel();
        setCatalogVersion(tbsVariantProduct);
        when(modelService.isNew(any(TbsVariantProductModel.class))).thenReturn(Boolean.FALSE);
        when(tbsCatalogVersionService.isMarketProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
        tbsVariantProduct.setErpMeasurement(1.5);
        tbsVariantProduct.setErpUnitOfMeasure(TEST_UNIT_OF_MEASURE);
        invokePrepareInterceptor(tbsVariantProduct, interceptorContext);
        assertNull(tbsVariantProduct.getSize());
        verify(unitService, times(0)).getUnitForCode(TEST_UNIT_OF_MEASURE);
    }

    @Test
    public void shouldNotSetAnythingIfNotMarketCatalog() {
        TbsVariantProductModel tbsVariantProduct = new TbsVariantProductModel();
        setCatalogVersion(tbsVariantProduct);
        when(modelService.isNew(any(TbsVariantProductModel.class))).thenReturn(Boolean.TRUE);
        when(tbsCatalogVersionService.isMarketProductCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.FALSE);
        tbsVariantProduct.setErpMeasurement(1.5);
        tbsVariantProduct.setErpUnitOfMeasure(TEST_UNIT_OF_MEASURE);
        invokePrepareInterceptor(tbsVariantProduct, interceptorContext);
        assertNull(tbsVariantProduct.getSize());
        verify(unitService, times(0)).getUnitForCode(TEST_UNIT_OF_MEASURE);
    }


    private void setCatalogVersion(ProductModel product) {
        CatalogVersionModel catalogVersion = new CatalogVersionModel();
        catalogVersion.setVersion("Staged");
        product.setCatalogVersion(catalogVersion);
    }

    private void invokePrepareInterceptor(TbsVariantProductModel tbsVariantProductModel, InterceptorContext interceptorContext) {
        try {
            variantProductMeasurementPrepareInterceptor.onPrepare(tbsVariantProductModel, interceptorContext);
        } catch (InterceptorException e) {
            fail();
        }
    }


}
