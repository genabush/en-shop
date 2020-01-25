/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.predicates;

import java.util.function.Predicate;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class ProductMissingPredicate implements Predicate<ProductVisibilityContext>
{
	 @Override
	 public boolean test(ProductVisibilityContext visibilityContext)
	 {
     return visibilityContext.getProduct() == null;
	 }
}
