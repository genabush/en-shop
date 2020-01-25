/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.services.TbsCategoryService;

/**
 * @author Marcin
 */
public class TbsProductCategoriesPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends ProductCategoriesPopulator<SOURCE, TARGET>
{
	private TbsCategoryService tbsCategoryService;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final Collection<CategoryModel> categories = getCommerceProductService().getSuperCategoriesExceptClassificationClassesForProduct(productModel);
		productData.setCategories(getCategoryConverter().convertAll(categories));
		productData.setBreadcrumbCategories(getBreadcrumbCategories(productModel));
	}

	private List<CategoryData> getBreadcrumbCategories(final ProductModel product)
	{
		if (product instanceof TbsBaseProductModel)
		{
			return getCategoryConverter().convertAll(getSuperCategoriesForBaseProduct((TbsBaseProductModel) product));
		}
		else if (product instanceof VariantProductModel)
		{
			if (CollectionUtils.isNotEmpty(product.getSupercategories()))
			{
				return getCategoryConverter().convertAll(getTbsCategoryService().getBreadcrumbCategoriesInReverseOrderForCategory(product.getSupercategories().iterator().next()));
			}
			else
			{
				return getCategoryConverter().convertAll(getSuperCategoriesForBaseProduct((TbsBaseProductModel) ((VariantProductModel) product).getBaseProduct()));
			}
		}
		return null;

	}

	private List<CategoryModel> getSuperCategoriesForBaseProduct(final TbsBaseProductModel baseProduct)
	{
		if (Objects.nonNull(baseProduct))
		{
			if (Objects.nonNull(baseProduct.getPrimaryCategory()))
			{
				return getTbsCategoryService().getBreadcrumbCategoriesInReverseOrderForCategory(baseProduct.getPrimaryCategory());
			}
			else
			{
				if (CollectionUtils.isNotEmpty(baseProduct.getSupercategories()))
				{
					return getTbsCategoryService().getBreadcrumbCategoriesInReverseOrderForCategory(baseProduct.getSupercategories().iterator().next());
				}
			}
		}
		return null;
	}

	/**
	 * @return the tbsCategoryService
	 */
	public TbsCategoryService getTbsCategoryService()
	{
		return tbsCategoryService;
	}

	/**
	 * @param tbsCategoryService
	 *          the tbsCategoryService to set
	 */
	public void setTbsCategoryService(TbsCategoryService tbsCategoryService)
	{
		this.tbsCategoryService = tbsCategoryService;
	}
}
