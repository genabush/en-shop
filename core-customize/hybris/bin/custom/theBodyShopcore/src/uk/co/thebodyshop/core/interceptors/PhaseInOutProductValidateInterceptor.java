/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import java.util.Objects;

/**
 * @author Balakrishna
 */
public class PhaseInOutProductValidateInterceptor implements ValidateInterceptor<TbsVariantProductModel> {

    protected static void throwInterceptorException(String message) throws InterceptorException {
        throw new InterceptorException(message);
    }

    @Override
    public void onValidate(TbsVariantProductModel tbsVariantProductModel, InterceptorContext interceptorContext) throws InterceptorException {

        // first identifying weigher current product is PhaseIn or Phase Out
        boolean isPhaseOutProduct = Objects.nonNull(tbsVariantProductModel.getPhaseInProduct());
        if (isPhaseOutProduct) {
            TbsVariantProductModel phaseOutProduct = tbsVariantProductModel;
            TbsVariantProductModel phaseInProduct = tbsVariantProductModel.getPhaseInProduct();
            phaseInOutConditionChecks(phaseInProduct,phaseOutProduct);
        }
    }

    protected void phaseInOutConditionChecks(TbsVariantProductModel phaseInProduct, TbsVariantProductModel phaseOutProduct) throws InterceptorException {

            if (!phaseInProduct.getCatalogVersion().equals(phaseOutProduct.getCatalogVersion())) {
                throwInterceptorException("Phase In And Phase Out Products Catalog Should Be Same.");
            }
            if (phaseInProduct.getCode().equals(phaseOutProduct.getCode())) {
                throwInterceptorException("Phase In And Phase Out Products Should Not Be Same.");
            }
    }

}
