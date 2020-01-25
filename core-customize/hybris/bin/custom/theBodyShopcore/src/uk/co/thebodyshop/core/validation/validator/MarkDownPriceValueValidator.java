/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.hybris.platform.core.Registry;

import uk.co.thebodyshop.core.helpers.MarkDownPriceValidationHelper;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.validation.annotation.MarkDownPriceValue;

/**
 * @author Marcin
 */
public class MarkDownPriceValueValidator implements ConstraintValidator<MarkDownPriceValue, MarkDownPriceRowModel>
{

	@Override
	public boolean isValid(final MarkDownPriceRowModel priceRow, final ConstraintValidatorContext constraintContext)
	{
		final MarkDownPriceValidationHelper markDownPriceValidationHelper = Registry.getApplicationContext().getBean(MarkDownPriceValidationHelper.class);
		if (markDownPriceValidationHelper.hasValidPrice(priceRow))
		{
			return true;
		}
		return false;
	}
}
