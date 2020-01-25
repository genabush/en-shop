/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.variants.model.VariantProductModel;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import static org.mockito.Mockito.*;


@UnitTest
public class VariantProductValidateInterceptorTest {


    private static final String DUMMY_VARIANT_PRODUCT_CODE = "dummyVariantProductCode";
    private static final String REGULAR_VARIANT_PRODUCT_CODE = "regularVariantProductCode";

    private ConfigurationService configurationService;
    private Configuration configuration;
    private TbsCatalogVersionService tbsCatalogVersionService;
    private InterceptorContext interceptorContext;

    private CheckVariantApprovalValidateInterceptor checkVariantApprovalValidateInterceptor;

    @Before
    public void setUp() {
        configurationService = mock(ConfigurationService.class);
        configuration = mock(Configuration.class);
        tbsCatalogVersionService = mock(TbsCatalogVersionService.class);
        interceptorContext = mock(InterceptorContext.class);
        checkVariantApprovalValidateInterceptor = new CheckVariantApprovalValidateInterceptor(configurationService, tbsCatalogVersionService);
        when(configurationService.getConfiguration()).thenReturn(configuration);
        when(configuration.getString(anyString())).thenReturn(DUMMY_VARIANT_PRODUCT_CODE);
    }

    @Test
    public void shouldValidateVariantSuccessfully() {
        when(tbsCatalogVersionService.isStagedCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
        TbsVariantProductModel tbsVariantProduct = setUpTbsVariant(REGULAR_VARIANT_PRODUCT_CODE, ArticleApprovalStatus.APPROVED);
        invokeOnValidate(tbsVariantProduct, interceptorContext);
        verify(configurationService, times(1)).getConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionForVariantDummyCode() {
        when(tbsCatalogVersionService.isStagedCatalog(any(CatalogVersionModel.class))).thenReturn(Boolean.TRUE);
        TbsVariantProductModel tbsVariantProduct = setUpTbsVariant(DUMMY_VARIANT_PRODUCT_CODE, ArticleApprovalStatus.APPROVED);
        invokeOnValidate(tbsVariantProduct, interceptorContext);
    }

    private TbsVariantProductModel setUpTbsVariant(String code, ArticleApprovalStatus articleApprovalStatus) {
        TbsVariantProductModel tbsVariantProduct = new TbsVariantProductModel();
        tbsVariantProduct.setApprovalStatus(articleApprovalStatus);
        tbsVariantProduct.setCatalogVersion(new CatalogVersionModel());
        TbsBaseProductModel tbsBaseProduct = new TbsBaseProductModel();
        tbsBaseProduct.setCode(code);
        tbsVariantProduct.setBaseProduct(tbsBaseProduct);
        return tbsVariantProduct;
    }

    private void invokeOnValidate(VariantProductModel variantProductModel, InterceptorContext interceptorContext) {
        try {
            checkVariantApprovalValidateInterceptor.onValidate(variantProductModel, interceptorContext);
        } catch (InterceptorException e) {
            throw new IllegalStateException(e);
        }
    }

}
