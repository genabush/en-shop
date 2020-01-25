/*
 * Copyright (c)
 * 2019. THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.product.populators;

import java.util.Objects;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.core.helpers.VariantDataHelper;
import uk.co.thebodyshop.core.model.SwatchColourModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.data.SwatchColourData;

/**
 * @author Jagadeesh
 */
public class TbsOrderEntryVariantProductPopulator implements Populator<ProductModel,ProductData>
{

	private final VariantDataHelper variantDataHelper;

	public TbsOrderEntryVariantProductPopulator(VariantDataHelper variantDataHelper)
	{
		this.variantDataHelper = variantDataHelper;
	}

	@Override
	public void populate(final ProductModel productModel, final ProductData productData) throws ConversionException
	{
		if (productModel instanceof TbsVariantProductModel)
		{
			TbsVariantProductModel variantProductModel = (TbsVariantProductModel) productModel;
			productData.setSize(getVariantDataHelper().getFormattedSizeValue(variantProductModel));
			if (Objects.nonNull(variantProductModel.getColour()))
			{
				SwatchColourModel swatchColourModel = variantProductModel.getColour();
				SwatchColourData swatchColourData = new SwatchColourData();
				populateColour(swatchColourModel, swatchColourData);
				productData.setColour(swatchColourData);
			}
		}
	}

	private void populateColour(SwatchColourModel swatchColourModel, SwatchColourData swatchColourData)
	{
		swatchColourData.setCode(swatchColourModel.getCode());
		swatchColourData.setHexCode(swatchColourModel.getHexCode());
		swatchColourData.setName(swatchColourModel.getName());
	}

	protected VariantDataHelper getVariantDataHelper()
	{
		return variantDataHelper;
	}
}
