/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.validation.annotation;

import uk.co.thebodyshop.core.validation.validator.ProductBadgesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ProductBadgesValidator.class)
@Documented
public @interface ProductBadges {

    String message() default "{uk.co.thebodyshop.core.validation.annotation.ProductBadges.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
