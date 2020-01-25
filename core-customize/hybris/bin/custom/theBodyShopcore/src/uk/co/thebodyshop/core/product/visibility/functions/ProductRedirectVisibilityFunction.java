/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.functions;

import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class ProductRedirectVisibilityFunction extends AbstractProductVisibilityFunction
{
	 private final UrlResolver<ProductModel> productModelUrlResolver;

	 public ProductRedirectVisibilityFunction(UrlResolver<ProductModel> productModelUrlResolver)
	 {
			this.productModelUrlResolver = productModelUrlResolver;
	 }

	 @Override
	 protected String getRedirectUrl(ProductVisibilityContext visibilityContext)
	 {
			return getProductModelUrlResolver().resolve(visibilityContext.getProduct());
	 }

	 protected UrlResolver<ProductModel> getProductModelUrlResolver()
	 {
			return productModelUrlResolver;
	 }
}
