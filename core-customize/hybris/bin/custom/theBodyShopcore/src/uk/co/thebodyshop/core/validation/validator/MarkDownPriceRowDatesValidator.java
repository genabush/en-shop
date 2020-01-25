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
import uk.co.thebodyshop.core.validation.annotation.MarkDownPriceRowDates;

/**
 * @author Marcin
 */
public class MarkDownPriceRowDatesValidator implements ConstraintValidator<MarkDownPriceRowDates, MarkDownPriceRowModel>
{

	@Override
	public boolean isValid(final MarkDownPriceRowModel priceRow, final ConstraintValidatorContext constraintContext)
	{
		final MarkDownPriceValidationHelper markDownPriceValidationHelper = Registry.getApplicationContext().getBean(MarkDownPriceValidationHelper.class);
		if (markDownPriceValidationHelper.hasValidDates(priceRow, true))
		{
			return true;
		}
		return false;
	}
}