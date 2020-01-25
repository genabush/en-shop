/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.functions;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.product.visibility.context.ProductVisibilityContext;

/**
 * @author vasanthramprakasam
 */
public class CategoryRedirectProductVisibilityFunction extends AbstractProductVisibilityFunction
{
	 private final UrlResolver<CategoryModel> categoryModelUrlResolver;

	 private final CategoryService categoryService;

	 public CategoryRedirectProductVisibilityFunction(UrlResolver<CategoryModel> categoryModelUrlResolver, CategoryService categoryService)
	 {
			this.categoryModelUrlResolver = categoryModelUrlResolver;
			this.categoryService = categoryService;
	 }

	 @Override
	 protected String getRedirectUrl(ProductVisibilityContext visibilityContext)
	 {
			return getRedirectionUrl(visibilityContext.getProduct(),visibilityContext.getUser());
	 }

	 private String getRedirectionUrl(final ProductModel productModel, final UserModel user)
	 {
			if (productModel instanceof TbsBaseProductModel)
			{
				 return getCategoryRedirectUrl((TbsBaseProductModel) productModel, user);
			}
			else if (productModel instanceof VariantProductModel)
			{
				 final VariantProductModel variant = (VariantProductModel) productModel;
				 if (variant.getBaseProduct() instanceof TbsBaseProductModel)
				 {
						return getCategoryRedirectUrl((TbsBaseProductModel) variant.getBaseProduct(), user);
				 }
			}
			return HOMEPAGE_PATH;
	 }

	 private String getCategoryRedirectUrl(final TbsBaseProductModel baseProduct, final UserModel user)
	 {
			// primary category check
			String redirectUrl = getCategoryUrl(baseProduct.getPrimaryCategory(), user);
			if (null != redirectUrl)
			{
				 return redirectUrl;
			}
			if (CollectionUtils.isNotEmpty(baseProduct.getSupercategories()))
			{
				 // first level category check
				 redirectUrl = findCategoryUrl(baseProduct.getSupercategories(), user);
				 if (null != redirectUrl)
				 {
						return redirectUrl;
				 }
				 // all supercategories check
				 for (final CategoryModel superCategory : baseProduct.getSupercategories())
				 {
						final Collection<CategoryModel> allCategories = getCategoryService().getAllSupercategoriesForCategory(superCategory);
						if (CollectionUtils.isNotEmpty(allCategories))
						{
							 redirectUrl = findCategoryUrl(allCategories, user);
							 if (null != redirectUrl)
							 {
									return redirectUrl;
							 }
						}
				 }
			}
			return HOMEPAGE_PATH;
	 }

	 private String findCategoryUrl(final Collection<CategoryModel> categories, final UserModel user)
	 {
			String redirectUrl = null;
			for (final CategoryModel category : categories)
			{
				 redirectUrl = getCategoryUrl(category, user);
				 if (null != redirectUrl)
				 {
						return redirectUrl;
				 }
			}
			return null;
	 }

	 private String getCategoryUrl(final CategoryModel category, final UserModel user)
	 {
			if (null != category)
			{
				 if (CollectionUtils.isNotEmpty(category.getAllowedPrincipals()) && category.getAllowedPrincipals().contains(user))
				 {
						return getCategoryModelUrlResolver().resolve(category);
				 }
				 if (CollectionUtils.isNotEmpty(user.getAllGroups()))
				 {
						for (final PrincipalGroupModel principalGroup : user.getAllGroups())
						{
							 if (CollectionUtils.isNotEmpty(category.getAllowedPrincipals()) && category.getAllowedPrincipals().contains(principalGroup))
							 {
									return getCategoryModelUrlResolver().resolve(category);
							 }
						}
				 }
			}
			return null;
	 }

	 protected UrlResolver<CategoryModel> getCategoryModelUrlResolver()
	 {
			return categoryModelUrlResolver;
	 }

	 protected CategoryService getCategoryService()
	 {
			return categoryService;
	 }
}
