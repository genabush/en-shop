/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.predicates;

import java.util.function.Predicate;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class ProductPhasedOutPredicate implements Predicate<ProductVisibilityContext>
{
	 @Override
	 public boolean test(ProductVisibilityContext visibilityContext)
	 {
	 	  final ProductModel productModel = visibilityContext.getProduct();
			if(productModel instanceof TbsVariantProductModel)
			{
				 final TbsVariantProductModel variantProductModel = (TbsVariantProductModel) productModel;
				 return ArticleApprovalStatus.DISCONTINUED.equals(variantProductModel.getApprovalStatus()) && variantProductModel.getPhaseInProduct() != null;
			}
			return false;
	 }
}
