/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.core.Registry;

import uk.co.thebodyshop.core.helpers.MarkDownPriceValidationHelper;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.validation.annotation.VariantMarkDownPriceRows;

/**
 * @author Marcin
 */
public class VariantMarkDownPriceRowsValidator implements ConstraintValidator<VariantMarkDownPriceRows, TbsVariantProductModel>
{

	@Override
	public boolean isValid(final TbsVariantProductModel variantModel, final ConstraintValidatorContext constraintValidatorContext)
	{
		final MarkDownPriceValidationHelper markDownPriceValidationHelper = Registry.getApplicationContext().getBean(MarkDownPriceValidationHelper.class);
			if (CollectionUtils.isNotEmpty(variantModel.getOwnMarkDownPrices()) && StringUtils.isNotEmpty(markDownPriceValidationHelper.getMarkDownPriceRowsErrors(variantModel.getOwnMarkDownPrices())))
			{
				return false;
			}
		return true;
	}
}
