/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.predicates;

import java.util.function.Predicate;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class ProductHasNoPricePredicate implements Predicate<ProductVisibilityContext>
{

	 private final TbsCatalogVersionService tbsCatalogVersionService;

	 private final CommercePriceService commercePriceService;

	 public ProductHasNoPricePredicate(TbsCatalogVersionService tbsCatalogVersionService, CommercePriceService commercePriceService)
	 {
			this.tbsCatalogVersionService = tbsCatalogVersionService;
			this.commercePriceService = commercePriceService;
	 }

	 @Override
	 public boolean test(ProductVisibilityContext visibilityContext)
	 {
			final ProductModel productModel = visibilityContext.getProduct();
			if (getTbsCatalogVersionService().isGlobalProductCatalog(productModel.getCatalogVersion()))
			{
				 return true;
			}
			return getCommercePriceService().getWebPriceForProduct(productModel) == null;
	 }

	 protected TbsCatalogVersionService getTbsCatalogVersionService()
	 {
			return tbsCatalogVersionService;
	 }

	 protected CommercePriceService getCommercePriceService()
	 {
			return commercePriceService;
	 }
}
