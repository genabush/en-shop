/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;
import uk.co.thebodyshop.core.model.ProductBadgeModel;

import java.util.Set;

public class ProductBadgesValidateInterceptor implements ValidateInterceptor<ProductModel> {

    private ConfigurationService configurationService;

    public ProductBadgesValidateInterceptor(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public void onValidate(ProductModel product, InterceptorContext interceptorContext) throws InterceptorException {
        int productBadgesMaxCount = configurationService.getConfiguration().getInteger(TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX, TheBodyShopCoreConstants.PRODUCT_BADGES_COUNT_MAX_DEFAULT);
        Set<ProductBadgeModel> productBadges = product.getProductBadges();
        if(productBadges != null && productBadges.size() > productBadgesMaxCount) {
            throw new InterceptorException("Product " + product.getCode() + " already contains " + productBadgesMaxCount + " badges.");
        }
    }

    protected ConfigurationService getConfigurationService() {
        return configurationService;
    }

}
