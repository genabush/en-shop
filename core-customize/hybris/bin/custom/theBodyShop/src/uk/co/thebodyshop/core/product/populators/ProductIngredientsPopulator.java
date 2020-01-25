/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import uk.co.thebodyshop.core.model.KeyIngredientModel;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.product.data.ProductIngredientData;

/**
 * @author Marcin
 */
public class ProductIngredientsPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET>
{
	private CommonI18NService commonI18nService;

	@Override
	public void populate(final ProductModel source, final ProductData target) throws ConversionException
	{
		final TbsBaseProductModel baseProduct = getBaseProduct(source);
		if (Objects.nonNull(baseProduct))
		{
			populateKeyIngredients(baseProduct, target);
			populateProductStrapline(baseProduct, target);
		}
	}

	private TbsBaseProductModel getBaseProduct(final ProductModel productModel)
	{
		if (productModel instanceof TbsBaseProductModel)
		{
			return (TbsBaseProductModel) productModel;
		}
		else if (productModel instanceof TbsVariantProductModel)
		{
			final ProductModel baseProduct = ((TbsVariantProductModel) productModel).getBaseProduct();
			if (baseProduct != null)
			{
				return (TbsBaseProductModel) baseProduct;
			}
		}
		return null;
	}

	private void populateKeyIngredients(final TbsBaseProductModel source, final ProductData target)
	{
		target.setKeyIngredients(getConvertedIngredients(source.getKeyIngredients()));
	}

	private void populateProductStrapline(final TbsBaseProductModel source, final ProductData target)
	{
		target.setStraplines(getStraplines(source));
	}

	private List<ProductIngredientData> getConvertedIngredients(final Collection<KeyIngredientModel> ingredients)
	{
		final List<ProductIngredientData> convertedIngredients = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(ingredients))
		{
			for (final KeyIngredientModel keyIngredient : ingredients)
			{
				final ProductIngredientData productIngredientData = new ProductIngredientData();
				productIngredientData.setCode(keyIngredient.getCode());
				productIngredientData.setName(keyIngredient.getName());
				productIngredientData.setDescription(keyIngredient.getDescription());
				productIngredientData.setPageLink(keyIngredient.getPageLink());
				convertedIngredients.add(productIngredientData);
			}
		}
		return convertedIngredients;
	}

	private List<String> getStraplines(final TbsBaseProductModel product)
	{
		final List<String> straplines = new LinkedList<>();
		for (final String strapline : getMarketSpecificStraplines(product))
		{
			straplines.add(strapline);
		}
		return straplines;
	}

	private Collection<String> getMarketSpecificStraplines(final TbsBaseProductModel product)
	{
		Collection<String> localeStraplines = product.getStraplines();
		if (CollectionUtils.isEmpty(localeStraplines))
		{
			for (final LanguageModel language : commonI18nService.getCurrentLanguage().getFallbackLanguages())
			{
				localeStraplines = product.getStraplines(commonI18nService.getLocaleForLanguage(language));
				if (CollectionUtils.isNotEmpty(localeStraplines))
				{
					return localeStraplines;
				}
			}
		}
		return localeStraplines;
	}

	/**
	 * e
	 * 
	 * @return the commonI18nService
	 */
	public CommonI18NService getCommonI18nService()
	{
		return commonI18nService;
	}

	/**
	 * @param commonI18nService
	 *          the commonI18nService to set
	 */
	public void setCommonI18nService(CommonI18NService commonI18nService)
	{
		this.commonI18nService = commonI18nService;
	}
}
