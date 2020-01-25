/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy.factory;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.strategy.ProductVisibilityStrategy;

/**
 * @author vasanthramprakasam
 */
public interface ProductVisibilityStrategyFactory
{
	 ProductVisibilityStrategy createStrategy(ProductVisibilityContext visibilityContext);
}
