/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.predicates;

import java.util.function.Predicate;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class ProductNotApprovedPredicate implements Predicate<ProductVisibilityContext>
{
	 @Override
	 public boolean test(ProductVisibilityContext visibilityContext)
	 {
			return !ArticleApprovalStatus.APPROVED.equals(visibilityContext.getProduct().getApprovalStatus());
	 }
}
