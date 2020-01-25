/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.strategy;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;

/**
 * @author vasanthramprakasam
 */
public class PhaseInProductVisibilityStrategy implements ProductVisibilityStrategy
{
	 private final Predicate<ProductVisibilityContext> phaseInProductPredicate;

	 private final ProductVisibilityStrategy productVisibilityStrategy;

	 private final Function<ProductVisibilityContext, ProductVisibilityData> homePageRedirectFunction;

	 private final Function<ProductVisibilityContext, ProductVisibilityData> productRedirectFunction;

	 private final Function<ProductVisibilityContext, ProductVisibilityData> categoryRedirectFunction;

	 public PhaseInProductVisibilityStrategy(Predicate<ProductVisibilityContext> phaseInProductPredicate, ProductVisibilityStrategy productVisibilityStrategy, Function<ProductVisibilityContext, ProductVisibilityData> homePageRedirectFunction,
			 Function<ProductVisibilityContext, ProductVisibilityData> productRedirectFunction, Function<ProductVisibilityContext, ProductVisibilityData> categoryRedirectFunction)
	 {
			this.phaseInProductPredicate = phaseInProductPredicate;
			this.productVisibilityStrategy = productVisibilityStrategy;
			this.homePageRedirectFunction = homePageRedirectFunction;
			this.productRedirectFunction = productRedirectFunction;
			this.categoryRedirectFunction = categoryRedirectFunction;
	 }

	 @Override
	 public ProductVisibilityData getProductVisibility(ProductVisibilityContext visibilityContext)
	 {
			final ProductModel product = visibilityContext.getProduct();
			final Optional<TbsVariantProductModel> phaseInProduct = Optional.ofNullable(product).filter(TbsVariantProductModel.class::isInstance)
					.map(TbsVariantProductModel.class::cast).map(TbsVariantProductModel::getPhaseInProduct);
			if (phaseInProduct.isPresent())
			{
				 ProductVisibilityContext visibilityContextForPhaseIn = new ProductVisibilityContext(visibilityContext.getCatalogVersion(),phaseInProduct.get(),visibilityContext.getUser(),visibilityContext.getProductCode());
				 ProductVisibilityData visibilityData = getProductVisibilityStrategy().getProductVisibility(visibilityContextForPhaseIn);
				 if(visibilityData.isVisible())
				 {
				 	 //redirect to phase in pdp
						visibilityData = getProductRedirectFunction().apply(visibilityContextForPhaseIn);
						visibilityData.setCode(product.getCode());
						return visibilityData;
				 }
				 return getCategoryRedirectFunction().apply(visibilityContextForPhaseIn);
			}
			else
			{
				 return getHomePageRedirectFunction().apply(visibilityContext);
			}
	 }

	 @Override
	 public boolean canApply(ProductVisibilityContext visibilityContext)
	 {
			return getPhaseInProductPredicate().test(visibilityContext);
	 }

	 protected Predicate<ProductVisibilityContext> getPhaseInProductPredicate()
	 {
			return phaseInProductPredicate;
	 }

	 protected ProductVisibilityStrategy getProductVisibilityStrategy()
	 {
			return productVisibilityStrategy;
	 }

	 protected Function<ProductVisibilityContext, ProductVisibilityData> getHomePageRedirectFunction()
	 {
			return homePageRedirectFunction;
	 }

	 protected Function<ProductVisibilityContext, ProductVisibilityData> getProductRedirectFunction()
	 {
			return productRedirectFunction;
	 }

	 protected Function<ProductVisibilityContext, ProductVisibilityData> getCategoryRedirectFunction()
	 {
			return categoryRedirectFunction;
	 }
}
