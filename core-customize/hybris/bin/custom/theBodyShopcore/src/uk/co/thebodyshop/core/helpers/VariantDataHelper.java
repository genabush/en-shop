/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helpers;

import java.math.BigDecimal;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.variant.solr.data.StockData;

/**
 * @author Marcin
 */
public interface VariantDataHelper
{
	String getFormattedSizeValue(final TbsVariantProductModel variant);

	PriceData getFormattedVariantPrice(final VariantProductModel variantModel);

	StockData getVariantStockStatus(final VariantProductModel variantModel, final BaseStoreModel baseStore);

	String getPricePerMetric(final VariantProductModel variantModel, final BaseStoreModel baseStore, BigDecimal price);

	Double getSizeForSort(final TbsVariantProductModel variantModel);

	Integer getLoyaltyPoints(TbsVariantProductModel variantModel, BaseStoreModel baseStore, BigDecimal price);
}
