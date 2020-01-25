/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;

/**
 * @author vasanthramprakasam
 */
public interface ProductVisibilityStrategy
{
	 ProductVisibilityData getProductVisibility(ProductVisibilityContext visibilityContext);

	 boolean canApply(ProductVisibilityContext visibilityContext);

	 default ProductVisibilityData getSuccessVisibility(ProductVisibilityContext visibilityContext)
	 {
			ProductVisibilityData visibilityData = new ProductVisibilityData();
			visibilityData.setCode(visibilityContext.getProduct().getCode());
			visibilityData.setVisible(true);
			return visibilityData;
	 }
}
