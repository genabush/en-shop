/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;

/**
 * @author Marcin
 */
public class ProductMarketPrepareInterceptor implements PrepareInterceptor<ProductModel>
{
	private final TbsCatalogVersionService catalogVersionService;

	@Override
	public void onPrepare(final ProductModel product, final InterceptorContext context) throws InterceptorException
	{
		if (context.isNew(product) && getCatalogVersionService().isMarketProductCatalog(product.getCatalogVersion()))
		{
			if (getCatalogVersionService().isStagedCatalog(product.getCatalogVersion()))
			{
				product.setApprovalStatus(ArticleApprovalStatus.READYTOBELOCALISED);
			}
		}
	}

	@Autowired
	public ProductMarketPrepareInterceptor(final TbsCatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @return the catalogVersionService
	 */
	protected TbsCatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}
}
