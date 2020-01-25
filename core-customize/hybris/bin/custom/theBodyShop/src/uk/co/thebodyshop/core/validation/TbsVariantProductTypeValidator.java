/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;

public class TbsVariantProductTypeValidator implements ConstraintValidator<TbsVariantProductType, TbsVariantProductModel>
{
	protected static final String DUMMY_BASE_PRODUCT_CODE = "tbs.dummy.baseproduct.code";

	@Override
	public void initialize(final TbsVariantProductType tbsVariantProductType)
	{
		// empty
	}

	@Override
	public boolean isValid(final TbsVariantProductModel variantModel, final ConstraintValidatorContext constraintValidatorContext)
	{
		if (variantModel.getApprovalStatus() == ArticleApprovalStatus.APPROVED && Objects.nonNull(variantModel.getBaseProduct()))
		{
			final String dummyBaseProductCode = getConfigurationService().getConfiguration().getString(DUMMY_BASE_PRODUCT_CODE);
			if (variantModel.getBaseProduct().getCode().equals(dummyBaseProductCode))
			{
				return false;
			}
		}
		return true;
	}

	public ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}
}
