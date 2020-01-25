/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.visibility.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContextExtractor;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;
import uk.co.thebodyshop.core.product.visibility.data.ProductVisibilityData;
import uk.co.thebodyshop.core.product.visibility.strategy.factory.ProductVisibilityStrategyFactory;

/**
 * @author Marcin
 */
public class DefaultProductVisibilityService implements ProductVisibilityService
{

	 private final ProductVisibilityContextExtractor productVisibilityContextExtractor;

	 private final ProductVisibilityStrategyFactory productVisibilityStrategyFactory;

	 public DefaultProductVisibilityService(ProductVisibilityContextExtractor productVisibilityContextExtractor, ProductVisibilityStrategyFactory productVisibilityStrategyFactory)
	 {
			this.productVisibilityContextExtractor = productVisibilityContextExtractor;
			this.productVisibilityStrategyFactory = productVisibilityStrategyFactory;
	 }

	 @Override
	 public ProductVisibilityData getVisibiltyInfo(String productCode, CatalogVersionModel catalogVersion)
	 {
			final ProductVisibilityContext productVisibilityContext = getProductVisibilityContextExtractor().extractContext(productCode,catalogVersion);
	 	  return getProductVisibilityStrategyFactory().createStrategy(productVisibilityContext).getProductVisibility(productVisibilityContext);
	 }

	 protected ProductVisibilityContextExtractor getProductVisibilityContextExtractor()
	 {
			return productVisibilityContextExtractor;
	 }

	 protected ProductVisibilityStrategyFactory getProductVisibilityStrategyFactory()
	 {
			return productVisibilityStrategyFactory;
	 }
}
