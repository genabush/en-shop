/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.price.factory;

import de.hybris.platform.catalog.jalo.CatalogAwareEurope1PriceFactory;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.PriceValue;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.services.MarkDownPriceRowService;

/**
 * @author Jagadeesh
 */
public class TbsEurope1PriceFactory extends CatalogAwareEurope1PriceFactory
{

	private MarkDownPriceRowService markDownPriceRowService;

	private ModelService modelService;

	@Override
	public PriceValue getBasePrice(final AbstractOrderEntry entry) throws JaloPriceFactoryException
	{
		final PriceValue basePrice = super.getBasePrice(entry);
		if (null != entry.getProduct() && null != basePrice)
		{
			final ProductModel product = modelService.get(entry.getProduct());
			final MarkDownPriceRowModel markDownPriceRow = markDownPriceRowService.getActiveMarkDownPrice(product);
			if (null != markDownPriceRow)
			{
				return new PriceValue(basePrice.getCurrencyIso(), markDownPriceRow.getPrice(), basePrice.isNet());
			}
		}
		return basePrice;
	}

	protected MarkDownPriceRowService getMarkDownPriceRowService()
	{
		return markDownPriceRowService;
	}

	public void setMarkDownPriceRowService(final MarkDownPriceRowService markDownPriceRowService)
	{
		this.markDownPriceRowService = markDownPriceRowService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
