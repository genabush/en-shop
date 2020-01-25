/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helpers;

import java.util.Set;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Marcin
 */
public interface MarkDownPriceValidationHelper
{
	public boolean hasValidPrice(final MarkDownPriceRowModel priceRow);

	public boolean hasValidDates(final MarkDownPriceRowModel priceRow, final boolean checkStartDate);

	public boolean isStartDateUnique(final MarkDownPriceRowModel priceRow);

	public String getMarkDownPriceRowsErrors(final Set<MarkDownPriceRowModel> markDownPrices);

	public Double getCurrentBasePrice(final TbsVariantProductModel variantModel);
}
