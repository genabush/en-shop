/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.context;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Iterables;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author vasanthramprakasam
 */
public class DefaultProductVisibilityContextExtractor implements ProductVisibilityContextExtractor
{
	 private final SessionService sessionService;

	 private final UserService userService;

	 private final SearchRestrictionService searchRestrictionService;

	 private final ProductDao productDao;

	 public DefaultProductVisibilityContextExtractor(SessionService sessionService, UserService userService, SearchRestrictionService searchRestrictionService, ProductDao productDao)
	 {
			this.sessionService = sessionService;
			this.userService = userService;
			this.searchRestrictionService = searchRestrictionService;
			this.productDao = productDao;
	 }

	 @Override
	 public ProductVisibilityContext extractContext(String productCode, CatalogVersionModel catalogVersion)
	 {
			final ProductModel product = getSessionService().executeInLocalView(new SessionExecutionBody()
			{
				 @Override
				 public ProductModel execute()
				 {
				 	  getSearchRestrictionService().disableSearchRestrictions();
						ProductModel productModel = null;
						final List<ProductModel> products = getProductDao().findProductsByCode(catalogVersion, productCode);
						if (CollectionUtils.isNotEmpty(products))
						{
							 productModel = Iterables.getFirst(products, null);
						}
						return productModel;
				 }
			});
			final UserModel userModel = getUserService().getCurrentUser();
			return new ProductVisibilityContext(catalogVersion,product,userModel,productCode);
	 }

	 protected SessionService getSessionService()
	 {
			return sessionService;
	 }

	 protected UserService getUserService()
	 {
			return userService;
	 }

	 protected SearchRestrictionService getSearchRestrictionService()
	 {
			return searchRestrictionService;
	 }

	 protected ProductDao getProductDao()
	 {
			return productDao;
	 }
}
