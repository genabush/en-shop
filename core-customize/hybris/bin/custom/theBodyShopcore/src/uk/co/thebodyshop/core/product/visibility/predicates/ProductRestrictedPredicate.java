/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.predicates;

import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class ProductRestrictedPredicate implements Predicate<ProductVisibilityContext>
{
	 @Override
	 public boolean test(ProductVisibilityContext visibilityContext)
	 {
	 	  final ProductModel productModel = visibilityContext.getProduct();
	 	  final UserModel user = visibilityContext.getUser();
			Set<PrincipalModel> allowedPrincipals = productModel.getRestrictedFor();
			if (CollectionUtils.isEmpty(allowedPrincipals))
			{
				 if (productModel instanceof VariantProductModel)
				 {
						allowedPrincipals = ((VariantProductModel) productModel).getBaseProduct().getRestrictedFor();
						if (CollectionUtils.isEmpty(allowedPrincipals))
						{
							 return false;
						}
				 }
			}
			// specific user check
			if (productModel.getRestrictedFor().contains(user))
			{
				 return false;
			}
			// all user groups check
			if (CollectionUtils.isNotEmpty(user.getAllGroups()))
			{
				 for (final PrincipalModel principalModel : productModel.getRestrictedFor())
				 {
						if (user.getAllGroups().contains(principalModel))
						{
							 return false;
						}
				 }
			}
			return true;
	 }
}
