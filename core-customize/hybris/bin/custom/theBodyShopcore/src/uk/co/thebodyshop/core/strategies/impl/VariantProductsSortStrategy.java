/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.strategies.impl;

import java.util.List;

import de.hybris.platform.commercefacades.product.data.VariantOptionData;

import uk.co.thebodyshop.core.enums.TbsBaseType;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.variant.solr.data.VariantData;

/**
 * @author Krishna
 */
public class VariantProductsSortStrategy
{
	public List<VariantOptionData> sortVariantProductsInProductPage(final List<VariantOptionData> variantOptions, final TbsBaseProductModel baseProduct)
	{
		if (null != baseProduct.getType() && baseProduct.getType().equals(TbsBaseType.SIZE))
		{
			variantOptions.sort((variantOption1, variantOption2) -> variantOption1.getSizeForSort().compareTo(variantOption2.getSizeForSort()));
		}
		if (null != baseProduct.getType() && baseProduct.getType().equals(TbsBaseType.COLOUR))
		{
			variantOptions.sort((variantOption1, variantOption2) -> variantOption2.getColourPosition().compareTo(variantOption1.getColourPosition()));
		}
		return variantOptions;
	}

	public List<VariantData> sortVariantProducts(final List<VariantData> variantsData, final TbsBaseProductModel baseProduct)
	{
		if (null != baseProduct.getType() && baseProduct.getType().equals(TbsBaseType.SIZE))
		{
			variantsData.sort((variantOption1, variantOption2) -> variantOption1.getSizeForSort().compareTo(variantOption2.getSizeForSort()));
		}
		if (null != baseProduct.getType() && baseProduct.getType().equals(TbsBaseType.COLOUR))
		{
			variantsData.sort((variantOption1, variantOption2) -> variantOption2.getColourPosition().compareTo(variantOption1.getColourPosition()));
		}
		return variantsData;
	}
}
