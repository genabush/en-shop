/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.populators;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantOptionsProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

/**
 * @author Marcin
 */
public class TbsCommerceSearchResultProductPopulator extends SearchResultVariantOptionsProductPopulator
{
	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		target.setIsVariant(Objects.nonNull(this.<Boolean> getValue(source, "isVariant")) ? this.<Boolean> getValue(source, "isVariant") : false);
		target.setPreviewDescription(this.<String> getValue(source, "previewDescription"));
		target.setVariantSize(this.<String> getValue(source, "variantSize"));
		if (CollectionUtils.isNotEmpty(this.<List<String>> getValue(source, "variants")))
		{
			final String variants = String.join(", ", this.<List<String>> getValue(source, "variants"));
			target.setVariants(variants);
		}
		target.setEmailMeWhenInStockToggle(Objects.nonNull(this.<Boolean> getValue(source, "emailMeWhenInStockToggle")) ? this.<Boolean> getValue(source, "emailMeWhenInStockToggle") : false);
		target.setMultiVariant(Objects.nonNull(this.<Boolean> getValue(source, "multiVariant")) ? this.<Boolean> getValue(source, "multiVariant") : false);
	}
}
