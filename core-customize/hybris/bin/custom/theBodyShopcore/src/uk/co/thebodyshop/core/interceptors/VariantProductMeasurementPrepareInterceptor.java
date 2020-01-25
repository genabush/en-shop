/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * On product creation in the market catalog, set the size/unit attributes to the values given by ERP (measurement/unitOfMeasure).
 *
 * @author Krishna
 */
public class VariantProductMeasurementPrepareInterceptor implements PrepareInterceptor<TbsVariantProductModel>
{
	private final UnitService unitService;
	private final TbsCatalogVersionService tbsCatalogVersionService;

	@Override
	public void onPrepare(final TbsVariantProductModel variantProductModel, final InterceptorContext ctx) throws InterceptorException
	{
			if (ctx.getModelService().isNew(variantProductModel) && getTbsCatalogVersionService().isMarketProductCatalog(variantProductModel.getCatalogVersion()))
			{
				if (null != variantProductModel.getErpMeasurement() && StringUtils.isNotBlank(variantProductModel.getErpMeasurement().toString()))
				{
					variantProductModel.setSize(variantProductModel.getErpMeasurement().toString());
				}

				if (StringUtils.isNotBlank(variantProductModel.getErpUnitOfMeasure()))
				{
					final UnitModel unit = getUnitService().getUnitForCode(variantProductModel.getErpUnitOfMeasure());

					if (null != unit)
					{
						variantProductModel.setUnit(unit);
					}
				}
			}
	}

	@Autowired
	public VariantProductMeasurementPrepareInterceptor(final UnitService unitService, final TbsCatalogVersionService tbsCatalogVersionService)
	{
		this.unitService = unitService;
		this.tbsCatalogVersionService = tbsCatalogVersionService;
	}

	protected TbsCatalogVersionService getTbsCatalogVersionService()
	{
		return tbsCatalogVersionService;
	}

	protected UnitService getUnitService()
	{
		return unitService;
	}
}
