/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.functions;

import java.util.Optional;
import java.util.function.Function;

import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;

/**
 * @author vasanthramprakasam
 */
public abstract class AbstractProductVisibilityFunction implements Function<ProductVisibilityContext, ProductVisibilityData>
{
	 private boolean visible;

	 private String message;

	 protected static final String HOMEPAGE_PATH = "/";

	 @Override
	 public ProductVisibilityData apply(ProductVisibilityContext visibilityContext)
	 {
			ProductVisibilityData visibilityData = new ProductVisibilityData();
			visibilityData.setCode(Optional.ofNullable(visibilityContext.getProduct()).map(ProductModel::getCode).orElse(visibilityContext.getProductCode()));
			visibilityData.setMessage(getMessage());
			visibilityData.setVisible(isVisible());
			visibilityData.setRedirectUrl(getRedirectUrl(visibilityContext));
			return visibilityData;
	 }

	 protected abstract String getRedirectUrl(ProductVisibilityContext visibilityContext);

	 public boolean isVisible()
	 {
			return visible;
	 }

	 public void setVisible(boolean visible)
	 {
			this.visible = visible;
	 }

	 public String getMessage()
	 {
			return message;
	 }

	 public void setMessage(String message)
	 {
			this.message = message;
	 }
}
