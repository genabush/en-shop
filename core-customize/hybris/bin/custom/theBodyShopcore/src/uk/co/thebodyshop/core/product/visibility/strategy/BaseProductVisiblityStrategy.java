/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy;

import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;

/**
 * @author vasanthramprakasam
 */
public class BaseProductVisiblityStrategy implements ProductVisibilityStrategy
{

	 private final ProductVisibilityStrategy productVisibilityStrategy;

	 private final Function<ProductVisibilityContext, ProductVisibilityData> categoryRedirectFunction;

	 public BaseProductVisiblityStrategy(ProductVisibilityStrategy productVisibilityStrategy, Function<ProductVisibilityContext, ProductVisibilityData> categoryRedirectFunction)
	 {
			this.productVisibilityStrategy = productVisibilityStrategy;
			this.categoryRedirectFunction = categoryRedirectFunction;
	 }

	 @Override
	 public ProductVisibilityData getProductVisibility(ProductVisibilityContext visibilityContext)
	 {
			final UserModel user = visibilityContext.getUser();
			final TbsBaseProductModel baseProduct = (TbsBaseProductModel)visibilityContext.getProduct();
			if (CollectionUtils.isNotEmpty(baseProduct.getVariants()))
			{
				 for (final VariantProductModel variant : baseProduct.getVariants())
				 {
				 	  final ProductVisibilityContext variantContext = new ProductVisibilityContext(visibilityContext.getCatalogVersion(),
								variant,user,variant.getCode());
						final ProductVisibilityData variantVisibility = getProductVisibilityStrategy().getProductVisibility(variantContext);
						if (variantVisibility.isVisible())
						{
							 variantVisibility.setCode(baseProduct.getCode());
							 return variantVisibility;
						}
				 }
			}
			return getCategoryRedirectFunction().apply(visibilityContext);
	 }

	 @Override
	 public boolean canApply(ProductVisibilityContext visibilityContext)
	 {
			return visibilityContext.getProduct() instanceof TbsBaseProductModel;
	 }

	 protected ProductVisibilityStrategy getProductVisibilityStrategy()
	 {
			return productVisibilityStrategy;
	 }

	 protected Function<ProductVisibilityContext, ProductVisibilityData> getCategoryRedirectFunction()
	 {
			return categoryRedirectFunction;
	 }
}
