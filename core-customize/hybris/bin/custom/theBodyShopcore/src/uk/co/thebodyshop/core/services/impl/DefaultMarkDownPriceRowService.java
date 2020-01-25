/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.core.model.product.ProductModel;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.services.MarkDownPriceRowService;

/**
 * @author Balakirshna
 */

public class DefaultMarkDownPriceRowService implements MarkDownPriceRowService
{
	@Override
	public MarkDownPriceRowModel getActiveMarkDownPrice(final ProductModel product)
	{
		if (product instanceof TbsVariantProductModel)
		{
			final TbsVariantProductModel variantProductModel = (TbsVariantProductModel) product;
			final Collection<MarkDownPriceRowModel> markDownPricesList = variantProductModel.getMarkDownPrices();
			if (CollectionUtils.isNotEmpty(markDownPricesList))
			{
				final long now = System.currentTimeMillis();
				final Date currentDate = new Date();
				final List<MarkDownPriceRowModel> filteredActivePrices = markDownPricesList.stream().filter(price -> price.getStartTime().before(currentDate) && price.getEndTime().after(currentDate) && price.getProduct().getMarkDownPrices().contains(price))
						.collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(filteredActivePrices))
				{
					Collections.sort(filteredActivePrices, new Comparator<MarkDownPriceRowModel>()
					{
						@Override
						public int compare(final MarkDownPriceRowModel price1, final MarkDownPriceRowModel price2)
						{
							return compareMarkDownPrices(price1, price2, now);
						}
					});
					return filteredActivePrices.get(0);
				}
			}
		}
		return null;
	}

	private int compareMarkDownPrices(final MarkDownPriceRowModel price1, final MarkDownPriceRowModel price2, final long now)
	{
		if (price1 != null && price2 != null && price1.getStartTime() != null && price2.getStartTime() != null)
		{
			return Long.compare(Math.abs(price1.getStartTime().getTime() - now), Math.abs(price2.getStartTime().getTime() - now));
		}
		else
		{
			return 0;
		}
	}
}
