/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TbsVariantProductTypeValidator.class)
public @interface TbsVariantProductType
{
	String message() default "{uk.co.thebodyshop.core.validation.TbsVariantProductType.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
