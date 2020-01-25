/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import uk.co.thebodyshop.core.markdownprice.marker.MarkDownPriceProductModificationMarker;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;

/**
 * @author Jagadeesh
 */
public class MarkDownPricePrepareInterceptor implements PrepareInterceptor<MarkDownPriceRowModel>
{
	private final MarkDownPriceProductModificationMarker markDownPriceProductModificationMarker;

	private final CatalogTypeService catalogTypeService;

	public MarkDownPricePrepareInterceptor(final MarkDownPriceProductModificationMarker markDownPriceProductModificationMarker, final CatalogTypeService catalogTypeService)
	{
		this.markDownPriceProductModificationMarker = markDownPriceProductModificationMarker;
		this.catalogTypeService = catalogTypeService;
	}

	@Override
	public void onPrepare(final MarkDownPriceRowModel markDownPriceModel, final InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isNew(markDownPriceModel))
		{
			updateCatalogVersion(markDownPriceModel);
		}
		markDownPriceProductModificationMarker.markProductAsModifiedIfFlagSet(markDownPriceModel, ctx, true);
	}

	protected void updateCatalogVersion(final MarkDownPriceRowModel markDownPriceRowModel)
	{
		CatalogVersionModel catalogVersionModel = markDownPriceRowModel.getCatalogVersion();
		if (catalogVersionModel == null)
		{
			final ProductModel productModel = markDownPriceRowModel.getProduct();
			if (productModel != null)
			{
				catalogVersionModel = this.getCatalogTypeService().getCatalogVersionForCatalogVersionAwareModel(productModel);
				if (catalogVersionModel != null)
				{
					markDownPriceRowModel.setCatalogVersion(catalogVersionModel);
				}
			}
		}
	}

	protected MarkDownPriceProductModificationMarker getMarkDownPriceProductModificationMarker()
	{
		return markDownPriceProductModificationMarker;
	}

	protected CatalogTypeService getCatalogTypeService()
	{
		return catalogTypeService;
	}
}
