/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.variants.model.VariantProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;

/**
 * @author Krishna
 */
public class CheckVariantApprovalValidateInterceptor implements ValidateInterceptor<VariantProductModel>
{
	protected static final String DUMMY_BASE_PRODUCT_CODE = "tbs.dummy.baseproduct.code";

	private final ConfigurationService configurationService;
	private final TbsCatalogVersionService tbsCatalogVersionService;

	@Override
	public void onValidate(final VariantProductModel variantProductModel, final InterceptorContext ctx) throws InterceptorException
	{
			if (variantProductModel.getApprovalStatus() == ArticleApprovalStatus.APPROVED && getTbsCatalogVersionService().isStagedCatalog(variantProductModel.getCatalogVersion()))
			{
				final String dummyBaseProductCode = getConfigurationService().getConfiguration().getString(DUMMY_BASE_PRODUCT_CODE);

				if (variantProductModel.getBaseProduct().getCode().equals(dummyBaseProductCode))
				{
					throw new InterceptorException("Variant product is linked with dummy base product");
				}
			}
	}

	@Autowired
	public CheckVariantApprovalValidateInterceptor(final ConfigurationService configurationService, final TbsCatalogVersionService tbsCatalogVersionService)
	{
		this.configurationService = configurationService;
		this.tbsCatalogVersionService = tbsCatalogVersionService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return this.configurationService;
	}

	protected TbsCatalogVersionService getTbsCatalogVersionService()
	{
		return tbsCatalogVersionService;
	}
}
