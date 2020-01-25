/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import uk.co.thebodyshop.core.validation.validator.MarkDownPriceRowDatesValidator;

/**
 * @author Marcin
 */
@Target(
		{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = MarkDownPriceRowDatesValidator.class)
@Documented
public @interface MarkDownPriceRowDates
{
	String message() default "{uk.co.thebodyshop.core.validation.annotation.MarkDownPriceRowDates.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}