/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;
import uk.co.thebodyshop.core.product.visibility.functions.PredicatedFunction;

/**
 * @author vasanthramprakasam
 */
public class DefaultProductVisibilityStrategy implements ProductVisibilityStrategy
{

	 private final List<PredicatedFunction<ProductVisibilityContext, ProductVisibilityData>> predicatedFunctions;

	 public DefaultProductVisibilityStrategy(List<PredicatedFunction<ProductVisibilityContext, ProductVisibilityData>> predicatedFunctions)
	 {
			this.predicatedFunctions = predicatedFunctions;
	 }

	 @Override
	 public ProductVisibilityData getProductVisibility(ProductVisibilityContext visibilityContext)
	 {
			Optional<ProductVisibilityData> productVisibilityData = getPredicatedFunctions().stream().map(predicateFunction -> predicateFunction.applyIfValid(visibilityContext))
				.filter(Objects::nonNull).findFirst();
			if (productVisibilityData.isPresent())
			{
				 return productVisibilityData.get();
			}
			else
			{
				 return getSuccessVisibility(visibilityContext);
			}
	 }

	 @Override
	 public boolean canApply(ProductVisibilityContext visibilityContext)
	 {
			return true;
	 }

	 protected List<PredicatedFunction<ProductVisibilityContext, ProductVisibilityData>> getPredicatedFunctions()
	 {
			return predicatedFunctions;
	 }
}
