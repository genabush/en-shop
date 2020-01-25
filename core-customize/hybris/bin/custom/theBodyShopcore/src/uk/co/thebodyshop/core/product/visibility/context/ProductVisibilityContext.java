/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.visibility.context;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;

/**
 * @author vasanthramprakasam
 */
public class ProductVisibilityContext
{
	 private final CatalogVersionModel catalogVersion;

	 private final ProductModel product;

	 private final UserModel user;

	 private final String productCode;

	 public ProductVisibilityContext(CatalogVersionModel catalogVersion, ProductModel product, UserModel user, String productCode)
	 {
			this.catalogVersion = catalogVersion;
			this.product = product;
			this.user = user;
			this.productCode = productCode;
	 }

	 public CatalogVersionModel getCatalogVersion()
	 {
			return catalogVersion;
	 }

	 public ProductModel getProduct()
	 {
	 	 return product;
	 }

	 public UserModel getUser()
	 {
	 	 return user;
	 }

	 public String getProductCode()
	 {
			return productCode;
	 }
}
