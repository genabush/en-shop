/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cmsfacades.rendering.RestrictionContextProvider;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * @author Krishna
 */
public class CategoryBannerCMSComponentModelToDataPopulator implements Populator<CMSItemModel, Map<String, Object>>
{
	private static final String CATEGORY_NAME = "categoryName";
	private static final String CATEGORY_DESCRIPTION = "categoryDescription";
	private static final String CATEGORY_IMAGE_URL = "categoryImageUrl";
	
	private Predicate<CMSItemModel> cmsCategoryBannerCMSComponentTypePredicate;
	
	private RestrictionContextProvider restrictionContextProvider;
	
	@Override
	public void populate(CMSItemModel cmsItemModel, Map<String, Object> additionalProperties) throws ConversionException
	{
		if(getCmsCategoryBannerCMSComponentTypePredicate().test(cmsItemModel))
		{
			RestrictionData restrictionData = getRestrictionContextProvider().getRestrictionInContext();
			if(null!=restrictionData.getCategory()) 
			{
				CategoryModel category = restrictionData.getCategory();
				if(StringUtils.isNotEmpty(category.getName()))
				{
					additionalProperties.put(CATEGORY_NAME, category.getName());
				}
				if(StringUtils.isNotEmpty(category.getDescription()))
				{
					additionalProperties.put(CATEGORY_DESCRIPTION, category.getDescription());
				}
				if(StringUtils.isNotEmpty(category.getCategoryImageUrl()))
				{
					additionalProperties.put(CATEGORY_IMAGE_URL, category.getCategoryImageUrl());
				}
			}
		}
	}

	public CategoryBannerCMSComponentModelToDataPopulator(Predicate<CMSItemModel> cmsCategoryBannerCMSComponentTypePredicate, RestrictionContextProvider restrictionContextProvider)
	{
		this.cmsCategoryBannerCMSComponentTypePredicate = cmsCategoryBannerCMSComponentTypePredicate;
		this.restrictionContextProvider = restrictionContextProvider;
	}

	protected Predicate<CMSItemModel> getCmsCategoryBannerCMSComponentTypePredicate()
	{
		return cmsCategoryBannerCMSComponentTypePredicate;
	}

	protected RestrictionContextProvider getRestrictionContextProvider()
	{
		return restrictionContextProvider;
	}	
}
