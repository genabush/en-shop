/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;
import uk.co.thebodyshop.core.model.ProductBadgeModel;

import java.util.Set;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@UnitTest
public class ProductBadgesValidateInterceptorTest {

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private Configuration configuration;

    @Mock
    private InterceptorContext interceptorContext;

    @Mock
    private ProductModel product;

    @Mock
    private Set<ProductBadgeModel> productBadges;

    @InjectMocks
    private ProductBadgesValidateInterceptor productBadgesValidateInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(configurationService.getConfiguration()).thenReturn(configuration);
        when(configuration.getInteger(TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX, TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX_DEFAULT))
                .thenReturn(TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX_DEFAULT);
    }

    @Test
    public void shouldValidateSuccessfully() {
        when(productBadges.size()).thenReturn(TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX_DEFAULT);
        when(product.getProductBadges()).thenReturn(productBadges);
        try {
            productBadgesValidateInterceptor.onValidate(product, interceptorContext);
        } catch (InterceptorException e) {
            fail();
        }
    }

    @Test(expected = InterceptorException.class)
    public void validationShouldFail() throws InterceptorException {
        when(productBadges.size()).thenReturn(TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX_DEFAULT + 1);
        when(product.getProductBadges()).thenReturn(productBadges);
        productBadgesValidateInterceptor.onValidate(product, interceptorContext);

    }

}
