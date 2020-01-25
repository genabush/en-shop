/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.functions;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class HomePageRedirectVisibilityFunction extends AbstractProductVisibilityFunction
{
	 @Override
	 protected String getRedirectUrl(ProductVisibilityContext visibilityContext)
	 {
			return HOMEPAGE_PATH;
	 }
}
