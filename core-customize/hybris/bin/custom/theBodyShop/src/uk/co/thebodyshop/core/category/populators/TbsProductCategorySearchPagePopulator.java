/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.category.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.ProductCategorySearchPagePopulator;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import uk.co.thebodyshop.core.services.TbsCategoryService;


public class TbsProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ITEM, SCAT, CATEGORY>
extends ProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ProductData, SCAT, CATEGORY>
{
	private Converter<CategoryModel, CategoryData> breadcrumbCategoryConverter;

	private TbsCategoryService tbsCategoryService;

	@Override
	public void populate(final ProductCategorySearchPageData<QUERY, RESULT, SCAT> source,
			final ProductCategorySearchPageData<STATE, ProductData, CATEGORY> target)
	{
		super.populate(source, target);
		if (null != source.getCurrentQuery() && source.getCurrentQuery() instanceof SolrSearchQueryData)
		{
			final CategoryModel category = getTbsCategoryService().getCategoryForSolrQuery((SolrSearchQueryData) source.getCurrentQuery());
			if (category != null)
			{
				target.setAmplienceIdForThirdSlot(category.getAmplienceIdForThirdSlot());
				target.setAmplienceIdForSixthSlot(category.getAmplienceIdForSixthSlot());
				target.setBreadcrumbCategories(getBreadcrumbCategoryConverter().convertAll(getTbsCategoryService().getBreadcrumbCategoriesInReverseOrderForCategory(category)));
			}
		}
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

	/**
	 * @return the breadcrumbCategoryConverter
	 */
	public Converter<CategoryModel, CategoryData> getBreadcrumbCategoryConverter()
	{
		return breadcrumbCategoryConverter;
	}

	/**
	 * @param breadcrumbCategoryConverter the breadcrumbCategoryConverter to set
	 */
	public void setBreadcrumbCategoryConverter(Converter<CategoryModel, CategoryData> breadcrumbCategoryConverter)
	{
		this.breadcrumbCategoryConverter = breadcrumbCategoryConverter;
	}
}
