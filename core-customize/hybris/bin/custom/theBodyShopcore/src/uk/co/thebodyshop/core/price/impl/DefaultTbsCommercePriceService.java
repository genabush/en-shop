/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.price.impl;

import de.hybris.platform.commerceservices.price.impl.DefaultCommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.util.PriceValue;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.price.TbsCommercePriceService;
import uk.co.thebodyshop.core.services.MarkDownPriceRowService;

/**
 * @author Jagadeesh
 */
public class DefaultTbsCommercePriceService extends DefaultCommercePriceService implements TbsCommercePriceService
{

	private MarkDownPriceRowService markDownPriceRowService;

	@Override
	public PriceInformation getWebPriceForProduct(final ProductModel product)
	{
		final MarkDownPriceRowModel markDownPrice = markDownPriceRowService.getActiveMarkDownPrice(product);
		final PriceInformation basePrice = super.getWebPriceForProduct(product);
		if (null != markDownPrice && null != basePrice)
		{
			return new PriceInformation(basePrice.getQualifiers(), new PriceValue(basePrice.getPriceValue().getCurrencyIso(), markDownPrice.getPrice(), basePrice.getPriceValue().isNet()));
		}
		return basePrice;
	}

	@Override
	public PriceInformation getBasePriceForProduct(final ProductModel product)
	{
		return super.getWebPriceForProduct(product);
	}

	public MarkDownPriceRowService getMarkDownPriceRowService()
	{
		return markDownPriceRowService;
	}

	public void setMarkDownPriceRowService(final MarkDownPriceRowService markDownPriceRowService)
	{
		this.markDownPriceRowService = markDownPriceRowService;
	}
}
