/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.converters.populator;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.converters.populator.StoreStockPopulator;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceStockData;
import de.hybris.platform.commercefacades.storelocator.data.StoreStockHolder;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.core.stock.TbsCommerceStockService;

/**
 * @author prateek.goel
 */
public class TbsStoreStockPopulator extends StoreStockPopulator
{

	TbsCommerceStockService tbsCommerceStockService;

	@Override
	public void populate(final StoreStockHolder storeStockHolder, final PointOfServiceStockData pointOfServiceStockData) throws ConversionException
	{
		super.populate(storeStockHolder, pointOfServiceStockData);
		if (isStockSystemEnabled())
		{
			final StockData stockData = pointOfServiceStockData.getStockData();
			stockData.setStockLevel(getTbsCommerceStockService().getProductStockLevelForStore(storeStockHolder.getProduct(), stockData.getStockLevel(), getBaseStoreService().getCurrentBaseStore()));
			if (stockData.getStockLevel() > 0)
			{
				stockData.setStockLevelStatus(StockLevelStatus.INSTOCK);
			}
			else
			{
				stockData.setStockLevelStatus(StockLevelStatus.OUTOFSTOCK);
			}
		}
	}

	protected TbsCommerceStockService getTbsCommerceStockService()
	{
		return tbsCommerceStockService;
	}

	public void setTbsCommerceStockService(final TbsCommerceStockService tbsCommerceStockService)
	{
		this.tbsCommerceStockService = tbsCommerceStockService;
	}

}
